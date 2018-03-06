/**
 * @author GJ
 */
urule.AssignmentAction=function(rule){
	this.container=$("<span>");
	this.label=generateContainer();
	this.container.append(this.label);
	URule.setDomContent(this.label,"请选择值类型");
	this.label.css({
		"color":"blue"
	});
	this.valueContainer=$("<span>");
	this.container.append(this.valueContainer);
	this.leftVariable=new urule.VariableValue(null,null,"Out");
	this.leftParameter=new urule.ParameterValue(null,null,"Out");
	this.leftReference=new urule.NamedReferenceValue(null,null,rule);
	this.valueContainer.append(this.leftVariable.getContainer());
	this.valueContainer.append(this.leftParameter.getContainer());
	this.valueContainer.append(this.leftReference.getContainer());

	this.leftVariable.getContainer().hide();
	this.leftParameter.getContainer().hide();
	this.leftReference.getContainer().hide();

	var equals=$("<span>");
	URule.setDomContent(equals,"=");
	equals.css({
		color:"red",
		"fontWeight":"blod"
	});
	this.container.append(equals);
	this.inputType=new urule.InputType(null,null,null,rule);
	this.container.append(this.inputType.getContainer());
	var self=this;
	self.menu=new URule.menu.Menu({
		menuItems:[{
			label:"选择变量",
			onClick:function(){
				self.leftParameter.getContainer().hide();
				self.leftReference.getContainer().hide();
				self.leftVariable.getContainer().show();
				self.type="variable";
				URule.setDomContent(self.label,".");
				self.label.css({
					"color":"white"
				});
			}
		},{
			label:"选择参数",
			onClick:function(){
				self.leftVariable.getContainer().hide();
				self.leftReference.getContainer().hide();
				self.leftParameter.getContainer().show();
				self.type="parameter";
				URule.setDomContent(self.label,".");
				self.label.css({
					"color":"white"
				});
			}
		}/*,{
			label:"选择命名变量",
			onClick:function(){
				self.leftVariable.getContainer().hide();
				self.leftParameter.getContainer().hide();
				self.leftReference.getContainer().show();
				self.type="NamedReference";
				URule.setDomContent(self.label,".");
				self.label.css({
					"color":"white"
				});
			}
		}*/]
	});
	this.label.click(function(e){
		self.menu.show(e);
	});
};
urule.AssignmentAction.prototype.initData=function(data){
	if(!data){
		return;
	}
	this.type=data["type"];
	if(this.type && this.type=="parameter"){
		this.leftParameter.setValue(data);		
		this.leftVariable.getContainer().hide();
		this.leftReference.getContainer().hide();
		this.leftParameter.getContainer().show();
	}else if(this.type && this.type=="NamedReference"){
        this.leftReference.initData(data);
        this.leftVariable.getContainer().hide();
        this.leftParameter.getContainer().hide();
        this.leftReference.getContainer().show();
    }else{
		this.type="variable";
		this.leftVariable.setValue(data);		
		this.leftParameter.getContainer().hide();
        this.leftReference.getContainer().hide();
        this.leftVariable.getContainer().show();
    }
	var value=data["value"];
	if(value){
		var valueType=value["valueType"];
		this.inputType.setValueType(valueType, value);
	}
	URule.setDomContent(this.label,".");
	this.label.css({
		"color":"white"
	});
};
urule.AssignmentAction.prototype.toXml=function(){
	var xml="<var-assign ";
	if(this.type=="variable"){
		xml+=this.leftVariable.toXml();		
	}else if(this.type=="NamedReference"){
        xml+=this.leftReference.toXml();
    }else{
		xml+=this.leftParameter.toXml();				
	}
	xml+=" type=\""+this.type+"\">";
	xml+=this.inputType.toXml();
	xml+="</var-assign>";
	return xml;
};
urule.AssignmentAction.prototype.getContainer=function(){
	return this.container;
};