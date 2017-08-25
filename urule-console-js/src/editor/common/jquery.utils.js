(function($){
	if(!window.URule){
		window.URule={};
	}
	URule.setDomContent=function(jqueryObject,value){
		if(navigator.userAgent.indexOf("Firefox")>0){
			jqueryObject.prop("textContent",value);
		}else{
			jqueryObject.prop("innerText",value);
		}
	};
	URule.getDomContent=function(jqueryObject){
		if(navigator.userAgent.indexOf("Firefox")>0){
			return jqueryObject.prop("textContent");
		}else{
			return jqueryObject.prop("innerText");
		}
	};
})($);