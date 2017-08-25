/**
 * @author Administrator
 */
urule.ResourceListDialog=function(type,select,doSuccess){
	this.type=type;
	this.select=select;
	this.doSuccess=doSuccess;
	this.init();
};
urule.ResourceListDialog.prototype.open=function(){
	this.initData();
	this.dialog.dialog("open");
};
urule.ResourceListDialog.prototype.close=function(){
	this.dialog.dialog("close");
};
urule.ResourceListDialog.prototype.getSelectedFile=function(){
	var selected=false;
	var selectedNode=null;
	var nodes = this.tree.getSelectedNodes();
	if(nodes && nodes.length>0){
		selectedNode=nodes[0];
		if(!selectedNode["isParent"]){
			selected=true;
		}
	}
	if(!selected){
		URule.alert("请先选择一个库文件！");
		return null;
	}
	return selectedNode["fullPath"];
};

urule.ResourceListDialog.prototype.init=function(){
	var self=this;
	this.dialog=$("<div>");
	this.treeContainer=$("<div class='ztree'>");
	this.dialog.append(this.treeContainer);
	this.dialog.dialog({
		title:"库列表",
		height:500,
		width:400,
		autoOpen:false,
		modal:true,
		show:false,
		buttons:[{
			text:"确认",
			click:function(){
				var selectedFile=self.getSelectedFile();
				if(!selectedFile){
					return;
				}
				self.doSelectFile(selectedFile);
			}
		},{
			text:"选择版本",
			click:function(){
				var selectedFile=self.getSelectedFile();
				if(!selectedFile){
					return;
				}
				var versionDialog=new urule.ResourceVersionDialog(selectedFile);
				versionDialog.open(function(file){
					self.doSelectFile(file);
				});
			}
		},{
			text:"取消",
			click:function(){
				$(this).dialog("close");
			}
		}]
	});
};
urule.ResourceListDialog.prototype.doSelectFile=function(selectedFile){
	var fullPath="jcr:"+selectedFile;
	if(this.doSuccess){
		this.doSuccess(this.type,fullPath);
		this.dialog.dialog("close");
		return;
	}
	var dup=false;
	this.select.each(function(){
		$(this.childNodes).each(function(){
			var path=this.textContent;
			if(path==fullPath){
				dup=true;
			}
		});
	});
	if(!dup){
		var self=this;
		var item=$("<a href='javascript:void(0)' class='list-group-item'>"+fullPath+"</a>");
		item.click(function(){
			self.select.find(".active").removeClass("active");
			item.addClass("active");
		});
		this.select.append(item);
		this.dialog.dialog("close");
	}else{
		URule.alert("当前库文件已被添加！");
	}
};
urule.ResourceListDialog.prototype.initData=function(){
	var self=this;
	var url=(uruleServer || "")+"urule?action=loadrepo&filetype="+this.type+"&path="+self.getRequestParameter("file");
	$.ajax({
		cache:false,
		dataType:"json",
		url:url,
		error:function(req,error){
			URule.alert("加载资源失败："+error);
		},
		success:function(data){
			self.tree=$.fn.zTree.init(self.treeContainer, {}, data);
		}
	});
};

urule.ResourceListDialog.prototype.getRequestParameter=function(name){
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