/**
 * @author GJ
 */
urule.ConditionLeft=function(parentContainer){
	this.container=$("<span>");
	parentContainer.append(this.container);
	this.arithmetic=new urule.SimpleArithmetic();
	
	this.label=generateContainer();
	this.container.append(this.label);
	this.label.css({
		"color":"blue"
	});
	URule.setDomContent(this.label,"请选择类型");
	this.valueContainer=$("<span>");
	this.container.append(this.valueContainer);
	this.initMenu();
	
};
urule.ConditionLeft.prototype.initMenu=function(constantLibraries){
	var self=this;
	self.menu=new URule.menu.Menu({
		menuItems:[{
			label:"选择变量",
			onClick:function(){
				self.type="variable";
				if(self.parameterValue){
					self.parameterValue.getContainer().hide();
				}
				if(self.functionValue){
					self.functionValue.getContainer().hide();
				}
				if(self.methodValue){
					self.methodValue.getContainer().hide();				
				}
				if(self.variableValue){
					self.variableValue.getContainer().show();
				}else{
					self.variableValue=new urule.VariableValue(self.arithmetic,null,"In");
					self.valueContainer.append(self.variableValue.getContainer());				
				}
				self.label.css({
					"color":"white"
				});
				URule.setDomContent(self.label,".");
				window._setDirty();
			}
		},{
			label:"选择参数",
			onClick:function(){
				self.type="parameter";
				if(self.variableValue){
					self.variableValue.getContainer().hide();
				}
				if(self.methodValue){
					self.methodValue.getContainer().hide();				
				}
				if(self.functionValue){
					self.functionValue.getContainer().hide();
				}
				if(self.parameterValue){
					self.parameterValue.getContainer().show();
				}else{
					self.parameterValue=new urule.ParameterValue(self.arithmetic,null,"In");
					self.valueContainer.append(self.parameterValue.getContainer());				
				}
				self.label.css({
					"color":"white"
				});
				URule.setDomContent(self.label,".");
				window._setDirty();
			}
		},{
			label:"选择方法",
			onClick:function(){
				self.type="method";
				if(self.variableValue){
					self.variableValue.getContainer().hide();
				}
				if(self.parameterValue){
					self.parameterValue.getContainer().hide();
				}
				if(self.functionValue){
					self.functionValue.getContainer().hide();
				}
				if(self.methodValue){
					self.methodValue.getContainer().show();				
				}else{
					self.methodValue=new urule.MethodValue(self.arithmetic,null);
					self.valueContainer.append(self.methodValue.getContainer());				
				}
				self.label.css({
					"color":"white"
				});
				URule.setDomContent(self.label,".");
				window._setDirty();
			}
		},{
			label:"选择函数",
			onClick:function(){
				self.type="commonfunction";
				if(self.variableValue){
					self.variableValue.getContainer().hide();
				}
				if(self.parameterValue){
					self.parameterValue.getContainer().hide();
				}
				if(self.methodValue){
					self.methodValue.getContainer().hide();				
				}
				if(self.functionValue){
					self.functionValue.getContainer().show();
				}else{
					self.functionValue=new urule.FunctionValue(self.arithmetic,null,"In");
					self.valueContainer.append(self.functionValue.getContainer());				
				}
				self.label.css({
					"color":"white"
				});
				URule.setDomContent(self.label,".");
				window._setDirty();
			}
		}]
	});
	this.label.click(function(e){
		self.menu.show(e);
	});
	
};
urule.ConditionLeft.prototype.initData=function(leftData){
	if(!leftData){
		return;
	}
	this.label.css({
		"color":"white",
	});
	URule.setDomContent(this.label,".");
	var leftPart=leftData["leftPart"];
	leftPart.arithmetic=leftData["arithmetic"];
	this.type=leftData["type"];
	if(!this.type){
		this.type="variable";
	}
	if(this.type=="parameter"){
		if(this.variableValue){
			this.variableValue.getContainer().hide();
		}
		if(this.methodValue){
			this.methodValue.getContainer().hide();
		}
		if(this.functionValue){
			this.functionValue.getContainer().hide();
		}
		this.parameterValue=new urule.ParameterValue(this.arithmetic,leftPart,"In");
		this.valueContainer.append(this.parameterValue.getContainer());						
	}else if(this.type=="variable"){
		if(this.parameterValue){
			this.parameterValue.getContainer().hide();
		}
		if(this.methodValue){
			this.methodValue.getContainer().hide();
		}
		if(this.functionValue){
			this.functionValue.getContainer().hide();
		}
		this.variableValue=new urule.VariableValue(this.arithmetic,leftPart,"In");
		this.valueContainer.append(this.variableValue.getContainer());			
	}else if(this.type=="method"){
		if(this.parameterValue){
			this.parameterValue.getContainer().hide();
		}
		if(this.variableValue){
			this.variableValue.getContainer().hide();
		}
		if(this.functionValue){
			this.functionValue.getContainer().hide();
		}
		this.methodValue=new urule.MethodValue(this.arithmetic,leftPart);
		this.valueContainer.append(this.methodValue.getContainer());			
		
	}else if(this.type=="commonfunction"){
		if(this.parameterValue){
			this.parameterValue.getContainer().hide();
		}
		if(this.variableValue){
			this.variableValue.getContainer().hide();
		}
		if(this.methodValue){
			this.methodValue.getContainer().hide();
		}
		this.functionValue=new urule.FunctionValue(this.arithmetic,leftPart);
		this.valueContainer.append(this.functionValue.getContainer());
	}
};
urule.ConditionLeft.prototype.toXml=function(){
	var xml="";
	xml+="<left ";
	if(this.type=="variable"){
		xml+=this.variableValue.toXml();
	}else if(this.type=="parameter"){
		xml+=this.parameterValue.toXml();		
	}else if(this.type=="method"){
		xml+=this.methodValue.toXml();				
	}else if(this.type=="commonfunction"){
		xml+=this.functionValue.toXml();
	}
	xml+=" type=\""+this.type+"\">";
	if(this.type=="method"){
		var parameters=this.methodValue.action.parameters;
		for(var i=0;i<parameters.length;i++){
			var p=parameters[i];
			xml+=p.toXml();
		}
	}else if(this.type=="commonfunction"){
		xml+=this.functionValue.getParameter().toXml();
	}
	xml+=this.arithmetic.toXml();
	xml+="</left>";
	if(this.inputType){
		xml+=this.inputType.toXml();
	}
	return xml;
};
urule.ConditionLeft.prototype.getVariableValue=function(){
	return this.variableValue;
};
urule.ConditionLeft.prototype.getOperator=function(){
	return this.operator;
};
urule.ConditionLeft.prototype.getInputType=function(){
	return this.inputType;
};