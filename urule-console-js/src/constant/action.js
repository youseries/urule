/**
 * Created by jacky on 2016/6/12.
 */
import {ajaxSave} from '../Utils.js';
export const LOAD_MASTER_COMPLETED='load_master_completed';
export const LOAD_SLAVE_COMPLETE='load_slave_completed';
export const ADD_MASTER='add_master';
export const DEL_MASTER='del_master';
export const ADD_SLAVE='add_slave';
export const DEL_SLAVE='del_slave';
export const SAVE='save';

export function save(newVersion,file){
    return {newVersion,file,type:SAVE};
}

export function saveData(data,newVersion,file) {
    let xml='<?xml version="1.0" encoding="utf-8"?>';
    xml+='<constant-library>';
    let errorInfo='';
    data.forEach((item,index)=>{
        if(!item.name || item.name.length<1){
            errorInfo='常量分类名称不能为空.';
            return false;
        }
        if(!item.label || item.label.length<1){
            errorInfo='常量分类标题不能为空.';
            return false;
        }
        xml+="<category name='"+item.name+"' label='"+item.label+"'>";
        var constants=item.constants;
        if(!constants || constants.length===0){
            errorInfo="常量分类["+item.label+"]下未定义具体的常量信息";
            return false;
        }
        constants.forEach((constant,i)=>{
            if(!constant.name || constant.name.length<1){
                errorInfo='常量名不能为空.';
                return false;
            }
            if(!constant.label || constant.label.length<1){
                errorInfo='常量标题不能为空.';
                return false;
            }
            if(!constant.type || constant.type.length<1){
                errorInfo='常量数据类型不能为空.';
                return false;
            }
            xml+="<constant name='"+constant.name+"' label='"+constant.label+"' type='"+constant.type+"'/>";
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
    xml+='</constant-library>';
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
};

export function addMaster() {
    return {type:ADD_MASTER};
};

export function deleteMaster(rowIndex) {
    return {rowIndex,type:DEL_MASTER};
}

export function deleteSlave(rowIndex) {
    return {rowIndex,type:DEL_SLAVE};
};

export function addSlave() {
    return {type:ADD_SLAVE};
};

export function loadMasterData(files) {
    return function (dispatch) {
        var url=window._server+"/xml";
        $.ajax({
            url,
            type:'POST',
            data:{files},
            success:function (data) {
                dispatch({type:LOAD_MASTER_COMPLETED,masterData:data[0].categories});
            },
            error:function (response) {
                if(response && response.responseText){
                    bootbox.alert("<span style='color: red'>服务端错误："+response.responseText+"</span>");
                }else{
                    bootbox.alert("<span style='color: red'>服务端出错</span>");
                }
            }
        });
    }
};
export function loadSlaveData(masterData) {
    return {type:LOAD_SLAVE_COMPLETE,masterRowData:masterData};
};