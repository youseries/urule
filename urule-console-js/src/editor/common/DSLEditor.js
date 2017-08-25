/**
 * @author GJ
 */
var urule={};
var codeMirror=null;
$(document).ready(function() {
	CodeMirror.commands.autocomplete = function(cm) {
        cm.showHint({hint: CodeMirror.hint.urule});
    };
	var codeEditor=document.getElementById("codeEditor");
	codeMirror = CodeMirror.fromTextArea(codeEditor, {
		lineNumbers: true,
	    mode: "rulemixed",
        extraKeys: {"Alt-/": "autocomplete"}
	});
	codeMirror.on("change",function(cm,e){
		var value = cm.getValue();
		if(e.text=="."){
			CodeMirror.commands.autocomplete(codeMirror);
		}
		$(".CodeMirror-code").find("pre").children(".error").removeClass("error");
		$(".CodeMirror-code").find("pre").attr("title", "");
		if(cm.validator){
			clearTimeout(cm.validator);
		}
		if($.trim(value)){
			cm.validator = setTimeout(function() {
				$.ajax({
					url:"urule?action=checkdsl",
					type : "POST",
					data :{
						content : value
					},
					error:function(req,error){
						URule.alert("校验失败！");
					},
					success:function(infos){
						if(infos && infos.length>0){
							for(var i = 0; i<infos.length; i ++) {
								var pre = $(".CodeMirror-code").find("pre").eq(infos[i].line-1);
								pre.children("span").addClass("error");
								pre.attr("title",infos[i].message);
								
							}
						}
					}
				});
				cm.validator = null;
			}, 1000);
		}
	})
	init();
});

function init(){
	var height=$(document).height()-55;
	codeMirror.setSize("100%",height)
	window._dirty=false;
	var file=_getRequestParameter("file");
	if(!file || file.length<1){
		URule.alert("当前编辑器未指定具体规则文件！");
		return;
	}
	var saveButton = '<div class="btn-group navbar-btn" style="margin-right:10px;" role="group" aria-label="...">'+
						'<button id="saveButton" type="button" class="btn btn-default navbar-btn" ><i class="icon-save"></i> 保存</button>' + 
						'<button id="saveButtonNewVersion" type="button" class="btn btn-default navbar-btn" ><i class="icon-save"></i> 保存新版本</button>' +
					'</div>';
	if(!hasPermission()) {
		saveButton = '';
	}else {
		$(window).keydown(function(event) {
			if(event.ctrlKey) {
				if(event.keyCode == 83) {
					save(file, false);
				}
			} else if (event.altKey) {
				if(event.keyCode == 83) {
					save(file, true);
				}
			}
		});
	}
	var toolbarHtml='<nav class="navbar navbar-default">'+
		'<div>'+
	        '<div class="collapse navbar-collapse">'+ saveButton +
	            '<button id="checkDSL" type="button" class="btn btn-default navbar-btn"><i class="icon-ok-sign"></i> 语法检查</button>'+
	            '<div class="btn-group navbar-btn" style="margin-left:10px;" role="group" aria-label="...">'+
	                '<button id="addVarButton" type="button" class="btn btn-default"><i class="icon-tasks"></i> 导入变量库</button>'+
	                '<button id="addConstantsButton" type="button" class="btn btn-default"><i class="icon-th-list"></i> 导入常量库</button>'+
	                '<button id="addActionButton" type="button" class="btn btn-default"><i class="icon-bolt"></i> 导入动作库</button>'+
	                '<button id="configParameterButton" type="button" class="btn btn-default"><i class="icon-th"></i> 导入参数库</button>'+	
	            '</div>'+
	       ' </div>'+
	    '</div>'+
	'</nav>';
	var toolbar=$(toolbarHtml);
	toolbar.css({
		padding:"4px",
		diaplay:"inline-block"
	});
	$("#toolbarContainer").append(toolbar);
	$("#saveButton").click(function(){
		save(file,false);
	});
	$("#saveButtonNewVersion").click(function(){
		save(file,true);
	});
	$("#checkDSL").click(function(){
		checkDSL();
	});
	
	$("#configParameterButton").click(function(){
		var dialog=new urule.ResourceListDialog("ParameterLibrary",null,selectResource);
		dialog.open();
	});
	$("#addVarButton").click(function(){
		var dialog=new urule.ResourceListDialog("VariableLibrary",null,selectResource);
		dialog.open();
	});
	$("#addConstantsButton").click(function(){
		var dialog=new urule.ResourceListDialog("ConstantLibrary",null,selectResource);
		dialog.open();
	});
	$("#addActionButton").click(function(){
		var dialog=new urule.ResourceListDialog("ActionLibrary",null,selectResource);
		dialog.open();
	});
	
	window._dirty=false;
	var url="urule?action=loaddsl&file="+file+"";
	$.ajax({
		cache:false,
		url:url,
		type:"GET",
		error:function(req,error){
			URule.alert("文件加载失败！");
		},
		success:function(data){
			codeMirror.setValue(data);
			$("#saveButton").addClass("disabled");
			$("#saveButtonNewVersion").addClass("disabled");

			codeMirror.on("change",function(){
				setDirty();
			});
			loadResLib();
		}
	});
};

function selectResource(type,res){
	codeMirror.replaceSelection("import"+type+" \""+res+"\";");
	loadResLib();
};

function checkDSL(doSuccess){
	var content=codeMirror.getValue();
	if(!content || content.length<10){
		URule.alert("请正确输入规则内容！");
		return;
	}
	var url="urule?action=checkdsl";
	$.ajax({
		cache:false,
		url:url,
		type:"POST",
		data:{content:content},
		error:function(req,error){
			URule.alert("语法检查失败！");

		},
		success:function(data){
			if(!data || data.length <= 0){
				if(doSuccess){
					doSuccess();
				}else{
					URule.alert("语法正确！");

				}
				return;
			}
			URule.alert("语法不正确！");
		}
	});
};
function loadResLib(){
	var file=_getRequestParameter("file");
	var content=codeMirror.getValue();
	if(!content || content.length<10){
		URule.alert("请正确输入规则内容！");
		return;
	}
	var url="urule?action=loaddslreslib";
	$.ajax({
		cache:false,
		url:url,
		type:"POST",
		data:{content:content},
		error:function(req,error){
			//alert("资源库加载失败!");
		},
		success:function(data){
			codeMirror._library=data;
		}
	});
};
function save(file,newVersion){
	if($("#saveButton").hasClass("disabled")){
		return false;
	}
	var url="urule?action=savedsl&file="+file+"";
	var content=codeMirror.getValue();
	$.ajax({
		cache:false,
		url:url,
		type:"POST",
		data:{content:content, newVersion:newVersion},
		error:function(req,error){
			URule.alert("保存失败！");
		},
		success:function(data){
			cancelDirty();
		}
	});
};

function setDirty(){
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