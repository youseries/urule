/**
 * @author GJ
 */
urule.NamedReferenceValue=function(arithmetic,data,rule){
	this.arithmetic=arithmetic;
	this.container=$("<span>");
    this.referenceName=null;
	this.propertyName=null;
	this.propertyLabel=null;
	this.rule=rule;
	if(rule){
		rule.namedReferenceValues.push(this);
	}
	this.referenceNamelabel=generateContainer();
    this.container.append(this.referenceNamelabel);
    this.referenceNamelabel.css({
		"color":"#9C27B0"
	});
	URule.setDomContent(this.referenceNamelabel,"请选择引用变量名");

	this.referencePropertylabel=generateContainer();
	this.referencePropertylabel.css({
		"color":"#673AB7"
	});
	this.container.append(this.referencePropertylabel);
	URule.setDomContent(this.referencePropertylabel,"请选择变量属性");

	if(arithmetic){
		this.container.append(arithmetic.getContainer());		
	}
	if(data){
		this.initData(data);
	}
	this.initMenu();
};

urule.NamedReferenceValue.prototype.getDisplayContainer=function(){
	var container=$("<span>"+this.propertyName+"."+this.propertyLabel+"</span>");
	if(this.arithmetic){
		var dis=this.arithmetic.getDisplayContainer();
		if(dis){
			container.append(dis);			
		}	
	}
	return container;
};
urule.NamedReferenceValue.prototype.initMenu=function(){
	if(!this.rule){
		return;
	}
	var self=this,refNamedMenuConfig={menuItems:[]};
	for(let name of this.rule.namedMap.keys()){
		refNamedMenuConfig.menuItems.push({
			label:name,
			onClick:function (item) {
				self.referenceName=name;
				URule.setDomContent(self.referenceNamelabel,self.referenceName+".");
				var category=self.rule.namedMap.get(name) || {};
				var variables=category.variables || [];
				self.initPropertyMenu(variables);
				window._setDirty();
			}
		});
	}
	if(this.referenceName && this.rule){
		var category=this.rule.namedMap.get(this.referenceName) || {};
		var variables=category.variables;
		if(variables){
			self.initPropertyMenu(variables);
		}
	}
	if(self.menu){
		self.menu.setConfig(refNamedMenuConfig);
	}else{
		self.menu=new URule.menu.Menu(refNamedMenuConfig);
	}
	this.referenceNamelabel.click(function(e){
		self.menu.show(e);
	});
};
urule.NamedReferenceValue.prototype.initPropertyMenu=function(variables){
	var self=this;
	var propertyMenuConfig={menuItems:[]};
	for(let variable of variables){
		propertyMenuConfig.menuItems.push({
			label:variable.label,
			onClick:function (propItem) {
				self.propertyName=variable.name;
				self.propertyLabel=variable.label;
				self.datatype=variable.type;
				URule.setDomContent(self.referencePropertylabel,self.propertyLabel);
				window._setDirty();
			}
		});
	}
	if(self.propertyMenu){
		self.propertyMenu.setConfig(propertyMenuConfig);
	}else{
		self.propertyMenu=new URule.menu.Menu(propertyMenuConfig);
	}
	self.referencePropertylabel.click(function(e){
		self.propertyMenu.show(e);
	});
};
urule.NamedReferenceValue.prototype.setValue=function(data){
	var self=this;
	this.referenceName=data["referenceName"];
	this.propertyName=data["propertyName"] || data["variableName"];
	this.propertyLabel=data["propertyLabel"] || data["variableLabel"];
	this.datatype=data["datatype"];
	URule.setDomContent(this.referenceNamelabel,this.referenceName+".");
	URule.setDomContent(this.referencePropertylabel,this.propertyLabel);
	window._setDirty();
};
urule.NamedReferenceValue.prototype.initData=function(data){
	this.setValue(data);
	if(this.arithmetic){
		this.arithmetic.initData(data["arithmetic"]);			
	}
};

urule.NamedReferenceValue.prototype.toXml=function(){
	if(!this.referenceName || !this.propertyName || this.propertyName===""){
		throw "引用变量信息不能为空！";
	}
	var xml="reference-name=\""+this.referenceName+"\" property-name=\""+this.propertyName+"\"" +
			"  property-label=\""+this.propertyLabel+"\" datatype=\""+this.datatype+"\"";
	return xml;
};
urule.NamedReferenceValue.prototype.getType=function(){
	return "NamedReference";
};
urule.NamedReferenceValue.prototype.getContainer=function(){
	return this.container;
};