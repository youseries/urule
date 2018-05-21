/**
 * Created by Jacky.gao on 2016/6/3.
 */
import {ajaxSave} from '../Utils.js';
export const LOAD_MASTER_COMPLETED='load_master_completed';
export const LOAD_SLAVE_COMPLETE='load_slave_completed';
export const ADD_MASTER='add_master';
export const DEL_MASTER='del_master';
export const ADD_SLAVE='add_slave';
export const DEL_SLAVE='del_slave';
export const GENERATED_FIELDS='generated_fields';
export const IMPORT_FIELDS='IMPORT_FIELDS';
export const SAVE='save';
export const SAVE_COMPLETED='save_completed';

export function save(newVersion,file){
    return {newVersion,file,type:SAVE};
};

export function saveData(data,newVersion,file) {
    let xml='<?xml version="1.0" encoding="utf-8"?>';
    xml+='<variable-library>';
    let errorInfo='';
    data.forEach((item,index)=>{
        if(!item.name || item.name.length<1){
            errorInfo='变量分类名称不能为空.';
            return false;
        }
        if(!item.clazz || item.clazz.length<1){
            errorInfo='变量类路径不能为空.';
            return false;
        }
        xml+="<category name='"+item.name+"' type='"+item.type+"' clazz='"+item.clazz+"'>";
        var variables=item.variables;
        if(!variables || variables.length==0){
            errorInfo="变量分类["+item.name+"]下未定义具体变量信息.";
            return false;
        }
        variables.forEach((variable,i)=>{
            if(!variable.name || variable.name.length<1){
                errorInfo='变量名不能为空.';
                return false;
            }
            if(!variable.label || variable.label.length<1){
                errorInfo='变量标题不能为空.';
                return false;
            }
            if(!variable.type || variable.type.length<1){
                errorInfo='变量数据类型不能为空.';
                return false;
            }
           xml+="<var act='InOut' name='"+variable.name+"' label='"+variable.label+"' type='"+variable.type+"'/>";
        });
        if(errorInfo.length>1){
            return false;
        }
        xml+='</category>';
    });
    if(errorInfo.length>1){
        bootbox.alert(errorInfo+',不能保存！');
        return;
    }
    xml+='</variable-library>';
    xml=encodeURIComponent(xml);
    let postData={content:xml,file,newVersion};
    const url=window._server+'/common/saveFile';
    if(newVersion){
        bootbox.prompt("请输入新版本描述.",function (versionComment) {
            if(!versionComment){
                return;
            }
            postData.versionComment=versionComment;
            ajaxSave(url,postData,function () {
                bootbox.alert('保存成功!');
            })
        });
    }else{
        ajaxSave(url,postData,function () {
            bootbox.alert('保存成功!');
        })
    }
    return {type:SAVE_COMPLETED};
};

export function importFields(rowIndex,jsonResult) {
    return {rowIndex,jsonResult,type:IMPORT_FIELDS};
};

export function addMaster() {
    return {type:ADD_MASTER};
};

export function deleteMaster(rowIndex) {
    return {rowIndex,type:DEL_MASTER};
};

export function deleteSlave(rowIndex) {
    return {rowIndex,type:DEL_SLAVE};
};

export function addSlave() {
    return {type:ADD_SLAVE};
};

export function generateFields(rowIndex,clazz){
    return function (dispatch){
        let url=window._server+'/variableeditor/generateFields';
        $.ajax({
            url,
            type:'POST',
            data:{clazz},
            success:function(result){
                dispatch({rowIndex,variables:result,type:GENERATED_FIELDS});
            },
            error:function(response){
                if(response && response.responseText){
                    bootbox.alert("<span style='color: red'>生成字段失败,服务端错误："+response.responseText+"</span>");
                }else{
                    bootbox.alert("<span style='color: red'>生成字段失败,服务端出错</span>");
                }
            }
        })
    }
};

export function loadMasterData(files) {
    return function (dispatch) {
        var url=window._server+"/xml";
        $.ajax({
            url,
            type:'POST',
            data:{files},
            success:function (data) {
                dispatch({type:LOAD_MASTER_COMPLETED,masterData:data[0]});
            },
            error:function (response) {
                if(response && response.responseText){
                    bootbox.alert("<span style='color: red'>加载数据失败,服务端错误："+response.responseText+"</span>");
                }else{
                    bootbox.alert("<span style='color: red'>加载数据失败,服务端出错</span>");
                }
            }
        });
    }
};
export function loadSlaveData(masterData) {
    return {type:LOAD_SLAVE_COMPLETE,masterRowData:masterData};
};