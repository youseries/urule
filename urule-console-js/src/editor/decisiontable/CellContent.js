/**
 * @author GJ
 */
urule.CellContent=function(element){
	this.container=$(element);
	this.container.css({
		height:"40px",
		width:"100%"
	});
	this.inputType=new urule.InputType(null,"无");
	this.container.append(this.inputType.getContainer());
};
urule.CellContent.prototype.clean=function(data){
	if(this.inputType){
		this.inputType.getContainer().remove();
	}
	this.inputType=new urule.InputType(null,"无");
	this.container.append(this.inputType.getContainer());
	window._setDirty();
};
urule.CellContent.prototype.initData=function(data){
	this.inputType.setValueType(data["valueType"],data);
};
urule.CellContent.prototype.toXml=function(){
	if(this.inputType.type==""){
		return "";
	}
	return this.inputType.toXml();
};
