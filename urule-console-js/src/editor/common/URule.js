/**
 * @author GJ
 */
import {MsgBox} from 'flowdesigner';

window._ConstantValueArray=[];
window._ActionTypeArray=[];
window._VariableValueArray=[];
window._ParameterValueArray=[];
window._FunctionValueArray=[];
window.actionLibraries=[];
window.variableLibraries=[];
window.constantLibraries=[];
window.parameterLibraries=[];
window.urule={};

window.generateContainer=function(){
	var container=$("<span>.</span>");
	container.css({
		height:"20px",
		cursor:"pointer",
		margin:"0px",
		color:"white",
		border:"dashed transparent 1px"
	});
	container.mouseover(function(){
		container.css({
			border:"dashed gray 1px"
		});
	});
	container.mouseout(function(){
		container.css({
			border:"dashed transparent 1px"
		});
	});
	return container;
};

window.refreshParameterLibraries=function(){
	var parameterFiles="";
	for(var i=0;i<parameterLibraries.length;i++){
		var parameter=parameterLibraries[i];
		if(i==0){
			parameterFiles=parameter;
		}else{
			parameterFiles+=";"+parameter;
		}
	}
	if(parameterFiles=="" || parameterFiles.length<2){
		return;
	}
	var url=window._server+'/common/loadXml';
	$.ajax({
		type:'POST',
		data:{files:parameterFiles},
		url:url,
		error:function(req,error){
			MsgBox.alert("加载文件失败！");
		},
		success:function(data){
			window._uruleEditorParameterLibraries=data;
			$.each(window._ParameterValueArray,function(index,item){
				item.initMenu(data);
			});
		}
	});	
};

window.refreshVariableLibraries=function(){
	var variableFiles="";
	for(var i=0;i<variableLibraries.length;i++){
		var variable=variableLibraries[i];
		if(i==0){
			variableFiles=variable;
		}else{
			variableFiles+=";"+variable;
		}
	}
	if(variableFiles=="" || variableFiles.length<2){
		return;
	}
	var url=window._server+'/common/loadXml';
	$.ajax({
		type:'POST',
		data:{files:variableFiles},
		url:url,
		error:function(req,error){
			MsgBox.alert("加载文件失败！");
		},
		success:function(data){
			window._uruleEditorVariableLibraries=data;
			$.each(window._VariableValueArray,function(index,item){
				item.initMenu(data);
			});
		}
	});
};
window.refreshActionLibraries=function(){
	var actionFiles="";
	for(var i=0;i<actionLibraries.length;i++){
		var action=actionLibraries[i];
		if(i==0){
			actionFiles=action;
		}else{
			actionFiles+=";"+action;
		}
	}
	if(actionFiles=="" || actionFiles.length<2){
		actionFiles="builtinactions";
	}
	var url=window._server+'/common/loadXml';
	$.ajax({
		type:'POST',
		data:{files:actionFiles},
		url:url,
		error:function(req,error){
			MsgBox.alert("加载文件失败！");
		},
		success:function(data){
			window._uruleEditorActionLibraries=data;
			$.each(window._ActionTypeArray,function(index,item){
				item.initMenu(data);
			});
		}
	});
};
window.refreshFunctionLibraries=function(){
	var url=window._server+'/common/loadFunctions';
	$.ajax({
		url:url,
		error:function(req,error){
			MsgBox.alert("加载函数失败！");
		},
		success:function(data){
			window._uruleEditorFunctionLibraries=data;
			$.each(window._FunctionValueArray,function(index,item){
				item.initMenu(data);
			});
		}
	});
};

window.refreshConstantLibraries=function(){
	var constantFiles="";
	for(var i=0;i<constantLibraries.length;i++){
		var constant=constantLibraries[i];
		if(i==0){
			constantFiles=constant;
		}else{
			constantFiles+=";"+constant;
		}
	}
	if(constantFiles=="" || constantFiles.length<2){
		return;
	}
	var url=window._server+'/common/loadXml';
	$.ajax({
		data:{files:constantFiles},
		url:url,
		type:'POST',
		error:function(req,error){
			MsgBox.alert("加载文件失败！");
		},
		success:function(data){
			window._uruleEditorConstantLibraries=data;
			$.each(window._ConstantValueArray,function(index,item){
				item.initMenu(data);
			});
		}
	});
};


