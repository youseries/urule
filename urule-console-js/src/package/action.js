/**
 * Created by jacky on 2016/6/17.
 */
import {formatDate} from '../Utils.js';

export const LOAD_MASTER_COMPLETED='load_master_completed';
export const LOAD_SLAVE_COMPLETE='load_slave_completed';
export const ADD_MASTER='add_master';
export const UPDATE_MASTER='update_master';
export const DEL_MASTER='del_master';
export const ADD_SLAVE='add_slave';
export const UPDATE_SLAVE='update_slave';
export const DEL_SLAVE='del_slave';
export const SAVE='save';
export const SAVE_COMPLETED='save_completed';


export function save(newVersion,project){
    return {newVersion,project,type:SAVE};
};

export function refreshKnowledgeCache(project,packageId,files) {
    $.ajax({
        url:window._server+'/packageeditor/refreshKnowledgeCache',
        data:{project,packageId,files},
        type:'POST',
        success:function (data) {
            const info=data.clientInfo;
            if(info){
                bootbox.confirm(`<h3 class="text-danger">发布操作成功！</h3>项目[${project}]中配置了如下客户端：<div class="well" style="margin: 5px 0px 5px 0px">${info}</div>是否将此次更新的知识包推送到这些客户端中？`,function (result) {
                    if(!result){
                        return;
                    }
                    const componentEvent=window.parent.componentEvent;
                    componentEvent.eventEmitter.emit(componentEvent.SHOW_LOADING);
                    $.ajax({
                        url:window._server+'/packageeditor/pushKnowledgePackageToClients',
                        data:{project,packageId},
                        type:'POST',
                        success:function (result) {
                            componentEvent.eventEmitter.emit(componentEvent.HIDE_LOADING);
                            bootbox.alert(`<div class="well" style="margin: 5px 0px 5px 0px">${result.info}</div>`);
                        },
                        error:function () {
                            componentEvent.eventEmitter.emit(componentEvent.HIDE_LOADING);
                            alert('推送到客户端操作失败！');
                        }
                    });
                })
            }else{
                bootbox.alert(`刷新知识包[${packageId}]操作成功!`);
            }
        },
        error:function () {
            alert('发布知识包失败！');
        }
    });
};

export function saveData(data,newVersion,project) {
    let xml='<?xml version="1.0" encoding="utf-8"?>';
    xml+='<res-packages>';
    let errorInfo='';
    data.forEach((p,index)=>{
        xml+="<res-package id='"+p.id+"' name='"+p.name+"' create_date='"+formatDate(p.createDate,'yyyy-MM-dd HH:mm:ss')+"'>";
        var resourceItems=p.resourceItems;
        resourceItems.forEach((item,i)=>{
            xml+="<res-package-item  name='"+item.name+"' path='"+item.path+"' version='"+item.version+"'/>";
        });
        xml+='</res-package>';
    });
    xml+='</res-packages>';
    xml=encodeURIComponent(xml);
    $.ajax({
        url:window._server+'/packageeditor/saveResourcePackages',
        type:'POST',
        data:{xml,project,newVersion},
        success:function () {
            bootbox.alert('保存成功!')
        },
        error:function (req) {
            if(req.status===401){
                alert("权限不足，不能进行此操作.");
            }else{
                alert('服务端错误，操作失败!');
            }
        }
    });
    return {type:SAVE_COMPLETED};
};
export function addMaster(data) {
    return {type:ADD_MASTER,data};
};

export function updateMaster(data) {
    return {type:UPDATE_MASTER,data};
};

export function deleteMaster(rowIndex) {
    return {rowIndex,type:DEL_MASTER};
};

export function deleteSlave(rowIndex) {
    return {rowIndex,type:DEL_SLAVE};
};

export function addSlave(data) {
    return {type:ADD_SLAVE,data};
};
export function updateSlave(data) {
    return {type:UPDATE_SLAVE,data};
};

export function loadMasterData(project) {
    return function (dispatch) {
        var url=window._server+"/packageeditor/loadPackages";
        $.ajax({
            url,
            type:'POST',
            data:{project},
            success:function (data) {
                dispatch({type:LOAD_MASTER_COMPLETED,masterData:data});
            },
            error:function () {
                alert("加载数据失败.");
            }
        });
    }
};

export function loadSimulatorCategoryData(files,callback){
    var url=window._server+"/packageeditor/loadForTestVariableCategories";
    $.ajax({
        url,
        type:'POST',
        data:{files},
        success:function(data){
            buildSimulatorVariableEditorType(data);
            callback(data);
        },
        error:function(){
            const ce=window.parent.componentEvent;
            ce.eventEmitter.emit(ce.HIDE_LOADING);
            alert("加载文件["+files+"]失败.");
        }
    })
};

export function loadFlows(files,callback){
    var url=window._server+"/packageeditor/loadFlows";
    $.ajax({
        url,
        data:{files},
        type:'POST',
        success:function(data){
            callback(data);
        },
        error:function(){
            const ce=window.parent.componentEvent;
            ce.eventEmitter.emit(ce.HIDE_LOADING);
            alert('加载决策流信息失败.');
        }
    });
};


export function loadSlaveData(masterData) {
    return {type:LOAD_SLAVE_COMPLETE,masterRowData:masterData};
};

export function doTest(files,data,callback){
    var url=window._server+"/packageeditor/doTest";
    $.ajax({
        url,
        type:'POST',
        data:{files,data:JSON.stringify(data)},
        success:function(result){
            callback(result);
        },
        error:function(response){
            const ce=window.parent.componentEvent;
            ce.eventEmitter.emit(ce.HIDE_LOADING);
            if(response && response.responseText){
                bootbox.alert("<span style='color: red'>服务端错误："+response.responseText+"</span>");
            }else{
                bootbox.alert("<span style='color: red'>服务端出错</span>");
            }
        }
    })
};

export function doBatchTest(files,callback){
    var url=window._server+'/packageeditor/doBatchTest';
    $.ajax({
        url,
        type:'POST',
        data:{files},
        success:function(result){
            callback(result);
            const ce=window.parent.componentEvent;
            ce.eventEmitter.emit(ce.HIDE_LOADING);
        },
        error:function(){
            const ce=window.parent.componentEvent;
            ce.eventEmitter.emit(ce.HIDE_LOADING);
            alert('批量测试操作失败.');
        }
    })
};

export function testFlow(files,data,flowId,callback){
    var url=window._server+"/packageeditor/doTest";
    $.ajax({
        url,
        type:'POST',
        data:{files,flowId,data:JSON.stringify(data)},
        success:function(result){
            callback(result);
        },
        error:function(){
            const ce=window.parent.componentEvent;
            ce.eventEmitter.emit(ce.HIDE_LOADING);
            alert('仿真测试操作失败!');
        }
    })
};

export function exportExcelTemplate(files){
    var url=window._server+"/packageeditor/exportExcelTemplate";
    window.open(url,'_self');
}

export function buildSimulatorVariableEditorType(data){
    data.forEach((category,index)=>{
       var variables=category.variables;
        if(variables){
            variables.forEach((v,i)=>{
               if(v.type==='Integer' || v.type==='Double' || v.type==='Long' || v.type==='Float' || v.type==='BigDecimal'){
                   v._editorType='number';
               }else if(v.type==='Boolean'){
                   v._editorType='boolean';
               }else if(v.type==='Date'){
                   v._editorType='date';
               }else if(v.type==='List' || v.type==='Set'){
                   v._editorType='list';
               }else{
                   v._editorType='string';
               }
            });
        }
    });
};