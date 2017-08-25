/**
 * @author GJ
 */
urule.ResourceVersionDialog=function(path){
	this.path=path;
	this.init();
};
urule.ResourceVersionDialog.prototype.open=function(doSuccess){
	this.dialog.dialog("open");
	this.doSuccess=doSuccess;
};
urule.ResourceVersionDialog.prototype.init=function(){
	this.dialog=$("<div>");
	var tableHtml="<table data-height=\"299\" data-click-to-select=\"true\" data-show-columns=\"true\" data-select-item-name=\"radioName\">";
	tableHtml+="<thead><tr>";
	tableHtml+="<th data-field=\"state\" data-radio=\"true\"></th>";
	tableHtml+="<th data-field=\"name\" data-align=\"center\">版本号</th>";
	tableHtml+="<th data-field=\"createUser\" data-align=\"center\">创建人</th>";
	tableHtml+="<th data-field=\"createDate\" data-align=\"center\" data-formatter=\"__formatDate\">创建时间</th>";
	tableHtml+="</tr></thead></table>";
	this.table=$(tableHtml);
	var url="urule?action=loadversion&file="+this.path;
	this.table.bootstrapTable({
		url:url,
		onLoadSuccess:function(){
			self.table.css("margin-top", "");
		}
	});
	this.dialog.append(this.table);
	var self=this;
	this.dialog.dialog({
		title:"版本列表【"+self.path+"】",
		height:400,
		width:700,
		autoOpen:false,
		modal:true,
		show:false,
		buttons:[{
			text:"确认",
			click:function(){
				var selctedData=self.table.bootstrapTable("getSelections");
				if(selctedData.length==0){
					URule.alert("请选择一个版本！");
					return;
				}
				self.doSuccess(self.path+":"+selctedData[0].name);
				$(this).dialog("close");
			}
		},{
			text:"取消",
			click:function(){
				$(this).dialog("close");
			}
		}]
	});
};

function __formatDate(time){
	var datetime = new Date();  
    datetime.setTime(time);
	var year = datetime.getFullYear();  
	var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;  
	var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();  
	var hour = datetime.getHours()< 10 ? "0" + datetime.getHours() : datetime.getHours();  
	var minute = datetime.getMinutes()< 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();  
	var second = datetime.getSeconds()< 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();  
	return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;  
};