/**
 * Created by Jacky.gao on 2016/8/2.
 */
import '../../../node_modules/bootstrap/dist/css/bootstrap.css';
import '../../../node_modules/codemirror/lib/codemirror.css';
import '../../../node_modules/codemirror/addon/hint/show-hint.css';
import '../../../node_modules/codemirror/addon/lint/lint.css';
import './ul.css';
import React from 'react';
import ReactDOM from 'react-dom';
import {MsgBox} from 'flowdesigner';
import CodeMirror from 'codemirror';
import '../../../node_modules/codemirror/addon/mode/simple.js';
import '../../../node_modules/codemirror/addon/hint/show-hint.js';
import '../../../node_modules/codemirror/addon/lint/lint.js';
import './urule_mode.js';
import './urule-hint.js';
import './urulemixed.js';
import KnowledgeTreeDialog from '../../components/dialog/component/KnowledgeTreeDialog.jsx';
import {getParameter,ajaxSave} from '../../Utils.js';
import * as event from '../../components/componentEvent.js';

$(document).ready(function () {
    ReactDOM.render(
        <KnowledgeTreeDialog/>,
        document.getElementById('dialogContainer')
    );

    CodeMirror.commands.autocomplete = function(cm) {
        cm.showHint({hint: CodeMirror.hint.urule});
    };
    var codeEditor=document.getElementById("codeEditor");
    window.codeMirror = CodeMirror.fromTextArea(codeEditor, {
        lineNumbers: true,
        mode: "rulemixed",
        extraKeys: {"Alt-/": "autocomplete"},
        gutters: ["CodeMirror-linenumbers", "CodeMirror-lint-markers"],
        lint: {
            getAnnotations:buildScriptLintFunction('Script'),
            async:true
        }
    });
    codeMirror.on("change",function(cm,e){
        var value = cm.getValue();
        if(e.text=="."){
            CodeMirror.commands.autocomplete(codeMirror);
        }
    });
    init();
});

function buildScriptLintFunction(type){
    return function (text, updateLinting, options, editor){
        if(text===''){
            updateLinting(editor,[]);
            return;
        }
        const url=window._server+'/common/scriptValidation';
        $.ajax({
            url,
            type:'POST',
            data:{type,content:text},
            success:function(result){
                if(result){
                    for(let item of result){
                        item.from={line:item.line-1};
                        item.to={line:item.line-1};
                    }
                    updateLinting(editor,result);
                }else{
                    updateLinting(editor,[]);
                }
            },
            error:function(response){
                if(response && response.responseText){
                    bootbox.alert("<span style='color: red'>语法检查操作失败："+response.responseText+"</span>");
                }else{
                    bootbox.alert("<span style='color: red'>语法检查操作失败,服务端出错</span>");
                }
            }
        });
    };
}

function init(){
    var height=$(document).height()-60;
    codeMirror.setSize("100%",height);
    window._dirty=false;
    var file=getParameter("file");
    if(!file || file.length<1){
        alert("当前编辑器未指定具体文件！");
        return;
    }
    var saveButton = `<div class="btn-group btn-group-sm navbar-btn" style="margin:1px;" role="group" aria-label="...">
            <button id="saveButton" type="button" class="btn btn-default navbar-btn" ><i class="rf rf-save"></i> 保存</button>
            <button id="saveButtonNewVersion" type="button" class="btn btn-default navbar-btn" ><i class="rf rf-savenewversion"></i> 保存新版本</button>
     </div>`;
    var toolbar=$(`<nav class="navbar navbar-default" style="margin-bottom: 1px">
        <div>
            <div class="collapse navbar-collapse"> ${saveButton}
                    <div class="btn-group btn-group-sm navbar-btn" style="margin:1px;" role="group" aria-label="...">
                        <button id="addVarButton" type="button" class="btn btn-default"><i class="rf rf-variable"></i> 变量库</button>
                        <button id="addConstantsButton" type="button" class="btn btn-default"><i class="rf rf-constant"></i> 常量库</button>
                        <button id="addActionButton" type="button" class="btn btn-default"><i class="rf rf-action"></i> 动作库</button>
                        <button id="configParameterButton" type="button" class="btn btn-default"><i class="rf rf-parameter"></i> 参数库</button>
                    </div>
                 </div>
            </div>
        </nav>`);
    $("#toolbarContainer").append(toolbar);
    $("#saveButton").click(function(){
        save(file,false);
    });
    $("#saveButtonNewVersion").click(function(){
        save(file,true);
    });

    $("#configParameterButton").click(function(){
        event.eventEmitter.emit(event.OPEN_KNOWLEDGE_TREE_DIALOG,{
            project:window._project,
            fileType:'ParameterLibrary',
            callback:function(file,version){
                let path='jcr:'+file;
                if(version!=='LATEST'){
                    path+=':'+version;
                }
                selectResource('ParameterLibrary',path);
            }
        });
    });
    $("#addVarButton").click(function(){
        event.eventEmitter.emit(event.OPEN_KNOWLEDGE_TREE_DIALOG,{
            project:window._project,
            fileType:'VariableLibrary',
            callback:function(file,version){
                let path='jcr:'+file;
                if(version!=='LATEST'){
                    path+=':'+version;
                }
                selectResource('VariableLibrary',path);
            }
        });
    });
    $("#addConstantsButton").click(function(){
        event.eventEmitter.emit(event.OPEN_KNOWLEDGE_TREE_DIALOG,{
            project:window._project,
            fileType:'ConstantLibrary',
            callback:function(file,version){
                let path='jcr:'+file;
                if(version!=='LATEST'){
                    path+=':'+version;
                }
                selectResource('ConstantLibrary',path);
            }
        });
    });
    $("#addActionButton").click(function(){
        event.eventEmitter.emit(event.OPEN_KNOWLEDGE_TREE_DIALOG,{
            project:window._project,
            fileType:'ActionLibrary',
            callback:function(file,version){
                let path='jcr:'+file;
                if(version!=='LATEST'){
                    path+=':'+version;
                }
                selectResource('ActionLibrary',path);
            }
        });
    });

    window._dirty=false;
    var url=window._server+'/uleditor/loadUL';
    $.ajax({
        url:url,
        type:"POST",
        data:{file},
        error:function(response){
            if(response && response.responseText){
                bootbox.alert("<span style='color: red'>文件加载失败："+response.responseText+"</span>");
            }else{
                bootbox.alert("<span style='color: red'>文件加载失败,服务端出错</span>");
            }
        },
        success:function(data){
            codeMirror.setValue(data);
            $("#saveButton").addClass("disabled");
            $("#saveButtonNewVersion").addClass("disabled");
            codeMirror.on("change",function(){
                setDirty();
            });
            loadResLib();
        }
    });
};

function selectResource(type,res){
    codeMirror.replaceSelection("import"+type+" \""+res+"\";");
    loadResLib();
};

function loadResLib(){
    var file=getParameter("file");
    var content=codeMirror.getValue();
    if(!content || content.length<10){
        MsgBox.alert("请先输入脚本.");
        return;
    }
    var url=window._server+'/uleditor/loadULLibs';
    $.ajax({
        url:url,
        type:"POST",
        data:{content:content},
        error:function(response){
            if(response && response.responseText){
                bootbox.alert("<span style='color: red'>资源库加载失败："+response.responseText+"</span>");
            }else{
                bootbox.alert("<span style='color: red'>资源库加载失败,服务端出错</span>");
            }
        },
        success:function(data){
            codeMirror._library=data;
        }
    });
};
function save(file,newVersion){
    if($("#saveButton").hasClass("disabled")){
        return false;
    }
    var content=codeMirror.getValue();
    content=encodeURIComponent(content);
    let postData={content,file,newVersion};
    const url=window._server+'/common/saveFile';
    if(newVersion){
        bootbox.prompt("请输入新版本描述.",function (versionComment) {
            if(!versionComment){
                return;
            }
            postData.versionComment=versionComment;
            ajaxSave(url,postData,function () {
                cancelDirty();
            })
        });
    }else{
        ajaxSave(url,postData,function () {
            cancelDirty();
        })
    }
};

function setDirty(){
    if(window._dirty){
        return;
    }
    window._dirty=true;
    $("#saveButton").html("<i class='rf rf-save'></i> *保存");
    $("#saveButton").removeClass("disabled");
    $("#saveButtonNewVersion").html("<i class='rf rf-savenewversion'></i> *保存新版本");
    $("#saveButtonNewVersion").removeClass("disabled");
};

function cancelDirty(){
    if(!window._dirty){
        return;
    }
    window._dirty=false;
    $("#saveButton").html("<i class='rf rf-save'></i> 保存");
    $("#saveButton").addClass("disabled");
    $("#saveButtonNewVersion").html("<i class='rf rf-savenewversion'></i> 保存新版本");
    $("#saveButtonNewVersion").addClass("disabled");
};
