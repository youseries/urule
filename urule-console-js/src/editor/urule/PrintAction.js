/**
 * @author GJ
 */
urule.PrintAction=function(rule){
	this.container=$("<span>");
	this.beforeContainer=$("<span>");
	URule.setDomContent(this.beforeContainer,"");
	this.container.append(this.beforeContainer);
	this.inputType=new urule.InputType(null,null,null,rule);
	this.inputTypeContainer=this.inputType.getContainer();
	this.container.append(this.inputTypeContainer);
	this.afterContainer=$("<span>");
	URule.setDomContent(this.afterContainer,"");
	this.container.append(this.afterContainer);
};
urule.PrintAction.prototype.initData=function(data){
	if(!data){
		return;
	}
	var value=data["value"];
	if(!value){
		return;
	}
	this.inputType.setValueType(value["valueType"], value);
};
urule.PrintAction.prototype.toXml=function(){
	var xml="<console-print>";
	xml+=this.inputType.toXml();
	xml+="</console-print>";
	return xml;
};
urule.PrintAction.prototype.getContainer=function(){
	return this.container;
};