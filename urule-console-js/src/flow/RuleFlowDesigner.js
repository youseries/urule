/**
 * Created by jacky on 2016/7/18.
 */
import {FlowDesigner,MsgBox} from 'flowdesigner';
import BaseNode from './BaseNode.js';
import * as event from '../components/componentEvent.js';
import * as action from '../frame/action.js';
import {Event,Node} from 'flowdesigner';
import * as CONSTANTS from './Constants.js';


export default class RuleFlowDesigner extends FlowDesigner{
    constructor(containerId){
        super(containerId);
        this.importVariableLibraries=[];
        this.importConstantLibraries=[];
        this.importParameterLibraries=[];
        this.importActionLibraries=[];
        this.variableLibraries=[];
        this.constantLibraries=[];
        this.parameterLibraries=[];
        this.actionLibraries=[];
        this.flowId='';
    }
    toXML(){
        if(!this.flowId || this.flowId.length<1){
            MsgBox.alert('决策流ID必须指定!');
            return;
        }
        if(!this.validate()){
            return;
        }
        if(this.toJSON().length<2){
            MsgBox.alert('决策流至少要包含一个开始节点和一个其它类型节点!');
            return;
        }
        let debug=false;
        if(this.debug!==undefined && this.debug!==null){
            debug=this.debug;
        }
        let xml='<?xml version="1.0" encoding="utf-8"?>';
        xml+=`<rule-flow id="${this.flowId}" debug="${debug}">`;
        for(let lib of this.importVariableLibraries){
            xml+=`<import-variable-library path="${lib}"/>`;
        }
        for(let lib of this.importConstantLibraries){
            xml+=`<import-constant-library path="${lib}"/>`;
        }
        for(let lib of this.importParameterLibraries){
            xml+=`<import-parameter-library path="${lib}"/>`;
        }
        for(let lib of this.importActionLibraries){
            xml+=`<import-action-library path="${lib}"/>`;
        }
        for(let figure of this.context.allFigures){
            if(!(figure instanceof BaseNode)){
                continue;
            }
            xml+=figure.toXML();
        }
        xml+='</rule-flow>';
        xml=encodeURIComponent(xml);
        return xml;
    }

    fromJson(json){
        this.flowId=json.id;
        this.debug=json.debug;
        const libs=json.libraries || [];
        for(let lib of libs){
            switch (lib.type){
                case "Variable":
                    this.importVariableLibraries.push(lib.path);
                    break;
                case "Constant":
                    this.importConstantLibraries.push(lib.path);
                    break;
                case "Action":
                    this.importActionLibraries.push(lib.path);
                    break;
                case "Parameter":
                    this.importParameterLibraries.push(lib.path);
                    break;
            }
        }
        if(this.importVariableLibraries.length>0){
            this._refreshLibraries(this.importVariableLibraries,"vl.xml");
        }
        if(this.importConstantLibraries.length>0){
            this._refreshLibraries(this.importConstantLibraries,"cl.xml");
        }
        if(this.importActionLibraries.length>0){
            this._refreshLibraries(this.importActionLibraries,"al.xml");
        }
        if(this.importParameterLibraries.length>0){
            this._refreshLibraries(this.importParameterLibraries,"pl.xml");
        }
        for(let nodeJson of json.nodes){
            nodeJson.w=nodeJson.width;
            nodeJson.h=nodeJson.height;
            switch (nodeJson.type){
                case "Action":
                    nodeJson.type='动作';
                    break;
                case "Script":
                    nodeJson.type='脚本';
                    break;
                case "Decision":
                    nodeJson.type='决策';
                    break;
                case "End":
                    nodeJson.type='结束';
                    break;
                case "Start":
                    nodeJson.type='开始';
                    break;
                case "Rule":
                    nodeJson.type='规则';
                    break;
                case "RulePackage":
                    nodeJson.type='知识包';
                    break;
                case "Fork":
                    nodeJson.type='分支';
                    break;
                case "Join":
                    nodeJson.type='聚合';
                    break;
            }
            const conns=nodeJson.connections || [];
            for(let conn of conns){
                conn.to=conn.toName;
            }
            this.addNode(nodeJson);
        }
        for(let figure of this.context.allFigures){
            if(!(figure instanceof Node)){
                continue;
            }
            figure._buildConnections();
        }
    }

    getPropertiesProducer(){
        const _this=this;
        return function (){
            const g=$('<div></div>');
            const flowIdGroup=$(`<div class="form-group"><label>决策流ID</label></div>`);
            const flowIdText=$(`<input type="text" class="form-control">`);
            flowIdGroup.append(flowIdText);
            const _this=this;
            flowIdText.change(function(){
                _this.flowId=$(this).val();
            });
            flowIdText.val(this.flowId);
            g.append(flowIdGroup);

            const debugGroup=$(`<div class="form-group"><label>允许调试信息输出</label></div>`);
            const debugSelect=$(`<select class="form-control">
                <option value="true">是</option>
                <option value="false">否</option>
            </select>`);
            if(_this.debug){
                debugSelect.val('true');
            }else{
                debugSelect.val('false');
            }
            debugGroup.append(debugSelect);
            debugSelect.change(function(){
                if($(this).val()==='true'){
                    _this.debug=true;
                }else{
                    _this.debug=false;
                }
            });
            g.append(debugGroup);

            const libGroup=$('<div class="form-group"><label>库文件</label></div>');
            const addButton=$(`<span style="float: right;"><button type="button" class="btn btn-info"><i class="glyphicon glyphicon-plus"></i> 添加</button></span>`);
            libGroup.append(addButton);
            addButton.click(function () {
                event.eventEmitter.emit(event.OPEN_KNOWLEDGE_TREE_DIALOG,{
                    project:window._project,
                    forLib:true,
                    callback:function (file,version) {
                        let fullFileName=file+(version==='LATEST' ? '' : ':'+version);
                        const extPos=file.indexOf('.')+1;
                        const extName=file.substring(extPos,file.length);
                        let importLibs=null;
                        if(extName==='vl.xml'){
                            importLibs=_this.importVariableLibraries;
                        }else if(extName==='cl.xml'){
                            importLibs=_this.importConstantLibraries;
                        }else if(extName==='pl.xml'){
                            importLibs=_this.importParameterLibraries;
                        }else if(extName==='al.xml'){
                            importLibs=_this.importActionLibraries;
                        }else{
                            alert(`Unknow lib [${file}]`);
                            return;
                        }
                        fullFileName="jcr:"+fullFileName;
                        const pos=importLibs.indexOf(fullFileName);
                        if(pos>-1){
                            MsgBox.alert(`库文件${file}已存在!`);
                            return;
                        }
                        const extCName=action.buildType(extName);
                        const newRow=$(`<tr>
                            <td style="font-size: 11px;word-break: break-all">${fullFileName}</td>
                            <td style="text-align: center">${extCName}</td>
                        </tr>`);
                        importLibs.push(fullFileName);
                        const delCol=$('<td style="text-align: center"></td>');
                        newRow.append(delCol);
                        const delButton=$(`<div class="btn btn-link" style="padding: 0">删除</div>`);
                        delCol.append(delButton);
                        delButton.click(function () {
                            const pos=importLibs.indexOf(fullFileName);
                            importLibs.splice(pos,1);
                            newRow.remove();
                            _this._refreshLibraries(importLibs,extName);
                        });
                        tbody.append(newRow);
                        _this._refreshLibraries(importLibs,extName);
                    }
                });
            });
            const table=$('<table class="table table-bordered" style="table-layout: fixed">');
            const thead=$(`<thead>
                <tr>
                    <td>库文件路径</td><td style="width: 60px">类型</td><td style="width: 50px">删除</td>
                </tr>
            </thead>`);
            table.append(thead);
            const tbody=$('<tbody>');
            table.append(tbody);

            function initLibraries(libraries) {
                for(let lib of libraries){
                    let pos=lib.indexOf(":"),extName;
                    if(pos>-1){
                        const libName=lib.substring(pos+1,lib.length);
                        pos=libName.indexOf('.')+1;
                        extName=libName.substring(pos,libName.length);
                    }else{
                        pos=lib.indexOf('.')+1;
                        extName=lib.substring(pos,lib.length);
                    }
                    const extCName=action.buildType(extName);
                    const newRow=$(`<tr>
                    <td style="font-size: 11px;word-break: break-all">${lib}</td>
                    <td style="text-align: center">${extCName}</td>
                </tr>`);
                    const delCol=$('<td style="text-align: center"></td>');
                    newRow.append(delCol);
                    const delButton=$(`<div class="btn btn-link" style="padding: 0">删除</div>`);
                    delCol.append(delButton);
                    delButton.click(function () {
                        const pos=libraries.indexOf(lib);
                        libraries.splice(pos,1);
                        newRow.remove();
                        _this._refreshLibraries(libraries,extName);
                    });
                    tbody.append(newRow);
                }
            }
            initLibraries(_this.importVariableLibraries);
            initLibraries(_this.importParameterLibraries);
            initLibraries(_this.importConstantLibraries);
            initLibraries(_this.importActionLibraries);

            libGroup.append(table);
            g.append(libGroup);
            return g;
        }
    }

    _refreshLibraries(importLibs,extName) {
        if(importLibs.length===0){
            return;
        }
        let libs=null;
        const _this=this;
        this._loadLibraries(importLibs,function (result) {
            if(extName==='vl.xml'){
                libs=_this.variableLibraries;
                libs.splice(0,libs.length);
                for(let category of result){
                    libs.push(...category);
                }
            }else if(extName==='cl.xml'){
                libs=_this.constantLibraries;
                libs.splice(0,libs.length);
                for(let category of result){
                    libs.push(...category.categories);
                }
            }else if(extName==='pl.xml'){
                libs=_this.variableLibraries;
                let paramCategory,param1Category;
                for(let category of libs){
                    if(category.name==='参数'){
                        paramCategory=category;
                    }
                    if(category.name==='parameter'){
                        param1Category=category;
                    }
                }
                if(!paramCategory){
                    paramCategory={
                        type:'variable',
                        name:'参数'
                    };
                    libs.push(paramCategory);
                }
                if(!param1Category){
                    param1Category={
                        type:'variable',
                        name:'parameter'
                    };
                    libs.push(param1Category);
                }
                paramCategory.variables=result[0];
                param1Category.variables=result[0];
            }else if(extName==='al.xml'){
                importLibs=_this.importActionLibraries;
                libs=_this.actionLibraries;
                libs.splice(0,libs.length);
                libs.push(...result);
            }
            Event.eventEmitter.emit(CONSTANTS.LIB_CHANGE,{
                actionLibraries:_this.actionLibraries,
                constantCategories:_this.constantLibraries,
                parameterLibraries:_this.parameterLibraries,
                variableCategories:_this.variableLibraries
            });
        });
    };

    _loadLibraries(importLibs,callback){
        let files="";
        for(var i=0;i<importLibs.length;i++){
            const libFile=importLibs[i];
            if(i==0){
                files=libFile;
            }else{
                files+=";"+libFile;
            }
        }
        if(files.length<2){
            return;
        }
        var url=window._server+"/common/loadXml";
        $.ajax({
            url,
            data:{files},
            type:'POST',
            error:function(response){
                if(response && response.responseText){
                    bootbox.alert("<span style='color: red'>加载库文件失败，服务端错误："+response.responseText+"</span>");
                }else{
                    bootbox.alert("<span style='color: red'>加载库文件失败,服务端出错</span>");
                }
            },
            success:function(data){
                callback(data);
            }
        });
    };
}