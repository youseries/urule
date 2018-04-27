/**
 * @author GJ
 */
urule.Condition=function(parentContainer){
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
urule.Condition.prototype.initMenu=function(constantLibraries){
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
					self.variableValue=new urule.VariableValue(self.arithmetic,null,"In",null,false);
					self.valueContainer.append(self.variableValue.getContainer());				
				}
				if(self.operator){
					self.operator.getContainer().show();
				}else{
					self.operator=new urule.ComparisonOperator(function(){
						self.inputType=self.operator.getInputType();
						if(self.inputType){
							self.container.append(self.inputType.getContainer());		
						}
					});
					self.container.append(self.operator.getContainer());
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
				if(self.operator){
					self.operator.getContainer().show();
				}else{
					self.operator=new urule.ComparisonOperator(function(){
						self.inputType=self.operator.getInputType();
						if(self.inputType){
							self.container.append(self.inputType.getContainer());		
						}
					});
					self.container.append(self.operator.getContainer());
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
urule.Condition.prototype.initData=function(data){
	this.label.css({
		"color":"white"
	});
	URule.setDomContent(this.label,".");
	var leftData=data["left"];
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
		this.variableValue=new urule.VariableValue(this.arithmetic,leftPart,"In",null,false);
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
	if(this.operator){
		this.operator.getContainer().show();
	}else{
		var self=this;
		this.operator=new urule.ComparisonOperator(function(){
			self.inputType=self.operator.getInputType();
			if(self.inputType){
				self.container.append(self.inputType.getContainer());		
			}
		});
		this.container.append(this.operator.getContainer());
	}
	var op=data["op"];
	this.operator.setOperator(op);
	this.operator.initRightValue(data["value"]);
	this.inputType=this.operator.getInputType();
	if(this.inputType){
		this.container.append(this.inputType.getContainer());		
	}
};
urule.Condition.prototype.toXml=function(){
	var xml="<atom op=\""+this.operator.getOperator()+"\">";
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
	xml+="</atom>";
	return xml;
};
urule.Condition.prototype.getVariableValue=function(){
	return this.variableValue;
};
urule.Condition.prototype.getOperator=function(){
	return this.operator;
};
urule.Condition.prototype.getInputType=function(){
	return this.inputType;
};