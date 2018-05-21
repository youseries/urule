/**
 * Created by jacky on 2016/6/15.
 */
import {ajaxSave} from '../Utils.js';

export const LOAD_MASTER_COMPLETED='load_master_completed';
export const LOAD_SLAVE_COMPLETE='load_slave_completed';
export const LOAD_METHOD_COMPLETED='load_method_data';
export const DO_NOTHING='do_nothing';
export const ADD_MASTER='add_master';
export const DEL_MASTER='del_master';
export const ADD_SLAVE='add_slave';
export const DEL_SLAVE='del_slave';
export const DEL_PARAMETER='del_parameter';
export const ADD_PARAMETER='add_parameter';
export const IMPORT_FIELDS='IMPORT_FIELDS';
export const SAVE='save';
export const SAVE_COMPLETED='save_completed';
export const LOADED_BEAN_METHODS='loaded_bean_methods';


export function save(newVersion,file){
    return {newVersion,file,type:SAVE};
};

export function saveData(data,newVersion,file) {
    let xml='<?xml version="1.0" encoding="utf-8"?>';
    xml+='<action-library>';
    let errorInfo='';
    for(let item of data){
        if(!item.name || item.name.length<1){
            errorInfo='动作名称不能为空.';
            break;
        }
        if(!item.id || item.id.length<1){
            errorInfo='Bean Id不能为空.';
            break;
        }
        xml+="<spring-bean id='"+item.id+"' name='"+item.name+"'>";
        var methods=item.methods;
        if(!methods || methods.length===0){
            errorInfo="动作分类["+item.name+"]下未定义具体的动作方法.";
            break;
        }
        for(let method of methods){
            if(!method.name || method.name.length<1){
                errorInfo='名称不能为空.';
                break;
            }
            if(!method.methodName || method.methodName.length<1){
                errorInfo='方法名不能为空.';
                break;
            }
            xml+="<method name='"+method.name+"' method-name='"+method.methodName+"'>";
            var parameters=method.parameters;
            if(parameters){
                for(let p of parameters){
                    if(!p.name || p.name.length<1){
                        errorInfo='参数名不能为空!';
                        break;
                    }
                    if(!p.type || p.type.length<1){
                        errorInfo='参数类型不能为空!';
                        break;
                    }
                    xml+="<parameter name='"+p.name+"' type='"+p.type+"'/>";
                }
            }
            xml+="</method>";
        }
        if(errorInfo.length>1){
            break;
        }
        xml+='</spring-bean>';
    }
    if(errorInfo.length>1){
        bootbox.alert(errorInfo+',不能保存！');
        return;
    }
    xml+='</action-library>';
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

export function loadBeanMethods(beanId){
    return function(dispatch){
        var url=window._server+'/actioneditor/loadMethods';
        $.ajax({
            url,
            type:'POST',
            data:{beanId},
            success:function(result){
                dispatch({type:LOADED_BEAN_METHODS,result});
            },
            error:function(response){
                if(response && response.responseText){
                    bootbox.alert("<span style='color: red'>服务端错误："+response.responseText+"</span>");
                }else{
                    bootbox.alert("<span style='color: red'>服务端出错</span>");
                }
            }
        });
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

export function deleteParameter(rowIndex){
    return {rowIndex,type:DEL_PARAMETER};
};

export function addSlave(newSlaveData) {
    if(newSlaveData){
        return {type:ADD_SLAVE,newSlaveData};
    }else{
        return {type:ADD_SLAVE};
    }
};

export function addParameter(){
    return {type:ADD_PARAMETER};
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
                    bootbox.alert("<span style='color: red'>服务端错误："+response.responseText+"</span>");
                }else{
                    bootbox.alert("<span style='color: red'>服务端出错</span>");
                }
            }
        });
    }
};

let currentMasterGridRowIndex=null;

export function loadSlaveData(masterData,rowIndex) {
   if(currentMasterGridRowIndex!==null && currentMasterGridRowIndex===rowIndex){
        return {type:DO_NOTHING};
    }
    currentMasterGridRowIndex=rowIndex;
    return {type:LOAD_SLAVE_COMPLETE,masterRowData:masterData};
};

export function loadMethodData(slaveData){
    return {type:LOAD_METHOD_COMPLETED,slaveData};
}