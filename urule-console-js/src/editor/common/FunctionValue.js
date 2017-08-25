/**
 * @author GJ
 */
urule.FunctionValue=function(arithmetic,data,rule){
	this.arithmetic=arithmetic;
	this.container=$("<span>");
	this.rule=rule;
	this.leftParn=$("<span style='color:blue'>(</span>");
	this.rightParn=$("<span style='color:blue'>)</span>");
	this.label=generateContainer();
	this.container.append(this.label);
	this.label.css({
		"color":"#008080"
	});
	this.functionContainer=$("<span></span>");
	this.container.append(this.functionContainer);
	URule.setDomContent(this.label,"请选择函数");
	if(arithmetic){
		this.container.append(arithmetic.getContainer());		
	}
	if(data){
		this.setFunction(data);
		if(arithmetic){
			arithmetic.initData(data["arithmetic"]);
		}
	}
	window._FunctionValueArray.push(this);
	this.initMenu();
};

urule.FunctionValue.prototype.getDisplayContainer=function(){
	var container=$("<span>"+this.functionName+"</span>");
	if(this.arithmetic){
		var dis=this.arithmetic.getDisplayContainer();
		if(dis){
			container.append(dis);			
		}	
	}
	return container;
};
urule.FunctionValue.prototype.toXml=function(){
	if(!this.functionLabel){
		throw "请选择函数";
	}
	if(!this.functionName){
		throw "请选择函数";
	}
	var xml=" function-label=\""+this.functionLabel+"\"";
	xml+=" function-name=\""+this.functionName+"\"";
	return xml;
};

urule.FunctionValue.prototype.initMenu=function(functionLibraries){
	var data=window._uruleEditorFunctionLibraries;
	if(functionLibraries){
		data=functionLibraries;
	}
	var self,onClick,config;
	self=this;
	onClick=function(menuItem){
		self.setFunction({
			parameter:menuItem.parameter,
			label:menuItem.label,
			name:menuItem.name
		});
	};
	config={menuItems:[]};
	$.each(data||[],function(index,item){
		config.menuItems.push({
			name:item.name,
			label:item.label,
			parameter:item.argument,
			onClick:onClick
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
urule.FunctionValue.prototype.initData=function(data){
	if(data){
		this.setFunction(data);		
	}
};

urule.FunctionValue.prototype.setFunction=function(data){
	window._setDirty();
	this.functionContainer.empty();
	URule.setDomContent(this.label,data.label);
	this.functionContainer.append(this.leftParn);
	this.functionLabel=data.label;
	this.functionName=data.name;
	this.parameter=new urule.FunctionParameter(this.rule);
	this.parameter.initData(data.parameter);
	this.functionContainer.append(this.parameter.getContainer());
	this.functionContainer.append(this.rightParn);
};
urule.FunctionValue.prototype.getFirstParameter=function(){
	return this.firstParameter;
};

urule.FunctionValue.prototype.getParameter=function(){
	return this.parameter;
};

urule.FunctionValue.prototype.getContainer=function(){
	return this.container;
};