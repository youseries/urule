/**
 * @author GJ
 */
urule.MethodValue=function(arithmetic,data){
	this.arithmetic=arithmetic;
	this.container=$("<span>");
	this.rightParn=$("<span style='color:blue'>]</span>");
	this.label=generateContainer();
	this.fetchLength=false;
	this.uppercase=false;
	this.lowercase=false;
	this.fetchSize=false;
	this.container.append(this.label);
	this.label.css({
		"color":"blue"
	});
	this.actionContainer=$("<span></span>");
	this.container.append(this.actionContainer);
	URule.setDomContent(this.label,"请选择方法");
	if(arithmetic){
		this.container.append(arithmetic.getContainer());		
	}
	if(data){
		this.setAction(data);
		arithmetic.initData(data["arithmetic"]);
	}
	window._ActionTypeArray.push(this);
	this.initMenu();
};

urule.MethodValue.prototype.initMenu=function(actionLibraries){
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
			};
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
	this.label.click(function(e){
		self.menu.show(e);
	});
	
};

urule.MethodValue.prototype.setAction=function(data){
	window._setDirty();
	if(this.action){
		this.action.getContainer().remove();
	}
	this.action=new urule.MethodAction();
	URule.setDomContent(this.label,"[");
	this.actionContainer.append(this.action.getContainer());
	this.actionContainer.append(this.rightParn);
	
	this.action.initData(data);
};

urule.MethodValue.prototype.getDisplayContainer=function(){
	var name="",method="";
	if(this.action){
		name=this.action.name;
		method=this.action.methodLabel;
	}
	var container=$("<span>"+method+"</span>");
	if(this.arithmetic){
		var dis=this.arithmetic.getDisplayContainer();
		if(dis){
			container.append(dis);
		}
	}
	return container;
};

urule.MethodValue.prototype.toXml=function(){
	if(!this.action.bean || this.action.name==""){
		throw "执行方法不能为空！";
	}
	var xml="bean-name=\""+this.action.bean+"\"";
	xml+=" bean-label=\""+this.action.name+"\"";
	xml+=" method-name=\""+this.action.method+"\"";
	xml+=" method-label=\""+this.action.methodLabel+"\"";
	return xml;
};

urule.MethodValue.prototype.getContainer=function(){
	return this.container;
};