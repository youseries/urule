isAdmin = function() {
	if(window._isAdmin === undefined) {
		$.ajax({
			url:"urule?action=checkadmin",
			async: false,
			type : "POST",
			success:function(data){
				window._isAdmin = data;
			}
		});
	}
	return window._isAdmin;
};

hasPermission = function(type) {	
	if(isAdmin()) {
		return true;
	}
	var permissions = getPermissions(type);
	for(var i = 0; i < permissions.length; i ++) {
		var p = permissions[i];
		if(p.granted == true){
			return true;
		}
		return false;
	}
	
	return false;
};

getPermissions = function(type) {
	var path = getRequestParameter("file");
	$.ajax({
		url:"urule?action=loadpermission",
		async: false,
		type : "POST",
		data : {
			path : path.substring(0, path.lastIndexOf("/")),
			type : type || "Mod" + getRequestParameter("type")
		},
		success:function(data){
			_permissions = data || [];
		}
	});
	
	return _permissions;
};

getRequestParameter=function(name){
	var value=null;
	var params=window.location.search.substring(1).split("&");
	for(var i=0;i<params.length;i++){
		var param=params[i];
		if(param.indexOf("=")==-1){
			continue;
		}
		var pair=param.split("=");
		var key=pair[0];
		if(key==name){
			value=pair[1];
			break;
		}
	}
	
	return value;
};