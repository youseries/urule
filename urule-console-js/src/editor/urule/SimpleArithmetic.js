/**
 * @author GJ
 */
urule.SimpleArithmetic=function(){
	this.container=$("<span>");
	this.selectorLabel=generateContainer();
	URule.setDomContent(this.selectorLabel,".");
	this.selectorLabel.css({
		"color":"#FFF"
	});
	this.operator="";
	this.container.append(this.selectorLabel);
	this.value=null;
	var self=this;
	var onClick=function(menuItem){
		self.setOperator(menuItem.name);
	};
	self.menu=new URule.menu.Menu({
		menuItems:[{
			label : "+",
			name:"Add",
			onClick : onClick
		}, {
			label : "-",
			name:"Sub",
			onClick : onClick
		}, {
			label : "x",
			name:"Mul",
			onClick : onClick
		}, {
			label : "÷",
			name:"Div",
			onClick : onClick
		}, {
			label : "%",
			name:"Mod",
			onClick : onClick
		},{
			label : "删除",
			onClick : function(){
				if(self.value){
					self.value.getContainer().remove();
					self.operator=null;
					self.value=null;
					URule.setDomContent(self.selectorLabel,".");
					self.selectorLabel.css({
						"color":"#FFF",
						"padding-left":"0px",
						"padding-right":"0px"
					});
				}
			}
		}]
	});
	this.selectorLabel.click(function(e){
		self.menu.show(e);
	});
	
};
urule.SimpleArithmetic.prototype.initData=function(data){
	if(!data){
		return;
	}
	var type=data["type"];
	this.setOperator(type);
	this.value.initData(data["value"]);
};
urule.SimpleArithmetic.prototype.setOperator=function(operator){
	window._setDirty();
	this.operator=operator;
	var info="";
	switch(operator){
	case "Add":
		info="+";
		break;
	case "Sub":
		info="-";
		break;
	case "Mul":
		info="x";
		break;
	case "Div":
		info="÷";
		break;
	case "Mod":
		info="%";
		break;
	}
	this.selectorLabel.css({
		"color":"green",
		"fontWeight":"blod",
		"padding-left":"5px",
		"padding-right":"5px"
	});
	URule.setDomContent(this.selectorLabel,info);
	if (!this.value) {
		this.simpleArithmetic = new urule.SimpleArithmetic();
		this.value = new urule.SimpleValue(this.simpleArithmetic);
		this.container.append(this.value.getContainer());
	}
};
urule.SimpleArithmetic.prototype.toXml=function(){
	if(!this.operator || this.operator==""){
		return "";
	}
	if(!this.value){
		throw "请输入具体值！";
	}
	var value=this.value.getValue();
	if(value==""){
		throw "请输入具体值！";
	}
	var xml="<simple-arith type=\""+this.operator+"\" value=\""+value+"\">";
	xml+=this.simpleArithmetic.toXml();
	xml+="</simple-arith>";
	return xml;
};
urule.SimpleArithmetic.prototype.getContainer=function(){
	return this.container;
};