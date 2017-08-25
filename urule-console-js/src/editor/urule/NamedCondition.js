/**
 * @author GJ
 */
urule.NamedCondition=function(context,parentContainer,parentJoin){
	this.context=context;
	this.parentJoin=parentJoin;
	this.variable=null;
	this.container=$("<span>");
	parentContainer.append(this.container);
	this.arithmetic=new urule.SimpleArithmetic();
	
	this.label=generateContainer();
	this.container.append(this.label);
	this.label.css({
		"color":"#673AB7"
	});
	URule.setDomContent(this.label,"请选择属性");
	this.valueContainer=$("<span>");
	this.container.append(this.valueContainer);
	this.initMenu();
	
};
urule.NamedCondition.prototype.initMenu=function(){
	var self=this,menuItems=[];
	var variables=[];
	if(this.parentJoin.variableCategory){
		variables=this.parentJoin.variableCategory.variables || [];
	}
	for(let variable of variables){
		menuItems.push({
			label:variable.label,
			variable:variable,
			onClick:function (item) {
				self.variableName=variable.name;
				self.variableLabel=variable.label;
				self.datatype=variable.type;
				URule.setDomContent(self.label,item.label);
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
				window._setDirty();
			}
		});
	}
	this.menu=new URule.menu.Menu({menuItems});
	this.label.click(function(e){
		self.menu.show(e);
	});
};
urule.NamedCondition.prototype.initData=function(data){
	this.variableName=data["variableName"];
	this.variableLabel=data["variableLabel"];
	this.datatype=data["datatype"];
	URule.setDomContent(this.label,this.variableLabel);
	var self=this;
	if(this.operator){
		this.operator.getContainer().show();
	}else{
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
urule.NamedCondition.prototype.toXml=function(){
	if(!this.variableName){
		throw "请定义条件.";
	}
	var xml="<named-criteria op=\""+this.operator.getOperator()+"\" var=\""+this.variableName+"\" var-label=\""+this.variableLabel+"\" datatype=\""+this.datatype+"\">";
	if(this.inputType){
		xml+=this.inputType.toXml();
	}
	xml+="</named-criteria>";
	return xml;
};
urule.NamedCondition.prototype.getVariableValue=function(){
	return this.variableValue;
};
urule.NamedCondition.prototype.getOperator=function(){
	return this.operator;
};
urule.NamedCondition.prototype.getInputType=function(){
	return this.inputType;
};