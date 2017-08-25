/**
 * @author GJ
 */
urule.ConditionListDialog=function(project, category, colData){
	this.project = project;
	this.category = category;
	this.colData = colData;
	this.init();
};
urule.ConditionListDialog.prototype.open=function(doSuccess){
	var self = this;
	self.dialog.dialog("open");
	self.doSuccess=doSuccess;
};

urule.ConditionListDialog.prototype.setOption=function(option){
	this.dialog.dialog("option",option);
};

urule.ConditionListDialog.prototype.refresh=function(project, category, variable){
	var url="urule?action=loadcommonconditions&project="+project + "&category=" + category + "&variable=" + this.colData.variableCategory + "." + this.colData.variableLabel;
	this.table.bootstrapTable("refresh", {url : url});
};

urule.ConditionListDialog.prototype.init=function(){
	this.dialog=$("<div>");
	var tableHtml="<table data-height=\"299\">";
	tableHtml+="<thead><tr>";
	tableHtml+="<th data-field=\"name\">名称</th>";
	tableHtml+="<th data-field=\"condition\">条件</th>";
	tableHtml+="</tr></thead></table>";
	this.table=$(tableHtml);
	var url="urule?action=loadcommonconditions&project="+this.project + "&category=" + this.category + "&variable=" + this.colData.variableCategory + "." + this.colData.variableLabel;
	this.table.bootstrapTable({
		url:url,
		onDblClickRow : function(data){
			self.doSuccess(data.condition);
		}
	});
	this.dialog.append(this.table);
	var self=this;
	this.dialog.dialog({
		title:"常用条件列表【"+(self.variable || "")+"】",
		height:300,
		width:500,
		autoOpen:false,
		modal:false,
		show:false,
		open : function(){
			self.isShow = true;
		},
		close :function(){
			self.isShow = false;
		},
		buttons:[{
			text:"关闭",
			click:function(){
				$(this).dialog("destroy");
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