/**
 * @author GJ
 */
urule.MethodAction=function(rule){
	this.parameters=[];
	this.rule=rule;
	this.init();
};
urule.MethodAction.prototype.init=function(){
	this.container=$("<span>");
	this.nameContainer=$("<span>");
	this.container.append(this.nameContainer);
	this.nameContainer.css({
		"color":"darkblue"
	});
};
urule.MethodAction.prototype.initData=function(data){
	if(!data){
		return;
	}
	this.bean=data["beanId"];
	this.name=data["beanLabel"];
	this.method=data["methodName"];
	this.methodLabel=data["methodLabel"];
	var parameters=data["parameters"];
	this.parameterCount=0;
	if(parameters){
		this.parameterCount=parameters.length;
	}
	if(this.parameterCount===0){
        URule.setDomContent(this.nameContainer,this.methodLabel);
		var parameterLabel=$("<span style='color:gray'>(无参数)</span>");
		this.container.append(parameterLabel);
	}else{
        URule.setDomContent(this.nameContainer,this.methodLabel+"(");
    }
	if(this.parameterCount==0){
		return;
	}
	for(var i=0;i<this.parameterCount;i++){
		var p=parameters[i];
		if(i>0){
			var comma=$("<span>;</span>");
			this.container.append(comma);
		}
		if(this.parameterCount>0){
			var seqLabel=$("<span style='color:purple'>&nbsp;"+p["name"]+":</span>");
			this.container.append(seqLabel);			
		}
		var parameter=new urule.MethodParameter(this.rule);
		this.parameters.push(parameter);
		this.container.append(parameter.getContainer());
		parameter.initData(p);
	}
	this.container.append(")");
};
urule.MethodAction.prototype.toXml=function(){
	if(!this.name || this.name==""){
		throw "请选择要执行的方法！";
	}
	var xml="<execute-method bean=\""+this.bean+"\" bean-label=\""+this.name+"\" method-label=\""+this.methodLabel+"\" method-name=\""+this.method+"\">";
	for(var i=0;i<this.parameters.length;i++){
		var p=this.parameters[i];
		xml+=p.toXml();
	}
	xml+="</execute-method>";
	return xml;
};
urule.MethodAction.prototype.getContainer=function(){
	return this.container;
};