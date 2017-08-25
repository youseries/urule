/**
 * @author GJ
 */
urule.Condition=function(parentContainer){
	this.container=$("<span>");
	parentContainer.append(this.container);
	var self=this;
	this.operator=new urule.ComparisonOperator(function(){
		self.inputType=self.operator.getInputType();
		if(self.inputType){
			self.container.append(self.inputType.getContainer());		
		}
	});
	self.container.append(this.operator.getContainer());
};
urule.Condition.prototype.initData=function(data){
	var op=data["op"];
	this.operator.setOperator(op);
	this.operator.initRightValue(data["value"]);
	this.inputType=this.operator.getInputType();
	if(this.inputType){
		this.container.append(this.inputType.getContainer());		
	}
};
urule.Condition.prototype.getDisplayContainer=function(){
	var container=$("<span>");
	var operator=URule.getDomContent(this.operator.getContainer());
	container.append($("<span style='color:blue'>"+operator+"</span>"));
	if(this.inputType){
		container.append(this.inputType.getDisplayContainer());
	}
	return container;
};
urule.Condition.prototype.toXml=function(){
	var xml="<condition op=\""+this.operator.getOperator()+"\">";
	if(this.inputType){
		xml+=this.inputType.toXml();
	}
	xml+="</condition>";
	return xml;
};
urule.Condition.prototype.getOperator=function(){
	return this.operator;
};
urule.Condition.prototype.getInputType=function(){
	return this.inputType;
};