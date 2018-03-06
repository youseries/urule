/**
 * @author GJ
 */
urule.InputType=function(endInfo,tip,functionProperty,rule){
	this.container=$("<span>");
	this.label=generateContainer();
	this.rule=rule;
	this.container.append(this.label);
	if(tip){
		URule.setDomContent(this.label,tip);
		this.label.css({
			"color":"gray"
		});		
	}else{
		URule.setDomContent(this.label,"选择值类型");
		this.label.css({
			"color":"blue"
		});
	}
	this.functionProperty=functionProperty;
	this.variableValue=null;
	this.simpleValue=null;
	this.referenceValue=null;
	this.methodValue=null;
	this.constantValue=null;
	this.functionValue=null;
	this.dataContainer=$("<span>");
	this.container.append(this.dataContainer);
	this.type="";
	var self=this;
	var onClick=function(menuItem){
		self.setValueType(menuItem.name);
	};
	self.menu=new URule.menu.Menu({
		menuItems:[{
			label : "输入值",
			name:"Input",
			onClick : onClick
		}, {
			label : "选择变量",
			name:"Variable",
			onClick : onClick
		}, {
			label : "选择常量",
			name:"Constant",
			onClick : onClick
		}, {
			label : "选择参数",
			name:"Parameter",
			onClick : onClick
		}]
	});
	/*
	if(this.rule){
		self.menu.menuItems.push({
			label:"选择命名变量",
			name:"NamedReference",
			onClick : onClick
		});
	}
	*/
	self.menu.menuItems.push({
		label : "选择方法",
		name  : "Method",
		onClick : onClick
	}, {
		label : "选择函数",
		name  : "CommonFunction",
		onClick : onClick
	});
	this.label.click(function(e){
		self.menu.show(e);
	});

	if(endInfo){
		var endInfoContainer=$("<span style='color:red;font-size:11pt'><strong>"+endInfo+"</strong></span>");
		this.container.append(endInfoContainer);
	}
};

urule.InputType.prototype.getDisplayContainer=function(){
	var container=$("<span>");
	if(this.type=="Input"){
		container.append(this.simpleValue.getDisplayContainer());
	}else if(this.type=="Variable" || this.type=="VariableCategory"){
		container.append(this.variableValue.getDisplayContainer());
	}else if(this.type=="Constant"){
		container.append(this.constantValue.getDisplayContainer());
	}else if(this.type=="Method"){
		container.append(this.methodValue.getDisplayContainer());
	}else if(this.type=="Parameter"){
		container.append(this.parameterValue.getDisplayContainer());
	}else if(this.type=="CommonFunction"){
		container.append(this.functionValue.getDisplayContainer());
	}else if(this.type=="NamedReference"){
		container.append(this.referenceValue.getDisplayContainer());
	}
	return container;
};

urule.InputType.prototype.setValueType=function(valueType,value){
	window._setDirty();
	this.type=valueType;
	if(this.variableValue){
		this.variableValue.getContainer().hide();
	}
	if(this.constantValue){
		this.constantValue.getContainer().hide();
	}
	if(this.simpleValue){
		this.simpleValue.getContainer().hide();
	}
	if(this.referenceValue){
		this.referenceValue.getContainer().hide();
	}
	if(this.methodValue){
		this.methodValue.getContainer().hide();
	}
	if(this.parameterValue){
		this.parameterValue.getContainer().hide();
	}
	if(this.functionValue){
		this.functionValue.getContainer().hide();
	}
	URule.setDomContent(this.label,".");
	this.label.css({
		"color":"#FDFDFD"
	});
	switch(valueType){
	case "Input":
		if(!this.simpleValue){
			this.arithmetic=new urule.ComplexArithmetic(this.rule);
			this.simpleValue=new urule.SimpleValue(this.arithmetic,value);
			this.dataContainer.append(this.simpleValue.getContainer());
		}
		this.simpleValue.getContainer().show();
		this.type="Input";
		break;
	case "NamedReference":
		if(!this.referenceValue){
			this.arithmetic=new urule.ComplexArithmetic(this.rule);
			this.referenceValue=new urule.NamedReferenceValue(this.arithmetic,value,this.rule);
			this.dataContainer.append(this.referenceValue.getContainer());
		}
		this.referenceValue.getContainer().show();
		this.type="NamedReference";
		break;
	case "Constant":
		if(!this.constantValue){
			this.arithmetic=new urule.ComplexArithmetic(this.rule);
			this.constantValue=new urule.ConstantValue(this.arithmetic,value);
			this.dataContainer.append(this.constantValue.getContainer());
		}
		this.constantValue.getContainer().show();
		this.type="Constant";
		break;
	case "Method":
		if(!this.methodValue){
			this.arithmetic=new urule.ComplexArithmetic(this.rule);
			this.methodValue=new urule.MethodValue(this.arithmetic,value,this.dataContainer);
			this.dataContainer.append(this.methodValue.getContainer());
		}
		this.methodValue.getContainer().show();
		this.type="Method";
		break;
	case "Parameter":
		if(!this.parameterValue){
			this.arithmetic=new urule.ComplexArithmetic(this.rule);
			this.parameterValue=new urule.ParameterValue(this.arithmetic,value,"In");
			this.dataContainer.append(this.parameterValue.getContainer());
		}
		this.parameterValue.getContainer().show();
		this.type="Parameter";
		break;
	case "CommonFunction":
		if(!this.functionValue){
			this.arithmetic=new urule.ComplexArithmetic(this.rule);
			this.functionValue=new urule.FunctionValue(this.arithmetic,value,"In");
			this.dataContainer.append(this.functionValue.getContainer());
		}
		this.functionValue.getContainer().show();
		this.type="CommonFunction";
		break;
	default :
		if(!this.variableValue){
			this.arithmetic=new urule.ComplexArithmetic(this.rule);
			this.variableValue=new urule.VariableValue(this.arithmetic,value,"In",this.functionProperty);
			this.dataContainer.append(this.variableValue.getContainer());
		}
		this.variableValue.getContainer().show();
		this.type="Variable";
		break;
	}
};
urule.InputType.prototype.toXml=function(){
	if(this.type==""){
		return "";
	}
	var xml="<value ";
	if(this.type=="Input"){
		var value=this.simpleValue.getValue();
		if(!value || value==""){
			throw "输入值不能为空!";
		}
		xml+=" content=\""+value+"\"";
	}else if(this.type=="NamedReference"){
        xml+=this.referenceValue.toXml();
    }else if(this.type=="Variable" || this.type=="VariableCategory"){
		xml+=this.variableValue.toXml();
		this.type=this.variableValue.getType();
	}else if(this.type=="Method"){
		xml+=this.methodValue.toXml();		
	}else if(this.type=="Parameter"){
		xml+=this.parameterValue.toXml();		
	}else if(this.type=="CommonFunction"){
		xml+=this.functionValue.toXml();		
	}else{
		xml+=this.constantValue.toXml();
	}
	xml+=" type=\""+this.type+"\" ";
	xml+=">";
	xml+=this.arithmetic.toXml();
	if(this.type=="Method"){
		var parameters=this.methodValue.action.parameters;
		for(var i=0;i<parameters.length;i++){
			var p=parameters[i];
			xml+=p.toXml();
		}
	}else if(this.type=="CommonFunction"){
		var parameter=this.functionValue.getParameter();
		xml+=parameter.toXml();
	}
	xml+="</value>";
	return xml;
};
urule.InputType.prototype.getContainer=function(){
	return this.container;
};