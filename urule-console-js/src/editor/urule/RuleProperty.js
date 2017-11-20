/**
 * @author GJ
 */
urule.RuleProperty=function(parent,name,defaultValue,editorType){
	this.parent=parent;
	this.value=defaultValue;
	this.editorType=editorType;
	this.container=$("<span class='rule-property'>");
	var nameContainer=$("<span>");
	this.name=name;
	var label=this.getLabel();
	URule.setDomContent(nameContainer,label+"：");
	this.container.append(nameContainer);
	var valueContainer=$("<span>");
	var valueLabel=generateContainer();
	if(defaultValue=="")defaultValue="无";
	valueLabel.css({
		"color":"#000"
	});
	URule.setDomContent(valueLabel,defaultValue);
	valueContainer.append(valueLabel);
	this.container.append(valueContainer);
	var editor=null;
	this.radioName=Math.uuid(15);
	this.yesRadio=null;
	this.noRadio=null;
	if(editorType==1){
		editor=$("<input type='text' size='30' class='form-control rule-text-editor'>");
	}else if(editorType==2){
		editor=$("<input type='datetime' size='30' class='form-control rule-text-editor' title='日期格式为:yyyy-MM-dd HH:mm:ss，如2016-10-11 12:50:06'>");
	}else if(editorType==3){
		this.yesRadio=$("<input type='radio' value='是' name='"+this.radioName+"'> 是 </input>");
		this.noRadio=$("<input type='radio' value='否' name='"+this.radioName+"'> 否</input>");
	}
	var self=this;
	if(editorType!=3){
		editor.blur(function(){
			self.value=editor.prop("value");
			editor.hide();
			if(self.value==""){
				URule.setDomContent(valueLabel,"无");
			}else{
				URule.setDomContent(valueLabel,self.value);
			}
			valueLabel.show();
			window._setDirty();
		});
		valueLabel.click(function(){
			valueLabel.hide();
			editor.prop("value",self.value);
			editor.show();
			editor.focus();
		});
		this.container.append(editor);
		editor.hide();
		if(editorType==2){
			if(defaultValue!=="无"){
				var defaultDate=new Date(defaultValue);
				this.value=defaultValue;//formatDate(defaultDate,'Y-m-d H:m:s');
				URule.setDomContent(valueLabel,this.value);
			}
		}
	}else{
		if(defaultValue==true){
			this.yesRadio.prop("checked",true);
		}else{
			this.noRadio.prop("checked",true);
		}
		this.yesRadio.change(function(){
			window._setDirty();
		});
		this.noRadio.change(function(){
			window._setDirty();
		});
		valueLabel.hide();
		this.container.append(this.yesRadio);
		this.container.append(this.noRadio);
	}
	var del=$(`<i class="glyphicon glyphicon-remove rule-property-del">`);
	del.click(function(){
		self.container.remove();
		var pos=self.parent.properties.indexOf(self);
		self.parent.properties.splice(pos,1);
		window._setDirty();
	});
	this.container.append(del);
};
urule.RuleProperty.prototype.getLabel=function(){
	var label="";
	if(this.name=="salience"){
		label="优先级";
	}else if(this.name=="loop"){
		label="允许循环触发";
	}else if(this.name=="effective-date"){
		label="生效日期";
	}else if(this.name=="expires-date"){
		label="失效日期";
	}else if(this.name=="enabled"){
		label="是否启用";
	}else if(this.name=="debug"){
		label="允许调试信息输出";
	}else if(this.name=="activation-group"){
		label="互斥组";
	}else if(this.name=="agenda-group"){
		label="执行组";
	}else if(this.name=="auto-focus"){
		label="自动获取焦点";
	}else if(this.name=="ruleflow-group"){
		label="规则流组";
	}
	return label;
};
urule.RuleProperty.prototype.toXml=function(){
	var xml=this.name;
	if(this.editorType==3){
		if(this.yesRadio.prop("checked")){
			xml+="=\"true\"";
		}else{
			xml+="=\"false\"";			
		}
	}else{
		if(!this.value || this.value==""){
			throw "请输入属性"+this.name+"的具体值!";
		}
		xml+="=\""+this.value+"\"";
	}
	return xml;
};
urule.RuleProperty.prototype.getContainer=function(){
	return this.container;
};
