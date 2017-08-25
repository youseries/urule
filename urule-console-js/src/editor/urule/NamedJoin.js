/**
 * @author GJ
 */
import {MsgBox} from 'flowdesigner';

urule.NamedJoin=function(context){
	this.type="and";
	this.context=context;
	window._VariableValueArray.push(this);
	this.H=30;
	this.children=[];
	this.container=$("<span class='btn btn-default dropdown-toggle rule-join-container' style='border:solid 1px #2196f3'>");
	this.referenceName=null,this.variableCategory=null;
	this.namedLabel=generateContainer();
	this.namedLabel.css({
		color:"#9C27B0",
		cursor:"pointer",
		fontSize:"12px"
	});
	URule.setDomContent(this.namedLabel,"请输入引用名");
	this.container.append(this.namedLabel);
	var namedEditor=$("<input type='text' class='form-control' style='width:100px;height:24px'>");
	this.container.append(namedEditor);
	namedEditor.hide();
	var self=this;
	this.namedLabel.click(function () {
		self.namedLabel.hide();
		namedEditor.css("display","inline");
		namedEditor.val(self.referenceName);
		namedEditor.focus();
		self.resetItemPosition(0,null);
	});
	namedEditor.blur(function () {
		if(self.referenceName && self.referenceName.length>0){
			self.context.deleteFromNamedMap(self.referenceName);
		}
		var value=$(this).val();
		if(value && value!==''){
			self.referenceName=value;
			URule.setDomContent(self.namedLabel,value+":");
			if(self.variableCategory){
				self.context.putToNamedMap(self.referenceName,self.variableCategory);
			}
		}else{
			self.referenceName=null;
			URule.setDomContent(self.namedLabel,"请输入引用名");
		}
		for(let refValue of self.context.rule.namedReferenceValues){
			if(refValue){
				refValue.initMenu();
			}
		}
		self.namedLabel.show();
		namedEditor.hide();
		self.resetItemPosition(0,null);
	});

	this.variableCategoryLabel=generateContainer();
	this.variableCategoryLabel.css({
		color:"#03A9F4",
		cursor:"pointer",
		fontSize:"12px"
	});
	URule.setDomContent(this.variableCategoryLabel,"请选择变量对象");
	this.container.append(this.variableCategoryLabel);
	this.initMenu();

	this.joinContainer=$("<span>");
	this.container.append(this.joinContainer);
	this.joinLabel=$("<span style='font-size: 11pt;color:#FF9800'>并且</span>");
	this.joinContainer.append(this.joinLabel);
};

urule.NamedJoin.prototype.initMenu=function(variableLibraries){
	var data=window._uruleEditorVariableLibraries;
	if(variableLibraries){
		data=variableLibraries;
	}
	if(!data){
		return;
	}
	var self=this;
	var config={menuItems:[]};
	for(let categories of data){
		for(let category of categories){
			var menuItem={
				label:category.name,
				category:category,
				onClick:function (item) {
					if(self.children.length>0){
						bootbox.confirm("当前节点下已配置了条件，此操作将会清这些条件，你确定吗？",function (result) {
							if(result){
								self.variableCategory=item.category;
								self.variableCategoryName=item.category.name;
								self.context.putToNamedMap(self.referenceName,self.variableCategory);
								URule.setDomContent(self.variableCategoryLabel,item.label);
								self.resetItemPosition(0,null);
								for(let child of self.children){
									child.remove();
								}
								self.children=[];
							}
						});
					}else{
						self.variableCategory=item.category;
						self.variableCategoryName=item.category.name;
						self.context.putToNamedMap(self.referenceName,self.variableCategory);
						URule.setDomContent(self.variableCategoryLabel,item.label);
						self.resetItemPosition(0,null);
					}
					for(let refValue of self.context.rule.namedReferenceValues){
						if(refValue){
							refValue.initMenu();
						}
					}
				}
			};
			config.menuItems.push(menuItem);
		}
	}
	if(self.categoryMenu){
		self.categoryMenu.setConfig(config);
	}else{
		self.categoryMenu=new URule.menu.Menu(config);
	}
	this.variableCategoryLabel.click(function(e){
		if(!self.referenceName){
			bootbox.alert("请先输入引用名称.");
			return;
		}
		self.categoryMenu.show(e);
	});
	if(this.variableCategoryName){
		for(let categories of data){
			for(let category of categories){
				if(category.name===this.variableCategoryName){
					this.variableCategory=category;
					break;
				}
			}
			if(this.variableCategory){
				break;
			}
		}
		if(this.variableCategory){
			this.context.putToNamedMap(this.referenceName,this.variableCategory);
			for(let conn of this.children){
				conn.getCondition().initMenu();
			}
		}
	}
	for(let refValue of self.context.rule.namedReferenceValues){
		if(refValue){
			refValue.initMenu();
		}
	}
};

urule.NamedJoin.prototype.initData=function(data){
	this.referenceName=data["referenceName"];
	this.variableCategoryName=data["variableCategory"];
	URule.setDomContent(this.namedLabel,this.referenceName+":");
	URule.setDomContent(this.variableCategoryLabel,this.variableCategoryName);
	var items=data["items"];
	this.setType(data["junctionType"]);
	if(!items){
		return;
	}
	for(let item of items){
		var newConnection=this.addItem(false);
		newConnection.getCondition().initData(item);
	}
};
urule.NamedJoin.prototype.setType=function(type){
	this.type=type;
	if(type==="or"){
		URule.setDomContent(this.joinLabel,"或者");
	}else{
		URule.setDomContent(this.joinLabel,"并且");
	}
	window._setDirty();
};
urule.NamedJoin.prototype.init=function(parentConnection){
	if(parentConnection){
		this.parentConnection=parentConnection;
		this.parent=parentConnection.getParentJoin();		
	}
	var joinArrow=$(`<i class="glyphicon glyphicon-chevron-down rule-join-node"></i>`);
	var self=this;
	var onClick=function(menu){
		self.setOperator(menu.name);
	};
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
		},{
			label:"删除",
			onClick:function(){
				if(self.children.length>0){
					MsgBox.alert("请先删除当前连接下子元素！");
					return;
				}
				if(parentConnection){
					var parentJoin=parentConnection.getParentJoin();
					if(parentJoin){
						parentJoin.removeConnection(parentConnection);
					}					
				}
				if(self.referenceName){
					self.context.deleteFromNamedMap(self.referenceName);
				}
				for(let refValue of self.context.rule.namedReferenceValues){
					if(refValue){
						refValue.initMenu();
					}
				}
			}
		}]
	});
	this.joinContainer.click(function(e){
		self.menu.show(e);
	});
	this.joinContainer.append(joinArrow);
};
urule.NamedJoin.prototype.removeConnection=function(connection){
	var pos=this.children.indexOf(connection);
	if(this.children.length>1){
		this.resetItemPosition(pos+1, false);
	}
	connection.remove();
	this.children.splice(pos, 1);
	this.resetContainerSize();
	window._setDirty();
};
urule.NamedJoin.prototype.addItem=function(isJoin){
	if(!this.variableCategoryName || !this.referenceName){
		bootbox.alert("请先定义变量引用名及变量对象!");
		return;
	}
	window._setDirty();
	var childrenCount=this.getChildrenCount();
	if(childrenCount>0 && this.parent){
		var parentChildren=this.parent.getChildren();
		var pos=parentChildren.indexOf(this.parentConnection);
		this.parent.resetItemPosition(pos+1,true);
	}
	var totalHeight=childrenCount*this.H;
	var parentLeft=parseInt(this.container.css("left"));
	var parentTop=parseInt(this.container.css("top"));
	var startX=parentLeft+this.container.width()-15;
	var startY=parentTop+this.H/5;
	var endX=startX+40;
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
urule.NamedJoin.prototype.toXml=function(){
	if(!this.referenceName || !this.variableCategoryName){
		throw "请定义引用条件信息.";
	}
	var xml="<named-atom junction-type=\""+this.type+"\" reference-name=\""+this.referenceName+"\" var-category=\""+this.variableCategoryName+"\">";
	for(var i=0;i<this.children.length;i++){
		var conn=this.children[i];
		xml+=conn.toXml();
	}
	xml+="</named-atom>";
	return xml;
};
urule.NamedJoin.prototype.resetItemPosition=function(index,add){
	if(index==-1){
		return;
	}
	for(var i=index;i<this.children.length;i++){
		var connection=this.children[i];
		if(add===null){
			var parentLeft=parseInt(this.container.css("left"));
			var startX=parentLeft+this.container.width()-15;
			var endX=startX+40;
			connection.setStartX(startX);
			connection.setEndX(endX);
		}else{
			var offset=this.H;
			if(!add){
				offset=-this.H;
			}
			connection.setEndY(connection.getEndY()+offset);
			if(index==0){
				connection.setStartY(connection.getStartY()+offset);
			}
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
urule.NamedJoin.prototype.resetContainerSize=function(){
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
urule.NamedJoin.prototype.getChildrenCount=function(){
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
urule.NamedJoin.prototype.initTopJoin=function(container){
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
urule.NamedJoin.prototype.getChildren=function(){
	return this.children;
};
urule.NamedJoin.prototype.getContainer=function(){
	return this.container;
};