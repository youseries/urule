/**
 * @author GJ
 */
urule.FunctionProperty=function(){
	this.container=$("<span>");
	this.label=generateContainer();
	this.container.append(this.label);
	URule.setDomContent(this.label,"选择属性");
	this.label.css({
		"color":"#004C85",
	});
};
urule.FunctionProperty.prototype.toXml=function(){
	if(!this.variableName){
		throw "请选择函数属性";
	}
	var xml="property-name=\""+this.variableName+"\"";
	xml+=" property-label=\""+this.variableLabel+"\"";
	return xml;
};
urule.FunctionProperty.prototype.initMenu=function(data){
	if(!data){
		return;
	}
	var self=this;
	var onClick=function(menuItem){
		self.setProperty({
			name:menuItem.name,
			label:menuItem.label,
			datatype:menuItem.type
		});
	};
	var menuConfig={menuItems:[]};
	$.each(data,function(index,item){
		menuConfig.menuItems.push({
			name:item.name,
			label:item.label,
			datatype:item.type,
			onClick:onClick
		});
	});
	this.menu=new URule.menu.Menu(menuConfig);
	this.label.click(function(e){
		self.menu.show(e);
	});
};
urule.FunctionProperty.prototype.setProperty=function(data){
	window._setDirty();
	this.variableName=data.name;
	this.variableLabel=data.label,
	URule.setDomContent(this.label,this.variableLabel);
};
urule.FunctionProperty.prototype.getContainer=function(){
	return this.container;
};