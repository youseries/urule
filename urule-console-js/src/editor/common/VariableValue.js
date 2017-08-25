/**
 * @author GJ
 */
urule.VariableValue=function(arithmetic,data,act,functionProperty){
	this.arithmetic=arithmetic;
	this.container=$("<span>");

    var self=this;
    this.label=generateContainer();
    this.functionProperty=functionProperty;
    this.container.append(this.label);
    this.label.css({
		"color":"darkcyan"
	});
	URule.setDomContent(this.label,"请选择变量");
	if(arithmetic){
		this.container.append(arithmetic.getContainer());		
	}
	if(data){
		this.initData(data);
	}
	window._VariableValueArray.push(this);
	this.act=act;
	this.initMenu();
};

urule.VariableValue.prototype.getDisplayContainer=function(){
	var container=$("<span>"+this.category+"."+this.variableLabel+"</span>");
	if(this.arithmetic){
		var dis=this.arithmetic.getDisplayContainer();
		if(dis){
			container.append(dis);			
		}	
	}
	return container;
};

urule.VariableValue.prototype.matchAct=function(act){
	if(!this.act){
		return true;
	}
	if(act.indexOf(this.act)>-1){
		return true;
	}
	return false;
};
urule.VariableValue.prototype.initMenu=function(variableLibraries){
	var data=window._uruleEditorVariableLibraries;
	if(variableLibraries){
		data=variableLibraries;
	}
	if(!data){
		return;
	}
	var self,onCategoryClick,onVariableClick,config;
	self=this;
	onCategoryClick=function(menuItem){
		self.setValue({variableCategory:menuItem.label,variables:menuItem.variables});
	};
	onVariableClick=function(menuItem){
		self.setValue({
			variables:menuItem.parent.parent.variables,
			variableCategory:menuItem.parent.parent.label,
			variableLabel:menuItem.label,
			variableName:menuItem.name,
			datatype:menuItem.datatype
		});
	};
	config={menuItems:[]};
	$.each(data,function(index,categories){
		$.each(categories,function(i,category){
			var variables=category.variables;
			if(self.functionProperty && self.category){
				if(category.name==self.category){
					self.functionProperty.initMenu(variables);
				}
			}
			var menuItem={
				label:category.name,
				variables:variables,
				onClick:onCategoryClick
			}
			$.each(variables||[],function(j,variable){
				if(!menuItem.subMenu){
					menuItem.subMenu={menuItems:[]};
				}
				if(self.matchAct(variable.act)){
					var subMenuItem={
						name:variable.name,
						label:variable.label,
						datatype:variable.type,
						act:variable.act,
						variables:variables,
						onClick:onVariableClick
					};
					menuItem.subMenu.menuItems.push(subMenuItem);
				}
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
urule.VariableValue.prototype.setValue=function(data){
	var self=this;
	this.category=data["variableCategory"];
	this.variableName=data["variableName"];
	this.variableLabel=data["variableLabel"];
	this.datatype=data["datatype"];
	if(this.functionProperty){
		this.functionProperty.initMenu(data["variables"]);			
	}
	if(this.variableLabel){
		URule.setDomContent(this.label,this.category+"."+this.variableLabel);
	}else{
		URule.setDomContent(this.label,this.category);
	}
	window._setDirty();
};
urule.VariableValue.prototype.initData=function(data){
	this.setValue(data);
	if(this.arithmetic){
		this.arithmetic.initData(data["arithmetic"]);			
	}
};

urule.VariableValue.prototype.toXml=function(){
	if(!this.category || this.category==""){
		throw "变量不能为空！";
	}
	var xml="var-category=\""+this.category+"\"";
	if(this.variableName){
		xml+=" var=\""+this.variableName+"\" var-label=\""+this.variableLabel+"\" datatype=\""+this.datatype+"\"";
	}
	return xml;
};
urule.VariableValue.prototype.getType=function(){
	if(this.variableName){
		return "Variable";
	}else{
		return "VariableCategory";
	}
};
urule.VariableValue.prototype.getContainer=function(){
	return this.container;
};