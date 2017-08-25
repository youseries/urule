/**
 * Created by jacky on 2016/7/18.
 */
import '../css/iconfont.css';
import '../../node_modules/bootstrap/dist/css/bootstrap.css';
import '../../node_modules/codemirror/lib/codemirror.css';
import '../../node_modules/codemirror/addon/hint/show-hint.css';
import '../../node_modules/codemirror/addon/lint/lint.css';
import './flow.css';
import RuleFlowDesigner from './RuleFlowDesigner.js';
import StartTool from './StartTool.js';
import RuleTool from './RuleTool.js';
import PackageTool from './PackageTool.js';
import ActionTool from './ActionTool.js';
import ScriptTool from './ScriptTool.js';
import DecisionTool from './DecisionTool.js';
import ForkTool from './ForkTool.js';
import JoinTool from './JoinTool.js';
import React from 'react';
import ReactDOM from 'react-dom';
import {MsgBox,Event} from 'flowdesigner';
import KnowledgeTreeDialog from '../components/dialog/component/KnowledgeTreeDialog.jsx';
import * as event from '../components/componentEvent.js';
import {getParameter,ajaxSave} from '../Utils.js';


$(document).ready(function(){
    const containerId='container';
    const designer=new RuleFlowDesigner(containerId);
    const file=getParameter('file');
    designer.addButton({
        icon:'<i class="rf rf-save"></i>',
        tip:'保存',
        click:function(){
            event.eventEmitter.emit(event.SHOW_LOADING,"数据保存中");
            const content=designer.toXML();
            if(!content){
                event.eventEmitter.emit(event.HIDE_LOADING);
                return;
            }

            let postData={content,file,newVersion:false};
            const url=window._server+'/common/saveFile';
            ajaxSave(url,postData,function () {
                event.eventEmitter.emit(event.HIDE_LOADING);
                MsgBox.alert('保存成功');
            });
        }
    });
    designer.addButton({
        icon:'<i class="rf rf-savenewversion"></i>',
        tip:'保存为新版本',
        click:function(){
            event.eventEmitter.emit(event.SHOW_LOADING,"数据保存中");
            const content=designer.toXML();
            if(!content){
                event.eventEmitter.emit(event.HIDE_LOADING);
                return;
            }

            let postData={content,file,newVersion:true};
            const url=window._server+'/common/saveFile';
            bootbox.prompt("请输入新版本描述.",function (versionComment) {
                if(!versionComment){
                    return;
                }
                postData.versionComment=versionComment;
                ajaxSave(url,postData,function () {
                    event.eventEmitter.emit(event.HIDE_LOADING);
                })
            });
        }
    });
    designer.addTool(new StartTool());
    designer.addTool(new RuleTool());
    designer.addTool(new PackageTool());
    designer.addTool(new ActionTool());
    designer.addTool(new ScriptTool());
    designer.addTool(new DecisionTool());
    designer.addTool(new ForkTool());
    designer.addTool(new JoinTool());
    designer.buildDesigner();

    const container=$('#'+containerId);
    container.append('<div id="__dialog_container"></div>');
    ReactDOM.render(
        <div>
            <KnowledgeTreeDialog/>
        </div>,
        document.getElementById('__dialog_container')
    );
    $.ajax({
        url:window._server+'/ruleflowdesigner/loadFlowDefinition',
        data:{file},
        success:function (json) {
            designer.fromJson(json);
            Event.eventEmitter.emit(Event.CANVAS_SELECTED);
        },
        error:function () {
            alert(`加载决策流${file}失败！`);
        }
    });
});