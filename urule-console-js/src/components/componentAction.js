/**
 * Created by jacky on 2016/6/10.
 */
import Styles from '../Styles.js';

let __ui_id=1;
export function uniqueID(){
    const id='_ui_'+(__ui_id++);
    return id;
};

export function loadFileVersions(file,callback){
    var url=window._server+'/frame/loadFileVersions';
    $.ajax({
        url,
        data:{file},
        type:'POST',
        success:function(data){
            buildData(data);
            callback(data);
        },
        error:function(){
            alert("加载文件["+file+"]的版本信息失败.");
        }
    });
};

export function loadResourceTreeData(data,callback){
    var url=window._server+'/common/loadResourceTreeData';
    $.ajax({
        url,
        data,
        type:'POST',
        success:function(data){
            buildData(data);
            callback(data);
        },
        error:function(){
            alert('加载资源失败.');
        }
    });
};

function buildData(data) {
    switch (data.type){
        case "root":
            data._icon=Styles.frameStyle.getRootIcon();
            data._style=Styles.frameStyle.getRootIconStyle();
            break;
        case "folder":
            data._icon=Styles.frameStyle.getFolderIcon();
            data._style=Styles.frameStyle.getFolderIconStyle();
            break;
        case "rule":
            data._icon=Styles.frameStyle.getRuleIcon();
            data._style=Styles.frameStyle.getRuleIconStyle();
            data.editorPath="/ruleeditor";
            break;
        case "project":
            data._icon=Styles.frameStyle.getProjectIcon();
            data._style=Styles.frameStyle.getProjectIconStyle();
            break;
        case "resource":
            data._icon=Styles.frameStyle.getResourceIcon();
            data._style=Styles.frameStyle.getResourceIconStyle();
            break;
        case "resourcePackage":
            data._icon=Styles.frameStyle.getResourcePackageIcon();
            data._style=Styles.frameStyle.getResourcePackageIconStyle();
            data.editorPath="/packageeditor";
            break;
        case "lib":
            data._icon=Styles.frameStyle.getLibIcon();
            data._style=Styles.frameStyle.getLibIconStyle();
            break;
        case "action":
            data._icon=Styles.frameStyle.getActionIcon();
            data._style=Styles.frameStyle.getActionIconStyle();
            data.editorPath="/actioneditor";
            break;
        case "parameter":
            data._icon=Styles.frameStyle.getParameterIcon();
            data._style=Styles.frameStyle.getParameterIconStyle();
            data.editorPath="/parametereditor";
            break;
        case "constant":
            data._icon=Styles.frameStyle.getConstantIcon();
            data._style=Styles.frameStyle.getConstantIconStyle();
            data.editorPath="/constanteditor";
            break;
        case "variable":
            data._icon=Styles.frameStyle.getVariableIcon();
            data._style=Styles.frameStyle.getVariableIconStyle();
            data.editorPath="/variableeditor";
            break;
        case "ruleLib":
            data._icon=Styles.frameStyle.getRuleLibIcon();
            data._style=Styles.frameStyle.getRuleLibIconStyle();
            break;
        case "decisionTableLib":
            data._icon=Styles.frameStyle.getDecisionTableLibIcon();
            data._style=Styles.frameStyle.getDecisionTableLibIconStyle();
            break;
        case "decisionTreeLib":
            data._icon=Styles.frameStyle.getDecisionTreeLibIcon();
            data._style=Styles.frameStyle.getDecisionTreeLibIconStyle();
            break;
        case "flowLib":
            data._icon=Styles.frameStyle.getFlowLibIcon();
            data._style=Styles.frameStyle.getFlowLibIconStyle();
            break;
        case "ul":
            data._icon=Styles.frameStyle.getUlIcon();
            data._style=Styles.frameStyle.getUlIconStyle();
            data.editorPath="/uleditor";
            break;
        case "decisionTable":
            data._icon=Styles.frameStyle.getDecisionTableIcon();
            data._style=Styles.frameStyle.getDecisionTableIconStyle();
            data.editorPath="/decisiontableeditor";
            break;
        case "scriptDecisionTable":
            data._icon=Styles.frameStyle.getScriptDecisionTableIcon();
            data._style=Styles.frameStyle.getScriptDecisionTableIconStyle();
            data.editorPath="/scriptdecisiontableeditor";
            break;
        case "decisionTree":
            data._icon=Styles.frameStyle.getDecisionTreeIcon();
            data._style=Styles.frameStyle.getDecisionTreeIconStyle();
            data.editorPath="/decisiontreeditor";
            break;
        case "flow":
            data._icon=Styles.frameStyle.getFlowIcon();
            data._style=Styles.frameStyle.getFlowIconStyle();
            data.editorPath="/floweditor";
            break;
        case "scorecard":
            data._icon=Styles.frameStyle.getScorecardIcon();
            data._style=Styles.frameStyle.getScorecardIconStyle();
            data.editorPath="/scorecardeditor";
            break;
    }
    var children=data.children;
    if(children){
        children.forEach((child,index)=>{
            buildData(child);
        });
    }
};