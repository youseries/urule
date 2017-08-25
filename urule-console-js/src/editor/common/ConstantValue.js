/**
 * @author GJ
 */
urule.ConstantValue=function(arithmetic,data){
	this.arithmetic=arithmetic;
	this.container=$("<span>");
	this.label=generateContainer();
	this.container.append(this.label);
	this.label.css({
		"color":"#0174DF",
	});
	this.label.prop("innerText","请选择常量");
	if(arithmetic){
		this.container.append(arithmetic.getContainer());		
	}
	if(data){
		this.setValue(data);
		arithmetic.initData(data["arithmetic"]);
	}
	this.initMenu();
	window._ConstantValueArray.push(this);
};
urule.ConstantValue.prototype.setValue=function(data){
	this.category=data["constantCategory"];
	this.constantName=data["constantName"];
	this.constantLabel=data["constantLabel"];
	URule.setDomContent(this.label,this.category+"."+this.constantLabel);
	window._setDirty();
};

urule.ConstantValue.prototype.initMenu=function(constantLibraries){
	var data=window._uruleEditorConstantLibraries;
	if(constantLibraries){
		data=constantLibraries;
	}
	if(!data){
		return;
	}
	var self,onClick,config;
	self=this;
	onClick=function(menuItem){
		self.setValue({
			constantCategory:menuItem.parent.parent.label,
			constantLabel:menuItem.label,
			constantName:menuItem.name
		});
	};
	config={menuItems:[]};
	$.each(data,function(index,item){
		var categories=item["categories"]; 
		$.each(categories,function(i,category){
			var menuItem={
				label:category.label
			}
			var constants=category["constants"];
			$.each(constants,function(j,constant){
				if(!menuItem.subMenu){
					menuItem.subMenu={menuItems:[]};
				}
				menuItem.subMenu.menuItems.push({
					name:constant.name,
					label:constant.label,
					onClick:onClick
				});
			});
			config.menuItems.push(menuItem);
		});
	});
	if(self.menu){
		self.menu.setConfig(config);
	}else{
		self.menu=new URule.menu.Menu(config);
	}
	this.label.click(function(e){
		self.menu.show(e);
	});
};

urule.ConstantValue.prototype.getDisplayContainer=function(){
	var container=$("<span>"+this.category+"."+this.constantLabel+"</span>");
	if(this.arithmetic){
		var dis=this.arithmetic.getDisplayContainer();
		if(dis){
			container.append(dis);			
		}	
	}
	return container;
};

urule.ConstantValue.prototype.toXml=function(){
	if(!this.category){
		throw "常量不能为空！";
	}
	var xml="const-category=\""+this.category+"\" const=\""+this.constantName+"\" const-label=\""+this.constantLabel+"\"";
	return xml;
};
urule.ConstantValue.prototype.getContainer=function(){
	return this.container;
};