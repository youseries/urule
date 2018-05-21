/**
 * @author GJ
 */
import {getParameter,ajaxSave} from '../../Utils.js';
import {MsgBox} from 'flowdesigner';

(function($){
	$.fn.urule=function(){
		this._dirty=false;
		this.rules=[];
		var file=getParameter("file");
		var version=getParameter("version")||"";
		if(!file || file.length<1){
			MsgBox.alert("当前编辑器未指定具体规则文件！");
			return;
		}
		var saveButton = '<div class="btn-group btn-group-sm navbar-btn" style="margin-top:0px;margin-bottom: 0px" role="group" aria-label="...">'+
							'<button id="saveButton" type="button" class="btn btn-default navbar-btn" ><i class="icon-save"></i> 保存</button>' + 
							'<button id="saveButtonNewVersion" type="button" class="btn btn-default navbar-btn" ><i class="icon-save"></i> 保存新版本</button>' +
						'</div>';
		var toolbarHtml=`<nav class="navbar navbar-default" style="margin: 5px">
        	<div style="margin-left:5px;margin-top:0px;margin-bottom: 0px">
	            <div>
	                <button id="addRuleButton" type="button" class="btn btn-default btn-sm navbar-btn"><i class="glyphicon glyphicon-plus-sign"></i> 添加规则</button>
	                <button id="addLoopRuleButton" type="button" class="btn btn-default btn-sm navbar-btn"><i class="glyphicon glyphicon-plus"></i> 添加循环规则</button>
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
		this.append(toolbar);
		var self=this;
		$("#addRuleButton").click(function(){
			var rule=_addRule();
			rule.initTopJoin();
		});
		$("#addLoopRuleButton").click(function(){
			var rule=_addLoopRule();
			rule.initTopJoin();
		});

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
			save(false);
		});
		$("#saveButtonNewVersion").click(function(){
			save(true);
		});
		$("#saveButton").addClass("disabled");
		$("#saveButtonNewVersion").addClass("disabled");

		var remarkContainer=$("<div style='margin: 5px;padding: 5px;'></div>");
        this.append(remarkContainer);
        this.remark=new Remark(remarkContainer);
		
		_loadRulesetFileData();

		var _this=this;
		this.sortable({
			delay: 200,
			update: function (event, ui) {
				var children=_this.children("div");
				children.each(function(index,div){
					let item=$(div),id=item.prop("id"),rules=_this.rules,targetRule=null;
					for(let rule of rules){
						if(rule.uuid===id){
							targetRule=rule;
							break;
						}
					}
					if(targetRule){
						const pos=rules.indexOf(targetRule);
						rules.splice(pos,1);
						rules.splice(index,0,targetRule);
					}
				});
				window._setDirty();
			}
		});

		function save(newVersion){
			if($("#saveButton").hasClass("disabled")){
				return false;
			}
			var xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			xml+="<rule-set>";
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
            xml+=self.remark.toXml();
			try{
				for(var i=0;i<self.rules.length;i++){
					var rule=self.rules[i];
					xml+=rule.toXml();
				}
			}catch(error){
				MsgBox.alert(error);
				return;
			}
			xml+="</rule-set>";
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
		}
		
		window._setDirty=function(){
			if(self._dirty){
				return;
			}
			self._dirty=true;
			window._dirty=true;
			$("#saveButton").html("<i class='rf rf-save'></i> *保存");
			$("#saveButton").removeClass("disabled");
			$("#saveButtonNewVersion").html("<i class='rf rf-savenewversion'></i> *保存新版本");
			$("#saveButtonNewVersion").removeClass("disabled");
		};

		function cancelDirty(){
			if(!self._dirty){
				return;
			}
			self._dirty=false;
			window._dirty=false;
			$("#saveButton").html("<i class='rf rf-save'></i> 保存");
			$("#saveButton").addClass("disabled");
			$("#saveButtonNewVersion").html("<i class='rf rf-savenewversion'></i> 保存新版本");
			$("#saveButtonNewVersion").addClass("disabled");
		};
		
		function _addRule(data){
			var ruleContainer=$("<div class='well' style='margin:5px;padding:8px;background-color: #fdfdfd'></div>");
			self.append(ruleContainer);
			var rule=new urule.Rule(self,ruleContainer,data);
			self.rules.push(rule);
			window._setDirty();
			return rule;
		};
		function _addLoopRule(data){
			var ruleContainer=$("<div class='well' style='margin:5px;padding:8px;border-color:#337AB7;background-color: #fdfdfd'></div>");
			self.append(ruleContainer);
			var rule=new urule.LoopRule(self,ruleContainer,data);
			self.rules.push(rule);
			window._setDirty();
			return rule;
		};

		function _loadRulesetFileData(){
			var url=window._server+'/common/loadXml';
			$.ajax({
				url:url,
				type:'POST',
				data:{files:file},
				error:function(response){
					if(response && response.responseText){
						bootbox.alert("<span style='color: red'>加载文件失败，服务端错误："+response.responseText+"</span>");
					}else{
						bootbox.alert("<span style='color: red'>加载文件失败,服务端出错</span>");
					}
				},
				success:function(data){
					var ruleset=data[0];
					var libraries=ruleset["libraries"];
                    self.remark.setData(ruleset["remark"]);
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
					var rules=ruleset["rules"];
					for(var i=0;i<rules.length;i++){
						var rule=rules[i];
						if(rule.loopRule){
                            _addLoopRule(rule);
                        }else{
                            _addRule(rule);
                        }
					}
					cancelDirty();
				}
			});
		}
		
	};
})(jQuery);
