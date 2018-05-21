/**
 * Created by Jacky.gao on 2016/8/31.
 */
export const MASTER_LOADED='master_loaded';
export const SLAVE_LOADED='slave_loaded';
export const PERMISSION_CHANGE="permission_change";

export function loadMasterData() {
    return function (dispatch) {
        const url=window._server+"/permission/loadResourceSecurityConfigs";
        $.ajax({
            url,
            success:function (data) {
                dispatch({type:MASTER_LOADED,data});
            },
            error:function (response) {
                if(response && response.responseText){
                    bootbox.alert("<span style='color: red'>加载权限信息失败,服务端错误："+response.responseText+"</span>");
                }else{
                    bootbox.alert("<span style='color: red'>加载权限信息失败,服务端出错</span>");
                }
            }
        });
    }
};
export function loadSlave(masterRowData) {
    return function (dispatch) {
        return dispatch({type:SLAVE_LOADED,data:masterRowData.projectConfigs});
    }
};
export function save(data) {
    let xml="<?xml version=\"1.0\" encoding=\"utf-8\"?><user-permission>";
    for(let item of data){
        xml+=`<user-permission username="${item.username}">`;
        let projectConfigs=item.projectConfigs || [];
        for(let config of projectConfigs){
            if(!config.project || !config.readProject){
                continue;
            }
            xml+=`<project-config project="${config.project}" read-project="${config.readProject}" 
                read-package="${config.readPackage}" write-package="${config.writePackage}" 
                read-variable-file="${config.readVariableFile}" write-variable-file="${config.writeVariableFile}" 
                read-parameter-file="${config.readParameterFile}" write-parameter-file="${config.writeParameterFile}" 
                read-constant-file="${config.readConstantFile}" write-constant-file="${config.writeConstantFile}" 
                read-action-file="${config.readActionFile}" write-action-file="${config.writeActionFile}" 
                read-rule-file="${config.readRuleFile}" write-rule-file="${config.writeRuleFile}" 
                read-scorecard-file="${config.readScorecardFile}" write-scorecard-file="${config.writeScorecardFile}" 
                read-decision-table-file="${config.readDecisionTableFile}" write-decision-table-file="${config.writeDecisionTableFile}" 
                read-decision-tree-file="${config.readDecisionTreeFile}" write-decision-tree-file="${config.writeDecisionTreeFile}" 
                read-flow-file="${config.readFlowFile}" write-flow-file="${config.writeFlowFile}" 
                />`;
        }
        xml+="</user-permission>";
    }
    xml+="</user-permission>";
    xml=encodeURIComponent(xml);
    const url=window._server+"/permission/saveResourceSecurityConfigs";
    $.ajax({
        url,
        type:'POST',
        data:{content:xml},
        success:function () {
            bootbox.alert('保存成功');
        },
        error:function (response) {
            if(response.status===401){
                bootbox.alert("权限不足，不能进行此操作.");
            }else{
                if(response && response.responseText){
                    bootbox.alert("<span style='color: red'>服务端错误："+response.responseText+"</span>");
                }else{
                    bootbox.alert("<span style='color: red'>服务端出错</span>");
                }
            }
        }
    });
};