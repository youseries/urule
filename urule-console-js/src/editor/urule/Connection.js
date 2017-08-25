/**
 * @author GJ
 */
urule.Connection=function(context,isJoin,parentJoin){
	this.isJoin=isJoin;
	this.context=context;
	this.parentJoin=parentJoin;
};
urule.Connection.prototype.drawPath=function(startX,startY,endX,endY){
	this.startX=startX;
	this.endX=endX;
	if(this.isJoin){
		this.startY=startY-3;
		this.endY=endY+2;
	}else{
		this.startY=startY-3;
		this.endY=endY-3;
	}
	this.path=this.context.getPaper().path(this.buildPathInfo());
	this.path.attr({'stroke':'#777'});
	if(this.isJoin){
		this.initJoin(this.isJoin);
	}else{
		this.initCondition();
	}
};
urule.Connection.prototype.toXml=function(){
	var xml="";
	if(this.isJoin){
		xml=this.join.toXml();
	}else{
		xml=this.condition.toXml();
	}
	return xml;
};
urule.Connection.prototype.initJoin=function(joinType){
	if(joinType==='named'){
		this.join=new urule.NamedJoin(this.context);
	}else{
		this.join=new urule.Join(this.context);
	}
	this.join.init(this);
	var joinContainer=this.join.getContainer();
	var left=(this.endX+10)+"px";
	var top=this.endY+"px";
	joinContainer.css({
		"position":"absolute",
		"left":left,
		"top":top
	});
	this.context.getCanvas().append(joinContainer);
};

urule.Connection.prototype.remove=function(){
	this.path.remove();
	if(this.join){
		this.join.getContainer().remove();
	}else{
		this.conditionContainer.remove();
	}
	window._setDirty();
};

urule.Connection.prototype.initCondition=function(joinType){
	this.conditionContainer=$("<div>");
	var left=(this.endX+10)+"px";
	var top=this.endY+"px";
	this.conditionContainer.css({
		"position":"absolute",
		"left":left,
		"top":top
	});
	if(this.parentJoin instanceof urule.NamedJoin){
		this.condition=new urule.NamedCondition(this.context,this.conditionContainer,this.parentJoin);
	}else{
		this.condition=new urule.Condition(this.conditionContainer);
	}
	var del=$(`<i class="glyphicon glyphicon-trash" style="color: #019dff;cursor: pointer;font-size: 9pt;padding-left:5px"></i>`);
	var self=this;
	del.click(function(){
		self.parentJoin.removeConnection(self);
	});
	this.conditionContainer.append(del);
	this.context.getCanvas().append(this.conditionContainer);
};
urule.Connection.prototype.update=function(add){
	var pathInfo=this.buildPathInfo();
	this.path.attr("path",pathInfo);
	if(add===null){
		var left=(this.endX+10)+"px";
		if(this.conditionContainer){
			this.conditionContainer.css({
				"left":left
			});
		}else{
			this.join.getContainer().css({
				"left":left
			});
		}
	}else{
		var top=this.endY+"px";
		if(this.conditionContainer){
			this.conditionContainer.css({
				"top":top
			});
		}else{
			this.join.getContainer().css({
				"top":top
			});
		}
	}
	if(this.join){
		this.join.resetItemPosition(0,add);
	}
};
urule.Connection.prototype.getParentJoin=function(){
	return this.parentJoin;
};
urule.Connection.prototype.getCondition=function(){
	return this.condition;
};
urule.Connection.prototype.getJoin=function(){
	return this.join;
};
urule.Connection.prototype.getStartX=function(){
	return this.startX;
};
urule.Connection.prototype.getStartY=function(){
	return this.startY;
};
urule.Connection.prototype.getEndX=function(){
	return this.endX;
};
urule.Connection.prototype.getEndY=function(){
	return this.endY;
};
urule.Connection.prototype.setStartX=function(startX){
	this.startX=startX;
};
urule.Connection.prototype.setStartY=function(startY){
	this.startY=startY;
};
urule.Connection.prototype.setEndX=function(endX){
	this.endX=endX;
};
urule.Connection.prototype.setEndY=function(endY){
	this.endY=endY;
};
urule.Connection.prototype.buildPathInfo=function(){
	var left=10;
	 var top=8;
	return "M" + (this.startX+left) + "," + (this.startY+top) + " C" + (this.startX+left) + "," + (this.endY+top) + "," + (this.startX+left) + "," + (this.endY+top) + "," + (this.endX+left) + "," + (this.endY+top);
};