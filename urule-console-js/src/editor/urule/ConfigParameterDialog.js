/**
 * @author GJ
 */
import {MsgBox} from 'flowdesigner';
import * as event from '../../components/componentEvent.js';

urule.ConfigParameterDialog=function(parent){
	this.parent=parent;
	this.init();
};

urule.ConfigParameterDialog.prototype.open=function(){
	const _this=this;
	MsgBox.showDialog('参数库配置',this.dialogContent,[
		{
			name:'添加',
			holdDialog:true,
			click:function(){
				event.eventEmitter.emit(event.OPEN_KNOWLEDGE_TREE_DIALOG,{
					project:window._project,
					fileType:'ParameterLibrary',
					callback:function(file,version){
						let path='jcr:'+file;
						if(version!=='LATEST'){
							path+=':'+version;
						}
						const pos=window.parameterLibraries.indexOf(path);
						if(pos!==-1){
							MsgBox.alert('参数库文件已存在');
							return;
						}
						_this.tbody.append(_this.newLibRow(path));
						window.parameterLibraries.push(path);
						window.refreshParameterLibraries();
						window._setDirty();
					}
				});
			}
		}
	]);
};

urule.ConfigParameterDialog.prototype.init=function(){
	var self=this;
	const table=$(`<table class="table table-bordered">
		<thead><tr>
			<td>参数库文件</td><td style="width: 70px">操作</td>
		</tr></thead>
	</table>`);
	this.tbody=$(`<tbody></tbody>`);
	table.append(this.tbody);
	this.dialogContent=$('<div>');
	this.dialogContent.append(table);

	for(var i=0;i<window.parameterLibraries.length;i++){
		const lib=window.parameterLibraries[i];
		this.tbody.append(this.newLibRow(lib));
	}
};

urule.ConfigParameterDialog.prototype.newLibRow=function(lib){
	const row=$(`<tr>
			<td>${lib}</td>
		</tr>`);
	const delCol=$(`<td></td>`),delButton=$(`<button type="button" class="btn btn-link">删除</button>`);
	delCol.append(delButton);
	delButton.click(function(){
		const pos=window.parameterLibraries.indexOf(lib);
		window.parameterLibraries.splice(pos,1);
		row.remove();
		window.refreshParameterLibraries();
		window._setDirty();
	});
	row.append(delCol);
	return row;
};