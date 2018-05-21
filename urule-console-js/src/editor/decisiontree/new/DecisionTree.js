/**
 * Created by Jacky.Gao on 2016/3/27.
 */
import {getParameter,ajaxSave} from '../../../Utils.js';
import Context from './Context.js';
import VariableNode from './VariableNode.js';

export default class DecisionTree{
    constructor(container){
        this.container=container;
        this.properties=[];
        this.initToolbar();
        this.initRemarkContainer();
        this.initPropertyContainer();

        var treeContainer=$("<div style='position: relative;text-align: center'>");
        this.container.append(treeContainer);
        var context=new Context(treeContainer);
        this.topNode=new VariableNode(context,null,true);
        context.topNode=this.topNode;
        var left = 10,top = 10;
        this.topNode.nodeContainer.css({
            position:"absolute",
            left:left,
            top:top
        });
    }
    initRemarkContainer(){
        var remarkContainer=$("<div style='margin: 5px;'></div>");
        this.container.append(remarkContainer);
        this.remark=new Remark(remarkContainer);
    }
    initPropertyContainer(){
        var propContainer=$("<div style='margin: 5px;border: solid 1px #dddddd;border-radius:5px'></div>");
        this.container.append(propContainer);
        this.propertyContainer=$("<span>");
        this.propertyContainer.css({
            "padding":"10px"
        });
        var addProp=$("<button type='button' class='rule-add-property btn btn-link'>添加属性</button>");
        propContainer.append(addProp);
        propContainer.append(this.propertyContainer);
        var self=this;
        var onClick=function(menuItem){
            var prop=new urule.RuleProperty(self,menuItem.name,menuItem.defaultValue,menuItem.editorType);
            self.addProperty(prop);
        };
        self.menu=new URule.menu.Menu({
            menuItems:[{
                label:"优先级",
                name:"salience",
                defaultValue:"10",
                editorType:1,
                onClick:onClick
            },{
                label:"生效日期",
                name:"effective-date",
                defaultValue:"",
                editorType:2,
                onClick:onClick
            },{
                label:"失效日期",
                name:"expires-date",
                defaultValue:"",
                editorType:2,
                onClick:onClick
            },{
                label:"是否启用",
                name:"enabled",
                defaultValue:true,
                editorType:3,
                onClick:onClick
            },{
                label:"允许调试信息输出",
                name:"debug",
                defaultValue:true,
                editorType:3,
                onClick:onClick
            }]
        });
        addProp.click(function(e){
            self.menu.show(e);
        });
    }

    addProperty(property){
        this.propertyContainer.append(property.getContainer());
        this.properties.push(property);
        window._setDirty();
    };
    
    initToolbar(){
        var file=getParameter("file");
        var version=getParameter("version")||"";
        if(!file || file.length<1){
            alert("未指定具体的决策树文件！");
            return;
        }

        var saveButton = `<div class="btn-group btn-group-sm navbar-btn" style="margin-top:0px;margin-bottom: 0px" role="group" aria-label="...">
                <button id="saveButton" type="button" class="btn btn-default navbar-btn" ><i class="rf rf-save"></i> 保存</button> 
                <button id="saveButtonNewVersion" type="button" class="btn btn-default navbar-btn" ><i class="rf rf-savenewversion"></i> 保存新版本</button>
            </div>`;
        var toolbarHtml=`<nav class="navbar navbar-default" style="margin: 5px">
            <div>
                <div>
                    <div class="btn-group btn-group-sm navbar-btn" style="margin-left:5px;margin-top:0px;margin-bottom: 0px" role="group" aria-label="...">
                        <button id="configVarButton" type="button" class="btn btn-default"><i class="rf rf-variable"></i> 变量库</button>
                        <button id="configConstantsButton" type="button" class="btn btn-default"><i class="rf rf-constant"></i> 常量库</button>
                        <button id="configActionButton" type="button" class="btn btn-default"><i class="rf rf-action"></i> 动作库</button>
                        <button id="configParameterButton" type="button" class="btn btn-default"><i class="rf rf-parameter"></i> 参数库</button>
                    </div>
                    ${saveButton} 
                 </div>
            </div>
        </nav>`;
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
            xml+="<decision-tree";
            for(var i=0;i<self.properties.length;i++){
                var prop=self.properties[i];
                xml+=" "+prop.toXml();
            }
            xml+=">";
            xml+=self.remark.toXml();
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
                alert(error);
                return;
            }
            xml+="</decision-tree>";
            xml=encodeURIComponent(xml);
            let postData={content:xml,file,newVersion};
            const url=window._server+'/common/saveFile';
            if(newVersion){
                bootbox.prompt("请输入新版本描述.",function (versionComment) {
                    if(!versionComment){
                        return;
                    }
                    postData.versionComment=versionComment;
                    ajaxSave(url,postData,function () {
                        cancelDirty();
                    })
                });
            }else{
                ajaxSave(url,postData,function () {
                    cancelDirty();
                })
            }
        };

        function _loadDecisionTreeFileData(){
            var url=window._server+'/common/loadXml';
            $.ajax({
                data:{files:file},
                type:'POST',
                url:url,
                error:function(req,error){
                    alert("加载文件失败！");
                },
                success:function(data){
                    var treeData=data[0];
                    self.remark.setData(treeData["remark"]);
                    var salience=treeData["salience"];
                    if(salience){
                        self.addProperty(new urule.RuleProperty(self,"salience",salience,1));
                    }
                    var loop=treeData["loop"];
                    if(loop!=null){
                        self.addProperty(new urule.RuleProperty(self,"loop",loop,3));
                    }
                    var effectiveDate=treeData["effectiveDate"];
                    if(effectiveDate){
                        self.addProperty(new urule.RuleProperty(self,"effective-date",effectiveDate,2));
                    }
                    var expiresDate=treeData["expiresDate"];
                    if(expiresDate){
                        self.addProperty(new urule.RuleProperty(self,"expires-date",expiresDate,2));
                    }
                    var enabled=treeData["enabled"];
                    if(enabled!=null){
                        self.addProperty(new urule.RuleProperty(self,"enabled",enabled,3));
                    }
                    var debug=treeData["debug"];
                    if(debug!=null){
                        self.addProperty(new urule.RuleProperty(self,"debug",debug,3));
                    }

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
    }
};

window._setDirty=function(){
    if(window._dirty){
        return;
    }
    window._dirty=true;
    $("#saveButton").html("<i class='rf rf-save'></i> *保存");
    $("#saveButton").removeClass("disabled");
    $("#saveButtonNewVersion").html("<i class='rf rf-savenewversion'></i> *保存新版本");
    $("#saveButtonNewVersion").removeClass("disabled");
};

function cancelDirty(){
    if(!window._dirty){
        return;
    }
    window._dirty=false;
    $("#saveButton").html("<i class='rf rf-save'></i> 保存");
    $("#saveButton").addClass("disabled");
    $("#saveButtonNewVersion").html("<i class='rf rf-savenewversion'></i> 保存新版本");
    $("#saveButtonNewVersion").addClass("disabled");
};