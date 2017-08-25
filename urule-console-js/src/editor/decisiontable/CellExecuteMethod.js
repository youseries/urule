/**
 * @author GJ
 */
urule.CellExecuteMethod=function(element){
	this.parentContainer=$(element);
	this.parentContainer.css({
		height:"40px",
		width:"100%"
	});
	this.container=generateContainer();
//	this.container.prop("innerText","无");
	URule.setDomContent(this.container,"无");
	this.container.css({
		"color":"gray"
	});
	this.parentContainer.append(this.container);
	window._ActionTypeArray.push(this);
	this.initMenu();
};
urule.CellExecuteMethod.prototype.initMenu=function(actionLibraries){
	var data=window._uruleEditorActionLibraries;
	if(actionLibraries){
		data=actionLibraries;
	}
	var self,onClick,config;
	self=this;
	onClick=function(menuItem){
		var parent=menuItem.parent.parent;
		self.setAction({
			beanLabel:parent.label,
			beanId:parent.name,
			methodLabel:menuItem.label,
			methodName:menuItem.name,
			parameters:menuItem.parameters
		});
	};
	config={menuItems:[]};
	$.each(data||[],function(index,item){
		var springBeans=item.springBeans||[]; 
		$.each(springBeans,function(i,springBean){
			var menuItem={
				name:springBean.id,
				label:springBean.name
			}
			var methods=springBean.methods||[];
			$.each(methods,function(j,method){
				if(!menuItem.subMenu){
					menuItem.subMenu={menuItems:[]};
				}
				menuItem.subMenu.menuItems.push({
					name:method.methodName,
					label:method.name,
					parameters:method.parameters,
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
	this.container.click(function(e){
		self.menu.show(e);
	});
};
urule.CellExecuteMethod.prototype.clean=function(){
	window._setDirty();
	if(this.action){
		this.action.getContainer().remove();
	}
	URule.setDomContent(this.container,"无");
	this.container.css({
		"color":"gray"
	});
	this.action=null;
};
urule.CellExecuteMethod.prototype.setAction=function(data){
	window._setDirty();
	if(this.action){
		this.action.getContainer().remove();
	}
	this.action=new urule.MethodAction();
	URule.setDomContent(this.container,".");
	this.container.css({
		"color":"white"
	});
	this.parentContainer.append(this.action.getContainer());
	this.action.initData(data);
};
urule.CellExecuteMethod.prototype.toXml=function(){
	if(this.action){
		return this.action.toXml();		
	}
	return "";
};