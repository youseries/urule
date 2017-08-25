/**
 * Created by Jacky.gao on 2016/5/23.
 */
import * as ACTIONS from './action.js';

function tree(state={}, action) {
    switch (action.type){
        case ACTIONS.ADD:
            return Object.assign({},state,{data:[...state.data,action.itemData]});
        case ACTIONS.DEL:
            var data=Object.assign({},state.data);
            var map=new Map();
            buildDataMap(data,map);
            var target=map.get(action.data.id);
            var targetData=target.data;
            var targetParentChildren=target.parent.children;
            var pos=targetParentChildren.indexOf(targetData);
            targetParentChildren.splice(pos,1);
            return Object.assign({},state,{data});
        case ACTIONS.UPDATE:
            var data=[...state.data];
            data[action.index].name=action.itemData.name;
            return Object.assign({},state,{data});
        case ACTIONS.LOAD_END:
            var data=action.data;
            return Object.assign({},state,{data});
        case ACTIONS.FILE_RENAME:
            var newData=Object.assign({},state.data);
            var dataMap=new Map();
            buildDataMap(newData,dataMap);
            var data=action.data;
            var targetData=dataMap.get(data.id).data;
            targetData.name=data.name;
            targetData.fullPath=data.fullPath;
            return Object.assign({},state,{data:newData});
        case ACTIONS.CREATE_NEW_PROJECT:
            var parentNodeData=action.parentNodeData;
            var newData=Object.assign({},state.data);
            var dataMap=new Map();
            buildDataMap(newData,dataMap);
            var targetParentNodeData=dataMap.get(parentNodeData.id);
            if(!targetParentNodeData.data.children){
                targetParentNodeData.data.children=[];
            }
            targetParentNodeData.data.children.push(action.newProjectData);
            return Object.assign({},state,{data:newData});
        case ACTIONS.CREATE_NEW_FILE:
            var parentNodeData=action.parentNodeData;
            var newData=Object.assign({},state.data);
            var dataMap=new Map();
            buildDataMap(newData,dataMap);
            var targetParentNodeData=dataMap.get(parentNodeData.id);
            var children=targetParentNodeData.data.children;
            if(!children){
                children=[];
                targetParentNodeData.data.children=children;
            }
            children.push(action.newFileData);
            return Object.assign({},state,{data:newData});
        default:
            return state;
    }
};

function buildDataMap(data,map,cleanActive,parent) {
    if(data instanceof Array){
        data.forEach((item,index)=>{
            map.set(item.id,{parent,data:item});
            if(cleanActive){
                item.active=false;
            }
            const children=item.children;
            if(children){
                buildDataMap(children,map,cleanActive,item);
            }
        });
    }else{
        map.set(data.id,{data,parent});
        if(cleanActive){
            data.active=false;
        }
        const children=data.children;
        if(children){
            buildDataMap(children,map,cleanActive,data);
        }
    }
}

export default tree;