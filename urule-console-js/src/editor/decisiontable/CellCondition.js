/**
 * @author GJ
 */
window._conditionId=0;
urule.CellCondition=function(element){
	this.container=$(element);
	this.container.css({
		height:"40px",
		position:"relative"
	});
	var context=new urule.Context(this.container);
	this.join=new urule.Join(context);
	this.join.init(null);
	this.join.initTopJoin(this.container);
	this.join.setType("and");
	this.id=window._conditionId++;
};
urule.CellCondition.prototype.clean=function(){
	this.join.clean();
	window._setDirty();
};
urule.CellCondition.prototype.getId=function(){
	return this.id;
};
urule.CellCondition.prototype.renderTo=function(container){
	container.append(this.container);
};
urule.CellCondition.prototype.getDisplayContainer=function(){
	var dis=null;
	if(this.join){
	    dis= this.join.getDisplayContainer();
	}
	if(!dis){
		dis= $("<span style='color:gray'>无</span>");
	}
	return dis;
};
urule.CellCondition.prototype.initData=function(data){
	if(this.join){
		this.join.initData(data);
	}
};
urule.CellCondition.prototype.toXml=function(){
	return this.join.toXml();
};