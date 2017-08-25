/**
 * Created by jacky on 2016/7/18.
 */
import BaseTool from './BaseTool.js';
import RuleNode from './RuleNode.js';
import * as event from '../components/componentEvent.js';
import {MsgBox} from "flowdesigner"

export default class RuleTool extends BaseTool{
    getType(){
        return '规则';
    }
    getIcon(){
        return `<i class="rf rf-rule" style="color:#737383"></i>`
    }
    newNode(){
        return new RuleNode();
    }
    getConfigs(){
        return {
            out:1
        };
    }
    getPropertiesProducer(){
        const _this=this;
        return function (){
            const g=$(`<div></div>`);
            const fileGroup=$(`<div class="form-group"><label>目标规则文件</label></div>`);
            const fileInputGroup=$('<div class="input-group"></div>');
            fileGroup.append(fileInputGroup);
            const fileText=$(`<input type="text" disabled class="form-control">`);
            const self=this;
            fileInputGroup.append(fileText);
            fileText.change(function(){
                self.file=$(this).val();
            });
            fileText.val(this.file);

            const fileButton=$(`<span class="input-group-btn" title="选择目标文件"><button class="btn btn-default"><i class="glyphicon glyphicon-search"></i></span>`);
            fileInputGroup.append(fileButton);
            const openFileButton=$(`<span class="input-group-btn" title="打开这个文件"><button class="btn btn-default"><i class="glyphicon glyphicon glyphicon-folder-open"></i></span>`);
            fileInputGroup.append(openFileButton);
            if(!this.file || this.file===''){
                openFileButton.addClass("disabled");
            }
            fileButton.click(function(){
                event.eventEmitter.emit(event.OPEN_KNOWLEDGE_TREE_DIALOG,{
                    project:window._project,
                    callback:function(file,version){
                        file='jcr:'+file;
                        self.file=file;
                        self.version=version;
                        fileText.val(file);
                        versionText.val(version);
                        openFileButton.removeClass("disabled");
                    }
                });
            });
            openFileButton.click(function(){
                if(!self.file || self.file===''){
                    MsgBox.alert("请先选择文件");
                    return;
                }
                let fileName=self.file;
                if(fileName.indexOf("jcr:")>-1){
                    fileName=fileName.substring(4,fileName.length);
                }
                const pos=fileName.indexOf(".")+1;
                const extName=fileName.substring(pos,fileName.length);
                let editorPath=window._server;
                if(extName==='rs.xml'){
                    editorPath+="/ruleseteditor";
                }else if(extName==='dt.xml'){
                    editorPath+="/decisiontableeditor";
                }else if(extName==='dtree.xml'){
                    editorPath+="/decisiontreeeditor";
                }else if(extName==='ul'){
                    editorPath+="/uleditor";
                }else if(extName==='sc'){
                    editorPath+="/scorecardeditor";
                }else if(extName==='rl.xml'){
                    editorPath+="/ruleflowdesigner";
                }
                if(editorPath===window._server){
                    MsgBox.alert("无法打开文件["+self.file+"]");
                    return;
                }
                let fullPath=fileName;
                if(self.version && self.version!=="LATEST"){
                    fullPath+=":"+self.version;
                }
                editorPath+="?file="+fullPath;
                const componentEvent=window.parent.componentEvent;
                if(componentEvent){
                    componentEvent.eventEmitter.emit("tree_node_click",{
                        fullPath,
                        name:fileName,
                        id:fullPath,
                        path:editorPath,
                        active:true
                    });
                }
            });
            g.append(fileGroup);
            const versionGroup=$(`<div class="form-group"><label>文件版本</label></div>`);
            const versionText=$(`<input type="text" disabled class="form-control">`);
            versionGroup.append(versionText);
            g.append(versionGroup);
            versionText.val(this.version);
            g.append(_this.getCommonProperties(this));
            return g;
        }
    }
}