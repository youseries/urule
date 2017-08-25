/**
 * @author GJ
 */
urule.MethodParameter=function(rule){
	this.inputType=new urule.InputType(null,null,null,rule);
	this.container=this.inputType.getContainer();
};
urule.MethodParameter.prototype.initData=function(data){
	if(!data){
		return;
	}
	this.name=data["name"];
	this.type=data["type"];
	if(!data["value"]){
		return;
	}
	var value=data["value"];
	if(!value["valueType"]){
		return;
	}
	this.inputType.setValueType(value["valueType"], value);
};

urule.MethodParameter.prototype.toXml=function(){
	var xml="<parameter name=\""+this.name+"\" type=\""+this.type+"\">";
	xml+=this.inputType.toXml();
	xml+="</parameter>";
	return xml;
};
urule.MethodParameter.prototype.getContainer=function(){
	return this.container;
};
urule.MethodParameter.prototype.getInputType=function(){
	return this.inputType;
};