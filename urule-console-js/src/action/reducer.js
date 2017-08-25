/**
 * Created by jacky on 2016/6/15.
 */
import {combineReducers} from 'redux';
import * as ACTIONS from './action.js';
import {uniqueID} from '../components/componentAction.js';

function master(state=[],action){
    switch (action.type){
        case ACTIONS.LOAD_MASTER_COMPLETED:
            var data=action.masterData.springBeans;
            return Object.assign({},state.prototype,{data});
        case ACTIONS.ADD_MASTER:
            var newData=[];
            if(state.data){
                newData=[...state.data];
            }
            newData.push({id:'',name:'',methods:[]});
            return Object.assign({},state.prototype,{data:newData});
        case ACTIONS.DEL_MASTER:
            var newData=[...state.data];
            var index=action.rowIndex;
            newData.splice(index,1);
            return Object.assign({},state.prototype,{data:newData});
        case ACTIONS.SAVE:
            var data=state.data;
            var newVersion=action.newVersion;
            var file=action.file;
            ACTIONS.saveData(data,newVersion,file);
            return state;
        default:
            return state;
    }
};

function slave(state={},action){
    switch (action.type){
        case ACTIONS.LOAD_SLAVE_COMPLETE:
            var masterRowData=action.masterRowData;
            if(masterRowData && masterRowData.methods){
                masterRowData.methods.forEach((m,index)=>{
                    m.id=uniqueID();
                });
            }
            return Object.assign({},state.prototype,{data:action.masterRowData});
        case ACTIONS.ADD_SLAVE:
            if(!state.data || !state.data.methods){
                bootbox.alert('请先指定方法所属的Bean');
                return state;
            }
            var newData=Object.assign({},state.data);
            var newSlaveData=action.newSlaveData || {name:'',methodName:'',parameters:[]};
            newData.methods.push(newSlaveData);
            return Object.assign({},state.prototype,{data:newData});
        case ACTIONS.DEL_SLAVE:
            var index=action.rowIndex;
            var newData=Object.assign({},state.data);
            newData.methods.splice(index,1);
            return Object.assign({},state.prototype,{data:newData});
        default:
            return state;
    }
};

function method(state={},action){
    switch (action.type){
        case ACTIONS.LOAD_METHOD_COMPLETED:
            return Object.assign({},state.prototype,{data:action.slaveData});
        case ACTIONS.LOAD_SLAVE_COMPLETE:
            return Object.assign({},state.prototype,{data:{}});
        case ACTIONS.ADD_PARAMETER:
            if(!state.data || !state.data.parameters){
                bootbox.alert('请先指定参数所属的方法');
                return state;
            }
            var newData=Object.assign({},state.data);
            newData.parameters.push({name:'',type:'String'});
            return Object.assign({},state.prototype,{data:newData});
        case ACTIONS.DEL_PARAMETER:
            var index=action.rowIndex;
            var newData=Object.assign({},state.data);
            newData.parameters.splice(index,1);
            return Object.assign({},state.prototype,{data:newData});
        default:
            return state;
    }
};
function methodList(state=[],action){
    switch (action.type){
        case ACTIONS.LOADED_BEAN_METHODS:
            return Object.assign({},state.prototype,{data:action.result});
        default:
            return state;
    }
}

export default combineReducers({
    master,slave,method,methodList
});
