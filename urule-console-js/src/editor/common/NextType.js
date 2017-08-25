/**
 * @author GJ
 */
urule.NextType=function(rule){
	this.container=$("<span>");
	this.rule=rule;
	this.inputType=null;
	this.paren=null;
	this.selectorLabel=generateContainer();
	this.selectorLabel.css({
		"fontWeight":"blod",
		"color":"#fff"
	});
	this.container.append(this.selectorLabel);
	URule.setDomContent(this.selectorLabel,".");
	var self=this;
	var onClick=function(menu){
		var type=menu.name;
		self.doNext(type);
		window._setDirty();
	};
	self.menu=new URule.menu.Menu({
		menuItems:[{
			label : "值",
			name:"value",
			onClick : onClick
		},{
			label : "括号",
			name:"paren",
			onClick : onClick
		}]
	});	
	this.selectorLabel.click(function(e){
		self.menu.show(e);
	});
};
urule.NextType.prototype.initData=function(data){
	var value=data["value"];
	var valueType=value["valueType"];
	if(valueType=="Paren"){
		this.doNext("paren");
		this.paren.initData(value);
	}else{
		this.doNext("value");
		this.inputType.setValueType(valueType, value);
	}
};
urule.NextType.prototype.toXml=function(){
	if(this.paren){
		return this.paren.toXml();
	}else if(this.inputType){
		return this.inputType.toXml();
	}
	return "";
};
urule.NextType.prototype.getDisplayContainer=function(){
	if(this.inputType){
		return this.inputType.getDisplayContainer();
	}else if(this.paren){
		return this.paren.getDisplayContainer();
	}
	return null;
};
urule.NextType.prototype.getContainer=function(){
	return this.container;
};
urule.NextType.prototype.doNext=function(type){
	if(type=="value"){
		if(this.paren){
			this.paren.getContainer().remove();
			this.paren=null;
		}
		if(!this.inputType){
			this.inputType = new urule.InputType(null,null,null,this.rule);
			this.container.append(this.inputType.getContainer());
		}
	}else if(type=="paren"){
		if(this.inputType){
			this.inputType.getContainer().remove();
			this.inputType=null;
		}
		if(!this.paren){
			this.paren=new urule.Paren(this.rule);
			this.container.append(this.paren.getContainer());
		}
	}
};