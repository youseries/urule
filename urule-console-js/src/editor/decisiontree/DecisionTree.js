/**
 * Created by Jacky.gao on 2016/2/22.
 */
DecisionTree=function(container){
    this.container=container;
    this.topNode=new VariableTreeNode();
    this.initToolbar();
    var content=$("<div style='text-align: center'>");
    container.append(content);
    content.append(this.topNode.container);
};

DecisionTree.prototype.initToolbar=function(){
    var file=_getRequestParameter("file");
    var version=_getRequestParameter("version")||"";
    if(!file || file.length<1){
        URule.alert("未指定具体的决策树文件！");
        return;
    }

    var saveButton = '<div class="btn-group navbar-btn" style="margin-top:0px;margin-bottom: 0px" role="group" aria-label="...">'+
        '<button id="saveButton" type="button" class="btn btn-default navbar-btn" ><i class="icon-save"></i> 保存</button>' +
        '<button id="saveButtonNewVersion" type="button" class="btn btn-default navbar-btn" ><i class="icon-save"></i> 保存新版本</button>' +
        '</div>';
    var toolbarHtml='<nav class="navbar navbar-default" style="margin: 5px">'+
        '<div>'+
        '<div>'+
        '<div class="btn-group navbar-btn" style="margin-left:5px;margin-top:0px;margin-bottom: 0px" role="group" aria-label="...">'+
        '<button id="configVarButton" type="button" class="btn btn-default"><i class="icon-tasks"></i> 导入变量库</button>'+
        '<button id="configConstantsButton" type="button" class="btn btn-default"><i class="icon-th-list"></i> 导入常量库</button>'+
        '<button id="configActionButton" type="button" class="btn btn-default"><i class="icon-bolt"></i> 导入动作库</button>'+
        '<button id="configParameterButton" type="button" class="btn btn-default"><i class="icon-th"></i> 导入参数库</button>'+
        '</div>'+
        saveButton +
        ' </div>'+
        '</div>'+
        '</nav>';
    var toolbar=$(toolbarHtml);
    toolbar.css({
        diaplay:"inline-block"
    });
    var toolbarContainer=$("<div>");
    toolbarContainer.append(toolbar);
    this.container.append(toolbarContainer);
    var self=this;
    $("#configVarButton").click(function(){
        if(!self.configVarDialog){
            self.configVarDialog=new urule.ConfigVariableDialog(self);
        }
        self.configVarDialog.open();
    });

    $("#configConstantsButton").click(function(){
        if(!self.configConstantDialog){
            self.configConstantDialog=new urule.ConfigConstantDialog(self);
        }
        self.configConstantDialog.open();
    });

    $("#configActionButton").click(function(){
        if(!self.configActionDialog){
            self.configActionDialog=new urule.ConfigActionDialog(self);
        }
        self.configActionDialog.open();
    });

    $("#configParameterButton").click(function(){
        if(!self.configParameterDialog){
            self.configParameterDialog=new urule.ConfigParameterDialog(self);
        }
        self.configParameterDialog.open();
    });

    $("#saveButton").click(function(){
        _save(false);
    });
    $("#saveButtonNewVersion").click(function(){
        _save(true);
    });
    $("#saveButton").addClass("disabled");
    $("#saveButtonNewVersion").addClass("disabled");
    _loadDecisionTreeFileData();

    function _save(newVersion){
        if($("#saveButton").hasClass("disabled")){
            return false;
        }
        var xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        xml+="<decision-tree>";
        $.each(parameterLibraries,function(index,item){
            xml+="<import-parameter-library path=\""+item+"\"/>";
        });
        $.each(variableLibraries,function(index,item){
            xml+="<import-variable-library path=\""+item+"\"/>";
        });
        $.each(constantLibraries,function(index,item){
            xml+="<import-constant-library path=\""+item+"\"/>";
        });
        $.each(actionLibraries,function(index,item){
            xml+="<import-action-library path=\""+item+"\"/>";
        });
        try{
            xml+=self.topNode.toXml();
        }catch(error){
            URule.alert(error);
            return;
        }
        xml+="</decision-tree>";
        xml=encodeURI(xml);
        var url=(uruleServer || "" )+"urule?action=savexml&file="+file+"";
        var dialog=$("<div style='width:100px;height:50px'>文件保存中...</div>");
        $.ajax({
            cache:false,
            url:url,
            type:"POST",
            data:{xml:xml,newVersion:newVersion},
            beforeSend:function(req){
                dialog.dialog({
                    modal : true,
                    height:80,
                    width:50,
                    open : function(event, ui) {
                        $(".ui-dialog-titlebar",$(this).parent()).hide();
                    }
                });
            },
            error:function(response){
                dialog.dialog("close");
                if(response && response.responseText){
                    bootbox.alert("<span style='color: red'>保存失败："+response.responseText+"</span>");
                }else{
                    bootbox.alert("<span style='color: red'>保存失败,服务端出错</span>");
                }
            },
            success:function(data){
                cancelDirty();
                dialog.dialog("close");
            }
        });
    };

    function _loadDecisionTreeFileData(){
        var url=(uruleServer || "")+"urule?action=loadxml&files="+file+","+version+"";
        $.ajax({
            cache:false,
            dataType:"json",
            type:'POST',
            url:url,
            error:function(response){
                if(response && response.responseText){
                    bootbox.alert("<span style='color: red'>加载文件失败："+response.responseText+"</span>");
                }else{
                    bootbox.alert("<span style='color: red'>加载文件失败,服务端出错</span>");
                }
            },
            success:function(data){
                var treeData=data[0];
                var libraries=treeData["libraries"];
                if(libraries){
                    for(var i=0;i<libraries.length;i++){
                        var lib=libraries[i];
                        var type=lib["type"];
                        var path=lib["path"];
                        switch(type){
                            case "Constant":
                                constantLibraries.push(path);
                                break;
                            case "Action":
                                actionLibraries.push(path);
                                break;
                            case "Variable":
                                variableLibraries.push(path);
                                break;
                            case "Parameter":
                                parameterLibraries.push(path);
                                break;
                        }
                    }
                }
                refreshActionLibraries();
                refreshConstantLibraries();
                refreshVariableLibraries();
                refreshParameterLibraries();
                refreshFunctionLibraries();
                self.topNode.initData(treeData["variableTreeNode"]);
                cancelDirty();
            }
        });
    };

};

function _getRequestParameter(name){
    var value=null;
    var params=window.location.search.substring(1).split("&");
    for(var i=0;i<params.length;i++){
        var param=params[i];
        if(param.indexOf("=")==-1){
            continue;
        }
        var pair=param.split("=");
        var key=pair[0];
        if(key==name){
            value=pair[1];
            break;
        }
    }
    return value;
};

window._setDirty=function(){
    if(window._dirty){
        return;
    }
    window._dirty=true;
    $("#saveButton").html("<i class='icon-save'></i> *保存");
    $("#saveButton").removeClass("disabled");
    $("#saveButtonNewVersion").html("<i class='icon-save'></i> *保存新版本");
    $("#saveButtonNewVersion").removeClass("disabled");
};

function cancelDirty(){
    if(!window._dirty){
        return;
    }
    window._dirty=false;
    $("#saveButton").html("<i class='icon-save'></i> 保存");
    $("#saveButton").addClass("disabled");
    $("#saveButtonNewVersion").html("<i class='icon-save'></i> 保存新版本");
    $("#saveButtonNewVersion").addClass("disabled");
};
