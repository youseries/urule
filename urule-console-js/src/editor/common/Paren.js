/**
 * @author GJ
 */
urule.Paren=function(rule){
	this.container=$("<span>");
	this.leftParen=$("<span>(</span>");
	this.leftParen.css({
		"color":"#000",
		"fontWeight":"blod",
		"padding-left":"3px",
		"padding-right":"3px"
	});
	this.rightParen=$("<span>)</span>");
	this.rightParen.css({
		"color":"#000",
		"fontWeight":"blod",
		"padding-left":"3px",
		"padding-right":"3px"
	});
	this.parenContainer=$("<span>");
	this.container.append(this.leftParen);
	this.container.append(this.parenContainer);
	this.container.append(this.rightParen);
	this.inputType=new urule.InputType(null,null,null,rule);
	this.parenContainer.append(this.inputType.getContainer());
	this.arithmetic=new urule.ComplexArithmetic(rule);
	this.container.append(this.arithmetic.getContainer());
};
urule.Paren.prototype.initData=function(data){
	var value=data["value"];
	var valueType=value["valueType"];
	this.inputType.setValueType(valueType, value);
	this.arithmetic.initData(data["arithmetic"]);
};

urule.Paren.prototype.getDisplayContainer=function(){
	return this.inputType.getDisplayContainer();
};

urule.Paren.prototype.toXml=function(){
	if(!this.inputType){
		throw "请输入括号内容!";
	}
	var xml="<paren>";
	xml+=this.inputType.toXml();
	if(this.arithmetic){
		xml+=this.arithmetic.toXml();
	}
	xml+="</paren>";
	return xml;
};
urule.Paren.prototype.getContainer=function(){
	return this.container;
};