/**
 * @author GJ
 */
urule.Join=function(context){
	this.type="and";
	this.context=context;
	this.H=30;
	this.W=60;
	this.children=[];
	this.joinContainer=$("<span class='btn btn-default dropdown-toggle'></span>");
	this.joinContainer.css({
		border:"solid gray 1px",
		padding:"3px",
		background:"#fff"
	});
	this.joinLabel=$("<span>并且</span>");
	this.joinContainer.append(this.joinLabel);
};
urule.Join.prototype.initData=function(data){
	var conditions=[];
	var criterions=data["conditions"];
	var joints=data["joints"];
	this.setType(data["type"]);
	if(criterions){
		conditions=criterions;
	}
	if(joints){
		conditions=conditions.concat(joints);
	}
	if(conditions.length==0){
		return;
	}
	for(var i=0;i<conditions.length;i++){
		var criterion=conditions[i];
		var junctionType=criterion["type"];
		var isJoin=false;
		if(junctionType){
			isJoin=true;
		}
		var newConnection=this.addItem(isJoin);
		if(isJoin){
			newConnection.getJoin().initData(criterion);			
		}else{
			newConnection.getCondition().initData(criterion);						
		}
	}
};
urule.Join.prototype.setType=function(type){
	this.type=type;
	if(type=="or"){
		URule.setDomContent(this.joinLabel,"或者");
	}else{
		URule.setDomContent(this.joinLabel,"并且");
	}
	window._setDirty();
};
urule.Join.prototype.init=function(parentConnection){
	if(parentConnection){
		this.parentConnection=parentConnection;
		this.parent=parentConnection.getParentJoin();		
	}
	var joinArrow=$(`<i class="glyphicon glyphicon-chevron-down rule-join-node"></i>`);
	var self=this;
	self.menu=new URule.menu.Menu({
		menuItems:[{
			label:"并且",
			onClick:function(){
				self.setType("and");
			}
		},{
			label:"或者",
			onClick:function(){
				self.setType("or");
			}
		},{
			label:"添加条件",
			onClick:function(){
				self.addItem(false);
			}
		}]
	});
	this.joinContainer.click(function(e){
		self.menu.show(e);
	});
	this.joinContainer.append(joinArrow);
};
urule.Join.prototype.clean=function(){
	while(this.children.length>0){
		var connection=this.children[0];
		this.removeConnection(connection);
	}
};

urule.Join.prototype.removeConnection=function(connection){
	var pos=this.children.indexOf(connection);
	if(this.children.length>1){
		this.resetItemPosition(pos+1, false);
	}
	connection.remove();
	this.children.splice(pos, 1);
	this.resetContainerSize();
	window._setDirty();
};
urule.Join.prototype.addItem=function(isJoin){
	window._setDirty();
	var childrenCount=this.getChildrenCount();
	if(childrenCount>0 && this.parent){
		var parentChildren=this.parent.getChildren();
		var pos=parentChildren.indexOf(this.parentConnection);
		this.parent.resetItemPosition(pos+1,true);
	}
	var totalHeight=childrenCount*this.H;
	var parentLeft=parseInt(this.joinContainer.css("left"));
	var parentTop=parseInt(this.joinContainer.css("top"));
	var startX=parentLeft+this.W/2;
	var startY=parentTop+this.H/5;
	var endX=startX+this.W-25;
	var endY=startY+totalHeight;
	if(isJoin){
		endY-=5;
	}
	var connection=new urule.Connection(this.context,isJoin,this);
	connection.drawPath(startX,startY,endX,endY);
	this.children.push(connection);
	this.resetContainerSize();
	return connection;
};
urule.Join.prototype.toXml=function(){
	var xml="<joint type=\""+this.type+"\">";
	for(var i=0;i<this.children.length;i++){
		var conn=this.children[i];
		xml+=conn.toXml();
	}
	xml+="</joint>";
	return xml;
};
urule.Join.prototype.resetItemPosition=function(index,add){
	if(index==-1){
		return;
	}
	for(var i=index;i<this.children.length;i++){
		var connection=this.children[i];
		var offset=this.H;
		if(!add){
			offset=-this.H;
		}
		connection.setEndY(connection.getEndY()+offset);
		if(index==0){
			connection.setStartY(connection.getStartY()+offset);
		}
		connection.update(add);
	}
	if(index>0 && this.parent){
		var parentChildren=this.parent.getChildren();
		var pos=parentChildren.indexOf(this.parentConnection);
		var parentJoin=this.parentConnection.getParentJoin();
		parentJoin.resetItemPosition(pos+1,add);
	}
	window._setDirty();
};
urule.Join.prototype.resetContainerSize=function(){
	var container=this.context.getCanvas();
	var height=container.css("height");
	height=parseInt(height);
	var childrenCount=this.context.getTotalChildrenCount();
	if(childrenCount==0)childrenCount=1;
	var totalHeight=childrenCount*this.H+10;
	container.css({
		"height":totalHeight+"px"
	});
};
urule.Join.prototype.getChildrenCount=function(){
	var total=0;
	for(var i=0;i<this.children.length;i++){
		var child=this.children[i].getJoin();
		if(child){
			var count=child.getChildrenCount();
			if(count==0){
				count=1;
			}
			total+=count;
		}else{
			total++;
		}
	}
	return total;
};
urule.Join.prototype.initTopJoin=function(container){
	var left=5;
	var top=5;
	this.joinContainer.css({
		"position":"absolute",
		"left":left,
		"top":top
	});
	container.append(this.joinContainer);
	this.context.setRootJoin(this);
};
urule.Join.prototype.getDisplayContainer=function(){
	if(this.children.length==0){
		return null;
	}
	var container=$("<span>");
	for(var i=0;i<this.children.length;i++){
		var child=this.children[i];
		var childDisplayContainer=child.getDisplayContainer();
		if(!childDisplayContainer){
			continue;
		}
		if(i>0){
			if(this.type=="or"){
				container.append($("<span style='color:green'>&nbsp或&nbsp</span>"));
			}else{
				container.append($("<span style='color:red'>&nbsp并且&nbsp</span>"));
			}
		}
		container.append(childDisplayContainer);
	}
	return container;
};

urule.Join.prototype.getChildren=function(){
	return this.children;
};
urule.Join.prototype.getContainer=function(){
	return this.joinContainer;
};