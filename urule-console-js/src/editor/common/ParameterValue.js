/**
 * @author GJ
 */
urule.ParameterValue=function(arithmetic,data,act){
	this.arithmetic=arithmetic;
	this.container=$("<span>");
	this.label=generateContainer();
	this.container.append(this.label);
	this.label.css({
		"color":"#6b3db0"
	});
	URule.setDomContent(this.label,"请选择参数");
	if(arithmetic){
		this.container.append(arithmetic.getContainer());		
	}
	if(data){
		this.initData(data);
	}
	window._ParameterValueArray.push(this);
	this.act=act;
	this.initMenu();
};

urule.ParameterValue.prototype.getDisplayContainer=function(){
	var container=$("<span>参数."+this.parameterLabel+"</span>");
	if(this.arithmetic){
		var dis=this.arithmetic.getDisplayContainer();
		if(dis){
			container.append(dis);			
		}	
	}
	return container;
};

urule.ParameterValue.prototype.matchAct=function(act){
	if(!this.act){
		return true;
	}
	if(act.indexOf(this.act)>-1){
		return true;
	}
	return false;
};
urule.ParameterValue.prototype.initMenu=function(parameterLibraries){
	var data=window._uruleEditorParameterLibraries;
	if(parameterLibraries){
		data=parameterLibraries;
	}
	if(!data){
		return;
	}
	var self,onClick,config;
	self=this;
	onClick=function(menuItem){
		self.setValue({
			variableName:menuItem.name,
			variableLabel:menuItem.label,
			datatype:menuItem.datatype
		});

	};
	config={menuItems:[]};
	$.each(data,function(index,variables){
		$.each(variables||[],function(i,variable){
			if(self.matchAct(variable.act)){
				var menuItem={
					name:variable.name,
					label:variable.label,
					datatype:variable.type,
					act:variable.act,
					onClick:onClick
				};
				config.menuItems.push(menuItem);
			}

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
urule.ParameterValue.prototype.setValue=function(data){
	this.parameterName=data["variableName"];
	this.parameterLabel=data["variableLabel"];
	this.datatype=data["datatype"];
	URule.setDomContent(this.label,"参数."+this.parameterLabel);
	window._setDirty();
};
urule.ParameterValue.prototype.initData=function(data){
	this.setValue(data);
	if(this.arithmetic){
		this.arithmetic.initData(data["arithmetic"]);			
	}
};

urule.ParameterValue.prototype.toXml=function(){
	if(!this.parameterLabel || this.parameterLabel==""){
		throw "参数不能为空！";
	}
	var xml=" var-category=\"参数\" var=\""+this.parameterName+"\" var-label=\""+this.parameterLabel+"\" datatype=\""+this.datatype+"\"";
	return xml;
};
urule.ParameterValue.prototype.getContainer=function(){
	return this.container;
};