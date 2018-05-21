/**
 * Created by Jacky.gao on 2016/9/18.
 */
import ScoreCardTable from './ScoreCardTable.js';
import '../../node_modules/bootstrap/dist/css/bootstrap.css';
import '../css/iconfont.css';
import './scorecard.css';
import '../editor/context.standalone.css';
import '../editor/urule/ruleset.css';
import React from 'react';
import ReactDOM from 'react-dom';
import '../Remark.js';
import '../editor/common/jquery.utils.js';
import '../editor/common/URule.js';
import '../editor/common/contextMenu.js';
import '../editor/Math.uuid.js';
import '../editor/common/Context.js';
import '../editor/decisiontable/CellCondition.js';
import '../editor/decisiontable/Condition.js';
import '../editor/decisiontable/Join.js';
import '../editor/decisiontable/Connection.js';

import '../editor/common/ComparisonOperator.js';
import '../editor/common/ComplexArithmetic.js';
import '../editor/common/VariableValue.js';
import '../editor/common/ConstantValue.js';

import '../editor/urule/SimpleArithmetic.js';
import '../editor/common/InputType.js';
import '../editor/common/NextType.js';
import '../editor/common/Paren.js';
import '../editor/common/MethodParameter.js';
import '../editor/common/MethodAction.js';
import '../editor/common/ParameterValue.js';
import '../editor/common/MethodValue.js';
import '../editor/common/FunctionProperty.js';
import '../editor/common/FunctionParameter.js';
import '../editor/common/FunctionValue.js';
import '../editor/common/SimpleValue.js';

import '../editor/urule/ConfigActionDialog.js';
import '../editor/urule/ConfigConstantDialog.js';
import '../editor/urule/ConfigParameterDialog.js';
import '../editor/urule/ConfigVariableDialog.js';
import '../editor/urule/RuleProperty.js';

import {ajaxSave,getParameter} from '../Utils.js';

import KnowledgeTreeDialog from '../components/dialog/component/KnowledgeTreeDialog.jsx';

$(document).ready(function (e) {
    const file=getParameter("file");
    if(!file){
        alert("未指定文件.");
        return;
    }

    const toolbarContainer=$("#toolbarContainer");
    const toolbar=$(`<div class="btn-toolbar" style="border: solid 1px #d0d0d0;padding:5px;margin:3px;border-radius: 5px;background: #fdfdfd"></div>`);
    toolbarContainer.append(toolbar);
    const operationGroup=$(`<div class="btn-group btn-group-sm"></div>`);
    toolbar.append(operationGroup);
    const addAttributeButton=$("<button type='button' class='btn btn-default'><i class='glyphicon glyphicon-plus'></i> 添加属性行</button>");
    operationGroup.append(addAttributeButton);

    const addCustomColButton=$("<button type='button' class='btn btn-default'><i class='glyphicon glyphicon-plus-sign'></i> 添加自定义列</button>");
    operationGroup.append(addCustomColButton);

    var self=this;
    const configGroup=$(`<div class="btn-group btn-group-sm"></div>`);
    toolbar.append(configGroup);
    const variableButton=$(`<button type="button" class="btn btn-default"><i class="rf rf-variable"></i> 变量库</button>`);
    configGroup.append(variableButton);
    variableButton.click(function () {
        if(!self.configVarDialog){
            self.configVarDialog=new urule.ConfigVariableDialog(self);
        }
        self.configVarDialog.open();
    });

    const constButton=$(`<button type="button" class="btn btn-default"><i class="rf rf-constant"></i> 常量库</button>`);
    configGroup.append(constButton);
    constButton.click(function () {
        if(!self.configConstantDialog){
            self.configConstantDialog=new urule.ConfigConstantDialog(self);
        }
        self.configConstantDialog.open();
    });

    const actionButton=$(`<button type="button" class="btn btn-default"><i class="rf rf-action"></i> 动作库</button>`);
    configGroup.append(actionButton);
    actionButton.click(function () {
        if(!self.configActionDialog){
            self.configActionDialog=new urule.ConfigActionDialog(self);
        }
        self.configActionDialog.open();
    });

    const parameterButton=$(` <button type="button" class="btn btn-default"><i class="rf rf-parameter"></i> 参数库</button>`);
    configGroup.append(parameterButton);
    parameterButton.click(function () {
        if(!self.configParameterDialog){
            self.configParameterDialog=new urule.ConfigParameterDialog(self);
        }
        self.configParameterDialog.open();
    });

    const saveGroup=$(`<div class="btn-group btn-group-sm"></div>`);
    toolbar.append(saveGroup);
    const saveButton=$(`<button type="button" class="btn btn-default disabled"><i class="rf rf-save"></i> 保存</button>`);
    saveGroup.append(saveButton);
    const saveVersionButton=$(`<button type="button" class="btn btn-default disabled"><i class="rf rf-savenewversion"></i> 保存新版本</button>`);
    saveGroup.append(saveVersionButton);

    window._setDirty=function(){
        if(self._dirty){
            return;
        }
        self._dirty=true;
        window._dirty=true;
        saveButton.html("<i class='rf rf-save'></i> *保存");
        saveButton.removeClass("disabled");
        saveVersionButton.html("<i class='rf rf-savenewversion'></i> *保存新版本");
        saveVersionButton.removeClass("disabled");
    };

    function cancelDirty(){
        if(!self._dirty){
            return;
        }
        self._dirty=false;
        window._dirty=false;
        saveButton.html("<i class='rf rf-save'></i> 保存");
        saveButton.addClass("disabled");
        saveVersionButton.html("<i class='rf rf-savenewversion'></i> 保存新版本");
        saveVersionButton.addClass("disabled");
    };

    addAttributeButton.click(function () {
        cardTable.addAttributeRow();
    });
    addCustomColButton.click(function () {
        cardTable.addCustomCol();
    });
    const cardTable=new ScoreCardTable({
        container:$("#tableContainer"),
        headers:[]
    });

    function save(newVersion) {
        try{
            let content=cardTable.toXml();
            content=encodeURIComponent(content);
            const url=window._server+"/common/saveFile";
            if(newVersion){
                bootbox.prompt("请输入新版本描述.",function (versionComment) {
                    if(!versionComment){
                        return;
                    }
                    ajaxSave(url,{content,file,newVersion,versionComment},function () {
                        cancelDirty();
                    });

                });
            }else{
                ajaxSave(url,{content,file,newVersion},function () {
                    cancelDirty();
                });
            }
        }catch(error){
            bootbox.alert(error.message || error);
            //throw error;
        }
    };

    saveButton.click(function () {
        save(false);
    });
    saveVersionButton.click(function () {
        save(true);
    });
    ReactDOM.render(
        <KnowledgeTreeDialog/>,
        document.getElementById("dialogContainer")
    );
    $.ajax({
        url:window._server+"/common/loadXml",
        type:"POST",
        data:{files:file},
        success:function (data) {
            const card=data[0];
            cardTable.init(card);
            var libraries=card.libraries;
            if(libraries){
                for(var i=0;i<libraries.length;i++){
                    var lib=libraries[i];
                    var type=lib["type"];
                    var path=lib["path"];
                    switch(type){
                        case "Constant":
                            constantLibraries.push(path);
                            break;
                        case "Action":
                            actionLibraries.push(path);
                            break;
                        case "Variable":
                            variableLibraries.push(path);
                            break;
                        case "Parameter":
                            parameterLibraries.push(path);
                            break;
                    }
                    refreshActionLibraries();
                    refreshConstantLibraries();
                    refreshVariableLibraries();
                    refreshParameterLibraries();
                    refreshFunctionLibraries();
                }
                cancelDirty();
            }
        },
        error:function (response) {
            if(response && response.responseText){
                bootbox.alert("<span style='color: red'>加载数据失败,服务端错误："+response.responseText+"</span>");
            }else{
                bootbox.alert("<span style='color: red'>加载数据失败,服务端出错</span>");
            }
        }
    });
});