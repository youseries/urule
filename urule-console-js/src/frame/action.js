/**
 * Created by Jacky.gao on 2016/5/23.
 */
import Styles from '../Styles.js';
import * as event from './event.js';
import * as componentEvent from '../components/componentEvent.js';

export const ADD='add';
export const DEL='del';
export const UPDATE='upload';
export const LOAD_END='load_end';
export const FILE_RENAME='file_rename';
export const CREATE_NEW_PROJECT='create_new_project';
export const CREATE_NEW_FILE='create_new_file';

export function createNewFile(newFileName,fileType,parentNodeData){
    return function (dispatch) {
        const url=window._server+'/frame/createFile';
        const fileName=newFileName+"."+fileType;
        const path=parentNodeData.fullPath+"/"+fileName;
        $.ajax({
            url,
            data:{path:encodeURI(parentNodeData.fullPath+"/"+fileName),type:fileType},
            type:'POST',
            success:function (newFileInfo) {
                const newFileData={
                    id:newFileInfo.id,
                    name:fileName,
                    type:newFileInfo.type,
                    fullPath:path,
                    contextMenu:buildFileContextMenu()
                };
                buildData(newFileData,1);
                dispatch({
                    parentNodeData,
                    newFileData,
                    type:CREATE_NEW_FILE
                });
                const targetURL=window._server+newFileData.editorPath+"?file="+newFileData.fullPath;
                componentEvent.eventEmitter.emit(componentEvent.TREE_NODE_CLICK,{
                    id:newFileData.id,
                    name:newFileData.name,
                    path:targetURL,
                    fullPath:path,
                    active:true
                });
                event.eventEmitter.emit(event.EXPAND_TREE_NODE,parentNodeData);
                event.eventEmitter.emit(event.CLOSE_CREATE_FILE_DIALOG);
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
    }
};

export function rename(path, newPath) {
    return function (dispatch) {
        const url=window._server+'/frame/fileRename';
        $.ajax({
            url,
            type:'POST',
            data:{path:path,newPath:newPath,classify:window._classify,projectName:window._projectName,types:window._types},
            success:function (data) {
                const rootFile =data.repo.rootFile;
                buildData(rootFile,1);
                dispatch({data:rootFile,type:LOAD_END});
                event.eventEmitter.emit(event.HIDE_RENAME_DIALOG);
                componentEvent.eventEmitter.emit(componentEvent.HIDE_LOADING);
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
    }
};

export function createNewProject(newProjectName,parentNodeData) {
    return function (dispatch) {
        const url=window._server+'/frame/createProject';
        $.ajax({
            url,
            type:'POST',
            data:{newProjectName:newProjectName},
            success:function (newProjectData) {
                buildData(newProjectData,1);
                dispatch({type:CREATE_NEW_PROJECT,newProjectData,parentNodeData});
                event.eventEmitter.emit(event.CLOSE_NEW_PROJECT_DIALOG);
                componentEvent.eventEmitter.emit(componentEvent.HIDE_LOADING);
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
};
export function createNewFolder(newFolderName,parentNodeData) {
    const fullFolderName=parentNodeData.fullPath+'/'+newFolderName;
    return function (dispatch) {
        const url=window._server+'/frame/createFolder';
        $.ajax({
            url,
            type:'POST',
            data:{fullFolderName:fullFolderName,classify:window._classify,projectName:window._projectName,types:window._types},
            success:function (data) {
                const rootFile =data.repo.rootFile;
                buildData(rootFile,1);
                dispatch({data:rootFile,type:LOAD_END});
                event.eventEmitter.emit(event.CLOSE_CREATE_FOLDER_DIALOG);
                componentEvent.eventEmitter.emit(componentEvent.HIDE_LOADING);
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
};

export function fileRename(itemData, newName) {
    return function (dispatch) {
        var fullPath=itemData.fullPath;
        var namePos=fullPath.lastIndexOf(itemData.name);
        var basePath=fullPath.substring(0,namePos);
        var newFullPath=basePath+newName;
        var url=window._server+"/frame/fileRename";
        $.ajax({
            url,
            type:'POST',
            data:{path:fullPath,newPath:newFullPath,classify:window._classify,projectName:window._projectName,types:window._types},
            success:function (data) {
                const pos=newName.indexOf('.');
                if(pos!==-1){
                    itemData.fullPath=newFullPath;
                    itemData.name=newName;
                    dispatch({data:itemData,type:FILE_RENAME});
                }else{
                    const rootFile =data.repo.rootFile;
                    buildData(rootFile,1);
                    dispatch({data:rootFile,type:LOAD_END});
                }
                event.eventEmitter.emit(event.CLOSE_UPDATE_PROJECT_DIALOG);
                componentEvent.eventEmitter.emit(componentEvent.HIDE_LOADING);
            },
            error:function (response) {
                componentEvent.eventEmitter.emit(componentEvent.HIDE_LOADING);
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
    }
};

function moveFile(path, newPath , dispatch) {
    var url=window._server+"/frame/fileRename";
    $.ajax({
        url,
        type:'POST',
        data:{path,newPath,classify:window._classify,projectName:window._projectName,types:window._types},
        success:function (data) {
            const rootFile =data.repo.rootFile;
            buildData(rootFile,1);
            dispatch({data:rootFile,type:LOAD_END});
            event.eventEmitter.emit(event.CLOSE_UPDATE_PROJECT_DIALOG);
            componentEvent.eventEmitter.emit(componentEvent.HIDE_LOADING);
        },
        error:function (response) {
            componentEvent.eventEmitter.emit(componentEvent.HIDE_LOADING);
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
}

export function add(data) {
    return {data,type:ADD};
};

export function del(index) {
    return {index,type:DEL};
};

export function update(index, data) {
    return {index,data,type:UPDATE};
};

export function loadData(classify,projectName,types,searchFileName) {
    if(classify===null || classify==='undefined'){
        classify=true;
    }
    return function (dispatch) {
        const url=window._server+'/frame/loadProjects';
        $.ajax({
            url:url,
            type:'POST',
            data:{classify,projectName,types,searchFileName},
            success:function (data) {
                const {classify,repo}=data;
                const {rootFile,projectNames} = repo;
                event.eventEmitter.emit(event.CHANGE_CLASSIFY,classify);
                if(projectNames && projectNames.length>0){
                    event.eventEmitter.emit(event.PROJECT_LIST_CHANGE,projectNames);
                }
                buildData(rootFile,1);
                dispatch({data:rootFile,type:LOAD_END});
                componentEvent.eventEmitter.emit(componentEvent.HIDE_LOADING);
            },
            error:function (response) {
                componentEvent.eventEmitter.emit(componentEvent.HIDE_LOADING);
                if(response && response.responseText){
                    bootbox.alert("<span style='color: red'>加载数据失败,服务端错误："+response.responseText+"</span>");
                }else{
                    bootbox.alert("<span style='color: red'>加载数据失败,服务端出错</span>");
                }
            }
        });
    }
};

export function buildType(fileType) {
    let pos=fileType.indexOf(':');
    if(pos>-1){
        fileType=fileType.substring(0,pos);
    }
    let type;
    switch (fileType){
        case 'vl.xml':
            type="变量库";
            break;
        case 'cl.xml':
            type='常量库';
            break;
        case 'pl.xml':
            type='参数库';
            break;
        case 'al.xml':
            type='动作库';
            break;
        case 'rs.xml':
            type='向导式决策集';
            break;
        case 'ul':
            type='脚本式决策集';
            break;
        case 'dt.xml':
            type='决策表';
            break;
        case 'dts.xml':
            type='脚本式决策表';
            break;
        case 'rl.xml':
            type='决策流';
            break;
        case 'dtree.xml':
            type='决策树';
            break;
        case "sc":
            type="评分卡";
            break;
        case "知识包":
            type='package';
            break;
    }
    if(!type){
        const info="Unknow file type :"+fileType;
        alert(info);
        throw info;
    }
    return type;
}

function buildData(data,level) {
    data._level=level++;
    switch (data.type){
        case "root":
            data._icon=Styles.frameStyle.getRootIcon();
            data._style=Styles.frameStyle.getRootIconStyle();
            data.contextMenu=[
                {
                    name:'创建新项目',
                    icon:'rf rf-createpro',
                    click:function (data) {
                        event.eventEmitter.emit(event.OPEN_NEW_PROJECT_DIALOG,data)
                    }
                },
                {
                    name:'导入项目',
                    icon:'rf rf-import',
                    click:function (e) {
                        event.eventEmitter.emit(event.OPEN_IMPORT_PROJECT_DIALOG);
                    }
                }
            ];
            break;
        case "rule":
            data._icon=Styles.frameStyle.getRuleIcon();
            data._style=Styles.frameStyle.getRuleIconStyle();
            data.contextMenu=buildFileContextMenu();
            data.editorPath="/ruleseteditor";
            break;
        case "project":
            data._icon=Styles.frameStyle.getProjectIcon();
            data._style=Styles.frameStyle.getProjectIconStyle();
            data.contextMenu=[
                {
                    name:'导出项目备份',
                    icon:'rf rf-export',
                    click:function (data,dispatch) {
                        bootbox.confirm("真的要导出项目"+data.name+"的备份文件吗？",function (result) {
                            if(!result){
                                return;
                            }
                            const url=window._server+'/frame/exportProjectBackupFile?path='+encodeURI(encodeURI(data.fullPath));
                            window.open(url,'_blank');
                        });
                    }
                },
                {
                    name:'修改项目名称',
                    icon:'rf rf-rename',
                    click:function (data) {
                        event.eventEmitter.emit(event.OPEN_UPDATE_PROJECT_DIALOG,data);
                    }
                },
                {
                    name:'删除项目',
                    icon:'rf rf-remove',
                    click:function (data,dispatch) {
                        bootbox.confirm("此操作将删除"+data.name+"项目及其下所有文件，你确定要这样做吗？",function (result) {
                            if(!result){
                                return;
                            }
                            fileDelete(data,dispatch);
                        });
                    }
                }
            ];
            break;
        case "resource":
            data._icon=Styles.frameStyle.getResourceIcon();
            data._style=Styles.frameStyle.getResourceIconStyle();
            break;
        case "all":
            data._icon=Styles.frameStyle.getResourceIcon();
            data._style=Styles.frameStyle.getResourceIconStyle();
            data.contextMenu=buildFullContextMenu();
            break;
        case "folder":
            data._icon=Styles.frameStyle.getFolderIcon();
            data._style=Styles.frameStyle.getFolderIconStyle();
            data.contextMenu=buildFullContextMenu(true,data.folderType);
            break;
        case "resourcePackage":
            data._icon=Styles.frameStyle.getResourcePackageIcon();
            data._style=Styles.frameStyle.getResourcePackageIconStyle();
            data.editorPath="/packageeditor";
            break;
        case "lib":
            data._icon=Styles.frameStyle.getLibIcon();
            data._style=Styles.frameStyle.getLibIconStyle();
            data.contextMenu=buildLibContextMenu();
            break;
        case "action":
            data._icon=Styles.frameStyle.getActionIcon();
            data._style=Styles.frameStyle.getActionIconStyle();
            data.contextMenu=buildFileContextMenu();
            data.editorPath="/actioneditor";
            break;
        case "parameter":
            data._icon=Styles.frameStyle.getParameterIcon();
            data._style=Styles.frameStyle.getParameterIconStyle();
            data.contextMenu=buildFileContextMenu();
            data.editorPath="/parametereditor";
            break;
        case "constant":
            data._icon=Styles.frameStyle.getConstantIcon();
            data._style=Styles.frameStyle.getConstantIconStyle();
            data.contextMenu=buildFileContextMenu();
            data.editorPath="/constanteditor";
            break;
        case "variable":
            data._icon=Styles.frameStyle.getVariableIcon();
            data._style=Styles.frameStyle.getVariableIconStyle();
            data.contextMenu=buildFileContextMenu();
            data.editorPath="/variableeditor";
            break;
        case "ruleLib":
            data._icon=Styles.frameStyle.getRuleLibIcon();
            data._style=Styles.frameStyle.getRuleLibIconStyle();
            data.contextMenu=[
                {
                    name:'添加目录',
                    icon:Styles.frameStyle.getFolderIcon(),
                    click:function (data,dispatch) {
                        event.eventEmitter.emit(event.OPEN_CREATE_FOLDER_DIALOG,{nodeData:data})
                    }
                },
                {
                    name:'添加向导式决策集',
                    icon:Styles.frameStyle.getRuleIcon(),
                    click:function () {
                        event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'rs.xml',nodeData:data})
                    }
                },
                {
                    name:'添加脚本式决策集',
                    icon:Styles.frameStyle.getUlIcon(),
                    click:function () {
                        event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'ul',nodeData:data})
                    }
                }
            ];
            break;
        case "decisionTableLib":
            data._icon=Styles.frameStyle.getDecisionTableLibIcon();
            data._style=Styles.frameStyle.getDecisionTableLibIconStyle();
            data.contextMenu=[
                {
                    name:'添加目录',
                    icon:Styles.frameStyle.getFolderIcon(),
                    click:function (data,dispatch) {
                        event.eventEmitter.emit(event.OPEN_CREATE_FOLDER_DIALOG,{nodeData:data})
                    }
                },
                {
                    name:'添加决策表',
                    icon:Styles.frameStyle.getDecisionTableIcon(),
                    click:function () {
                        event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'dt.xml',nodeData:data})
                    }
                }
            ];
            /*
           data.contextMenu.push({
                name:'添加脚本式决策表',
                icon:Styles.frameStyle.getScriptDecisionTableIcon(),
                click:function () {
                    event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'dts.xml',nodeData:data})
                }
            });
            */
            break;
        case "decisionTreeLib":
            data._icon=Styles.frameStyle.getDecisionTreeLibIcon();
            data._style=Styles.frameStyle.getDecisionTreeLibIconStyle();
            data.contextMenu=[
                {
                    name:'添加目录',
                    icon:Styles.frameStyle.getFolderIcon(),
                    click:function (data,dispatch) {
                        event.eventEmitter.emit(event.OPEN_CREATE_FOLDER_DIALOG,{nodeData:data})
                    }
                },
                {
                    name:'添加决策树',
                    icon:Styles.frameStyle.getDecisionTreeIcon(),
                    click:function () {
                        event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'dtree.xml',nodeData:data})
                    }
                }
            ];
            break;
        case "flowLib":
            data._icon=Styles.frameStyle.getFlowLibIcon();
            data._style=Styles.frameStyle.getFlowLibIconStyle();
            data.contextMenu=[
                {
                    name:'添加目录',
                    icon:Styles.frameStyle.getFolderIcon(),
                    click:function (data,dispatch) {
                        event.eventEmitter.emit(event.OPEN_CREATE_FOLDER_DIALOG,{nodeData:data})
                    }
                },
                {
                    name:'添加决策流',
                    icon:Styles.frameStyle.getFlowIcon(),
                    click:function () {
                        event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'rl.xml',nodeData:data})
                    }
                }
            ];
            break;
        case "scorecardLib":
            data._icon=Styles.frameStyle.getScorecardLibIcon();
            data._style=Styles.frameStyle.getScorecardLibIconStyle();
            data.contextMenu=[
                {
                    name:'添加目录',
                    icon:Styles.frameStyle.getFolderIcon(),
                    click:function (data,dispatch) {
                        event.eventEmitter.emit(event.OPEN_CREATE_FOLDER_DIALOG,{nodeData:data})
                    }
                },
                {
                    name:'添加评分卡',
                    icon:Styles.frameStyle.getScorecardIcon(),
                    click:function () {
                        event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'sc',nodeData:data})
                    }
                }
            ];
            break;
        case "ul":
            data._icon=Styles.frameStyle.getUlIcon();
            data._style=Styles.frameStyle.getUlIconStyle();
            let menus=buildFileContextMenu();
            menus.splice(0,1);
            data.contextMenu=menus;
            data.editorPath="/uleditor";
            break;
        case "decisionTable":
            data._icon=Styles.frameStyle.getDecisionTableIcon();
            data._style=Styles.frameStyle.getDecisionTableIconStyle();
            data.contextMenu=buildFileContextMenu();
            data.editorPath="/decisiontableeditor";
            break;
        case "scriptDecisionTable":
            data._icon=Styles.frameStyle.getScriptDecisionTableIcon();
            data._style=Styles.frameStyle.getScriptDecisionTableIconStyle();
            data.contextMenu=buildFileContextMenu();
            data.editorPath="/scriptdecisiontableeditor";
            break;
        case "decisionTree":
            data._icon=Styles.frameStyle.getDecisionTreeIcon();
            data._style=Styles.frameStyle.getDecisionTreeIconStyle();
            data.contextMenu=buildFileContextMenu();
            data.editorPath="/decisiontreeeditor";
            break;
        case "flow":
            data._icon=Styles.frameStyle.getFlowIcon();
            data._style=Styles.frameStyle.getFlowIconStyle();
            data.contextMenu=buildFileContextMenu();
            data.editorPath="/ruleflowdesigner";
            break;
        case "scorecard":
            data._icon=Styles.frameStyle.getScorecardIcon();
            data._style=Styles.frameStyle.getScorecardIconStyle();
            data.contextMenu=buildFileContextMenu();
            data.editorPath="/scorecardeditor";
            break;
    }
    var children=data.children;
    if(children){
        children.forEach((child,index)=>{
            buildData(child,level);
        });
    }
};

function buildLibContextMenu(){
    const menus=[
        {
            name:'添加目录',
            icon:Styles.frameStyle.getFolderIcon(),
            click:function (data,dispatch) {
                event.eventEmitter.emit(event.OPEN_CREATE_FOLDER_DIALOG,{nodeData:data})
            }
        },
        {
            name:'添加变量库',
            icon:Styles.frameStyle.getVariableIcon(),
            click:function (data,dispatch) {
                event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'vl.xml',nodeData:data})
            }
        },
        {
            name:'添加常量库',
            icon:Styles.frameStyle.getConstantIcon(),
            click:function (data,dispatch) {
                event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'cl.xml',nodeData:data})
            }
        },
        {
            name:'添加参数库',
            icon:Styles.frameStyle.getParameterIcon(),
            click:function (data,dispatch) {
                event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'pl.xml',nodeData:data})
            }
        },
        {
            name:'添加动作库',
            icon:Styles.frameStyle.getActionIcon(),
            click:function (data,dispatch) {
                event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'al.xml',nodeData:data})
            }
        }
    ];
    return menus;
}

function buildFullContextMenu(isFolder,folderType){
    const menus=[{
        name: isFolder ? '添加子目录' : '添加目录',
        icon:Styles.frameStyle.getFolderIcon(),
        click:function (data,dispatch) {
            event.eventEmitter.emit(event.OPEN_CREATE_FOLDER_DIALOG,{nodeData:data})
        }
    }];
    let addPasteMenuItem=false;
    if(!folderType || folderType==='all' || folderType==='lib'){
        menus.push(
            {
                name:'添加变量库',
                icon:Styles.frameStyle.getVariableIcon(),
                click:function (data,dispatch) {
                    event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'vl.xml',nodeData:data})
                }
            },
            {
                name:'添加常量库',
                icon:Styles.frameStyle.getConstantIcon(),
                click:function (data,dispatch) {
                    event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'cl.xml',nodeData:data})
                }
            },
            {
                name:'添加参数库',
                icon:Styles.frameStyle.getParameterIcon(),
                click:function (data,dispatch) {
                    event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'pl.xml',nodeData:data})
                }
            },
            {
                name:'添加动作库',
                icon:Styles.frameStyle.getActionIcon(),
                click:function (data,dispatch) {
                    event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'al.xml',nodeData:data})
                }
            }
        );
        if(!addPasteMenuItem){
            menus.push(buildPasteMenuItem());
            addPasteMenuItem=true;
        }
    }
    if(!folderType || folderType==='all' || folderType==='ruleLib'){
        menus.push(
            {
                name:'添加向导式决策集',
                icon:Styles.frameStyle.getRuleIcon(),
                click:function (data,dispatch) {
                    event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'rs.xml',nodeData:data})
                }
            },
            {
                name:'添加脚本式决策集',
                icon:Styles.frameStyle.getUlIcon(),
                click:function (data,dispatch) {
                    event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'ul',nodeData:data})
                }
            }
        );
        if(!addPasteMenuItem){
            menus.push(buildPasteMenuItem());
            addPasteMenuItem=true;
        }
    }
    if(!folderType || folderType==='all' || folderType==='decisionTableLib'){
        menus.push(...[
            {
                name:'添加决策表',
                icon:Styles.frameStyle.getDecisionTableIcon(),
                click:function (data,dispatch) {
                    event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'dt.xml',nodeData:data})
                }
            }
        ]);
/*        menus.push({
            name:'添加脚本式决策表',
            icon:Styles.frameStyle.getScriptDecisionTableIcon(),
            click:function (data,dispatch) {
                event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'dts.xml',nodeData:data})
            }
        });*/
        if(!addPasteMenuItem){
            menus.push(buildPasteMenuItem());
            addPasteMenuItem=true;
        }
    }
    if(!folderType || folderType==='all' || folderType==='decisionTreeLib'){
        menus.push({
            name:'添加决策树',
            icon:Styles.frameStyle.getDecisionTreeIcon(),
            click:function (data,dispatch) {
                event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'dtree.xml',nodeData:data})
            }
        });
        if(!addPasteMenuItem){
            menus.push(buildPasteMenuItem());
            addPasteMenuItem=true;
        }
    }
    if(!folderType  || folderType==='all'|| folderType==='scorecardLib'){
        menus.push({
            name:'添加评分卡',
            icon:Styles.frameStyle.getScorecardIcon(),
            click:function (data,dispatch) {
                event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'sc',nodeData:data})
            }
        });
    }
    if(!folderType || folderType==='all' || folderType==='flowLib'){
        menus.push({
            name:'添加决策流',
            icon:Styles.frameStyle.getFlowIcon(),
            click:function (data,dispatch) {
                event.eventEmitter.emit(event.OPEN_CREATE_FILE_DIALOG,{fileType:'rl.xml',nodeData:data})
            }
        });
        if(!addPasteMenuItem){
            menus.push(buildPasteMenuItem());
            addPasteMenuItem=true;
        }
    }
    if(isFolder){
        menus.push(
            {
                name:'删除',
                icon:'rf rf-remove',
                click:function (data,dispatch) {
                    bootbox.confirm("删除目录["+data.name+"],将会同时删除其下所有子目录及文件，确认吗？",function (result) {
                        if(!result){
                            return;
                        }
                        fileDelete(data,dispatch,true);
                    });
                }
            },
            {
                name:'修改目录名',
                icon:'rf rf-rename',
                click:function (data,dispatch) {
                    event.eventEmitter.emit(event.SHOW_RENAME_DIALOG,data);
                }
            },
            {
                name:'锁定目录',
                icon:'rf rf-lock',
                click:function (data,dispatch) {
                    lockFile(data.fullPath,dispatch);
                }
            },
            {
                name:'解锁目录',
                icon:'rf rf-unlock',
                click:function (data,dispatch) {
                    unlockFile(data.fullPath,dispatch);
                }
            }
        );
        if(!addPasteMenuItem){
            menus.push(buildPasteMenuItem());
            addPasteMenuItem=true;
        }
    }
    return menus;
};

function buildPasteMenuItem() {
    return {
        name:'粘贴文件',
        icon:'rf rf-paste',
        click:function (data,dispatch) {
            let sourceFileData=window.___cutFileData,copy=false;
            if(!sourceFileData){
                sourceFileData=window.___copyFileData;
                copy=true;
            }
            if(!sourceFileData){
                bootbox.alert("没有文件可供粘贴！");
                return;
            }
            const newDir=data.fullPath;
            const newFullPath=newDir+"/"+sourceFileData.name,oldFullPath=sourceFileData.fullPath;
            if(oldFullPath===newFullPath){
                bootbox.alert("目录未改变，不能进行此操作！");
                return;
            }
            let info="真的要移动文件【"+sourceFileData.name+"】到【"+newDir+"】目录吗？";
            if(copy){
                info="真的要复制文件【"+sourceFileData.name+"】到【"+newDir+"】目录吗？";
            }
            bootbox.confirm(info,function (result) {
                if(!result){
                    return;
                }
                window.___cutFileData=null;
                window.___copyFileData=null;
                if(!copy){
                    moveFile(oldFullPath,newFullPath,dispatch);
                }else{
                    $.ajax({
                        url:window._server+'/frame/copyFile',
                        type:'POST',
                        data:{newFullPath,oldFullPath},
                        success:function (data) {
                            const rootFile =data.repo.rootFile;
                            buildData(rootFile,1);
                            dispatch({data:rootFile,type:LOAD_END});
                        },
                        error:function (response) {
                            if(response && response.responseText){
                                bootbox.alert("<span style='color: red'>复制文件操作失败,服务端错误："+response.responseText+"</span>");
                            }else{
                                bootbox.alert("<span style='color: red'>复制文件操作失败,服务端出错</span>");
                            }
                        }
                    });
                }
            });
        }
    };
}

function buildFileContextMenu() {
    return [
        {
            name:'查看源码',
            icon:'rf rf-code',
            click:function (data,dispatch) {
                seeFileSource(data);
            }
        },
        {
            name:'查看版本信息',
            icon:'rf rf-version',
            click:function (data,dispatch) {
                seeFileVersions(data);
            }
        },
        {
            name:'删除文件',
            icon:'rf rf-remove',
            click:function (data,dispatch) {
                bootbox.confirm("真的要删除["+data.name+"]文件吗？",function (result) {
                    if(!result){
                        return;
                    }
                    fileDelete(data,dispatch);
                });
            }
        },
        {
            name:'修改文件名',
            icon:'rf rf-rename',
            click:function (data,dispatch) {
                event.eventEmitter.emit(event.SHOW_RENAME_DIALOG,data);
            }
        },
        {
            name:'复制文件',
            icon:'rf rf-copy',
            click:function (data,dispatch) {
                window.___copyFileData=data;
                window.___cutFileData=null;
            }
        },
        {
            name:'剪切文件',
            icon:'rf rf-cut',
            click:function (data,dispatch) {
                window.___cutFileData=data;
                window.___copyFileData=null;
            }
        },
        {
            name:'锁定文件',
            icon:'rf rf-lock',
            click:function (data,dispatch) {
                lockFile(data.fullPath,dispatch);
            }
        },
        {
            name:'解锁文件',
            icon:'rf rf-unlock',
            click:function (data,dispatch) {
                unlockFile(data.fullPath,dispatch);
            }
        }
    ];
};

export function lockFile(file,dispatch){
    componentEvent.eventEmitter.emit(componentEvent.SHOW_LOADING);
    var url=window._server+"/frame/lockFile";
    $.ajax({
        url,
        type:"POST",
        data:{file},
        success:function (data) {
            const rootFile =data.repo.rootFile;
            buildData(rootFile,1);
            dispatch({data:rootFile,type:LOAD_END});
            componentEvent.eventEmitter.emit(componentEvent.HIDE_LOADING);
            bootbox.alert('锁定成功!');
        },
        error:function (response) {
            componentEvent.eventEmitter.emit(componentEvent.HIDE_LOADING);
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

export function unlockFile(file,dispatch){
    componentEvent.eventEmitter.emit(componentEvent.SHOW_LOADING);
    var url=window._server+"/frame/unlockFile";
    $.ajax({
        url,
        type:"POST",
        data:{file},
        success:function (data) {
            const rootFile =data.repo.rootFile;
            buildData(rootFile,1);
            dispatch({data:rootFile,type:LOAD_END});
            componentEvent.eventEmitter.emit(componentEvent.HIDE_LOADING);
            bootbox.alert('解锁成功!');
        },
        error:function (response) {
            componentEvent.eventEmitter.emit(componentEvent.HIDE_LOADING);
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

export function saveFileSource(file,content){
    content=encodeURIComponent(content);
    var url=window._server+"/common/saveFile";
    $.ajax({
        url,
        type:'POST',
        data:{file,content},
        success:function () {
            bootbox.alert('保存成功!');
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

export function seeFileSource(data) {
    var url=window._server+"/frame/fileSource";
    $.ajax({
        url,
        type:'POST',
        data:{path:data.fullPath},
        success:function (result) {
            event.eventEmitter.emit(event.OPEN_SOURCE_DIALOG,data.fullPath,result.content);
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

function seeFileVersions(data) {
    var url=window._server+"/frame/fileVersions";
    $.ajax({
        url,
        type:'POST',
        data:{path:data.fullPath},
        success:function (list) {
            event.eventEmitter.emit(event.OPEN_FILE_VERSION_DIALOG,{list,data});
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

function fileDelete(item, dispatch,isFolder) {
    componentEvent.eventEmitter.emit(componentEvent.SHOW_LOADING);
    setTimeout(function () {
        var url=window._server+"/frame/deleteFile";
        $.ajax({
            url,
            type:'POST',
            data:{isFolder,path:item.fullPath,classify:window._classify,projectName:window._projectName,types:window._types},
            success:function (data) {
                if(!isFolder){
                    dispatch({data:item,type:DEL});
                }else{
                    const rootFile =data.repo.rootFile;
                    buildData(rootFile,1);
                    dispatch({data:rootFile,type:LOAD_END});
                }
                componentEvent.eventEmitter.emit(componentEvent.HIDE_LOADING);
            },
            error:function (response) {
                componentEvent.eventEmitter.emit(componentEvent.HIDE_LOADING);
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
    },150);
};
