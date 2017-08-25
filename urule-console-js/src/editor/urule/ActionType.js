/**
 * @author GJ
 */
urule.ActionType=function(parentContainer,rule){
	this.uuid=Math.uuid();
	parentContainer.prop('id',this.uuid);
	this.rule=rule;
	this.parentContainer=parentContainer;
	this.type="";
	this.init();
	window._ActionTypeArray.push(this);
	this.initMenu();
};
urule.ActionType.prototype.toXml=function(){
	if(this.type=="execute-function"){
		var xml="<execute-function ";
		xml+=this.action.toXml();
		xml+=">";
		xml+=this.action.getParameter().toXml();
		xml+="</execute-function>";
		return xml;
	}else{
		return this.action.toXml();
	}
};
urule.ActionType.prototype.initData=function(data){
	if(!data){
		return;
	}
	var actionType=data["actionType"];
	this.setAction(actionType, data);
};

urule.ActionType.prototype.init=function(){
	this.container=generateContainer();
	URule.setDomContent(this.container,"请选择动作类型");
	this.container.css({
		"color":"green"
	});
	this.parentContainer.append(this.container);
	this.action=null;
};
urule.ActionType.prototype.initMenu=function(actionLibraries){
	var data=window._uruleEditorActionLibraries;
	if(actionLibraries){
		data=actionLibraries;
	}
	var self,onClick,config;
	self=this;
	onClick=function(menuItem){
		var parent=menuItem.parent.parent;
		self.setAction("ExecuteMethod",{
			beanLabel:parent.label,
			beanId:parent.name,
			methodLabel:menuItem.label,
			methodName:menuItem.name,
			parameters:menuItem.parameters
		});
	};
	config={menuItems:[{
		label:"打印内容到控制台",
		onClick:function(){
			self.setAction("ConsolePrint");
		}
	},{
		label:"变量赋值",
		onClick:function(){
			self.setAction("VariableAssign");
		}
	},{
		label:"执行函数",
		onClick:function(){
			self.setAction("ExecuteCommonFunction");
		}
	}]};
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

urule.ActionType.prototype.initDefaultMenuData=function(){
	var self=this;
	var menuData=[];
	menuData.push({
		name:"打印内容到控制台",
		fun:function(){
			self.setAction("ConsolePrint");
		}
	});
	menuData.push({
		name:"变量赋值",
		fun:function(){
			self.setAction("VariableAssign");
		}
	});
	return menuData;
};

urule.ActionType.prototype.setAction=function(type,data){
	window._setDirty();
	if(this.action){
		this.action.getContainer().remove();
	}
	switch(type){
	case "ConsolePrint":
		this.action=new urule.PrintAction(this.rule);
		URule.setDomContent(this.container,"输出:");
		this.type="console-print";
		break;
	case "ExecuteMethod":
		this.action=new urule.MethodAction(this.rule);
		URule.setDomContent(this.container,"执行方法:");
		this.type="execute-method";
		break;
	case "VariableAssign":
		this.action=new urule.AssignmentAction(this.rule);
		URule.setDomContent(this.container,"变量赋值:");
		this.type="var-assign";
		break;
	case "ExecuteCommonFunction":
		this.action=new urule.FunctionValue(null,null,this.rule);
		URule.setDomContent(this.container,"执行函数:");
		this.type="execute-function";
		break;
	}
	this.parentContainer.append(this.action.getContainer());
	this.action.initData(data);
};