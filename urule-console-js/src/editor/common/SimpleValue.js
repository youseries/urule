/**
 * @author GJ
 */
urule.SimpleValue=function(arithmetic,data){
	var TIP="请输入值";
	this.container=$("<span>");
	this.valueContainer=generateContainer();
	this.valueContainer.css({
		color:"rgb(180,95,4)"
	});
	this.editor=$(`<input type='text' class="form-control rule-text-editor" style="height: 22px;">`);
	var self=this;
	this.container.append(this.valueContainer).append(this.editor);
	this.editor.blur(function(){
		self.editor.hide();
		var text=self.editor.prop("value");
		if(text!=""){
			URule.setDomContent(self.valueContainer,text);
		}
		self.valueContainer.show();
		$(this).trigger("DOMSubtreeModified");
		window._setDirty();
	}).mousedown(function(evt){
		evt.stopPropagation();
	}).keydown(function(evt){
		evt.stopPropagation();
	});
	self.editor.hide();
	this.valueContainer.prop("innerText",TIP);
	this.valueContainer.click(function(){
		self.valueContainer.hide();
		var parent=self.container.parent();
		var maxWidth=120;
		if(parent && parent.parent() && parent.parent().parent()){
			parent=parent.parent().parent();
			var css=parent.prop("class");
			if(css && css=="htMiddle htDimmed current"){
				maxWidth=parent.width()-20;
			}
		}
		self.editor.css({
			width:maxWidth
		});
		self.editor.css({display:'inline'});
		self.editor.focus();
		$(this).trigger("DOMSubtreeModified");
	});
	this.arithmetic=arithmetic;
	this.container.append(arithmetic.getContainer());
	this.initData(data);
};

urule.SimpleValue.prototype.getDisplayContainer=function(){
	var container=$("<span>"+this.editor.prop("value")+"</span>");
	if(this.arithmetic){
		var dis=this.arithmetic.getDisplayContainer();
		if(dis){
			container.append(dis);			
		}
	}
	return container;
};

urule.SimpleValue.prototype.initData=function(data){
	if(!data){
		return;
	}
	var text=data["content"];
	//var disText=text.length>15?(text.substring(0,15)+"..."):text;
	URule.setDomContent(this.valueContainer,text);
	this.editor.prop("value",text);
	if(this.arithmetic){
		this.arithmetic.initData(data["arithmetic"]);
	}
};
urule.SimpleValue.prototype.getValue=function(){
	var value=this.editor.prop("value");
	value=value.replace(new RegExp("&","gm"),"&amp;");
	value=value.replace(new RegExp("<","gm"),"&lt;");
	value=value.replace(new RegExp(">","gm"),"&gt;");
	value=value.replace(new RegExp("'","gm"),"&apos;");
	value=value.replace(new RegExp("\"","gm"),"&quot;");
	return value;
};
urule.SimpleValue.prototype.getContainer=function(){
	return this.container;
};