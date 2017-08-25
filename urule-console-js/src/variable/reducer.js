/**
 * Created by Jacky.gao on 2016/6/3.
 */
import * as ACTIONS from './action.js';
import {combineReducers} from 'redux';

function master(state={}, action) {
    switch (action.type){
        case ACTIONS.LOAD_MASTER_COMPLETED:
            return Object.assign({},state.prototype,{data:action.masterData});
        case ACTIONS.DEL_MASTER:
            var rowIndex=action.rowIndex;
            var newData=[...state.data];
            newData.splice(rowIndex,1);
            return Object.assign({},state.prototype,{data:newData});
        case ACTIONS.ADD_MASTER:
            var newData=[...state.data];
            newData.push({name:'',clazz:'',type:'Custom',variables:[]});
            return Object.assign({},state.prototype,{data:newData});
        case ACTIONS.GENERATED_FIELDS:
            var newData=[...state.data];
            var variables=action.variables;
            var rowIndex=action.rowIndex;
            var targetData=newData[rowIndex];
            var fields=targetData.variables || [];
            fields.push(...variables);
            targetData.variables=fields;
            return Object.assign({},state.prototype,{data:newData});
        case ACTIONS.IMPORT_FIELDS:
            var newData=[...state.data];
            var jsonResult=action.jsonResult;
            var variables=jsonResult.variables;
            var clazz=jsonResult.clazz;
            var rowIndex=action.rowIndex;
            var targetData=newData[rowIndex];
            var fields=targetData.variables || [];
            if(clazz){
                targetData.clazz=clazz;
            }
            fields.push(...variables);
            targetData.variables=fields;
            return Object.assign({},state.prototype,{data:newData});
        case ACTIONS.SAVE:
            ACTIONS.saveData(state.data,action.newVersion,action.file);
            return state;
        default:
            return state;
    }
};
function slave(state={},action) {
    switch (action.type){
        case ACTIONS.LOAD_SLAVE_COMPLETE:
            return Object.assign({},state.prototype,{data:action.masterRowData});
        case ACTIONS.DEL_SLAVE:
            var rowIndex=action.rowIndex;
            var newData=Object.assign({},state.data);
            newData.variables.splice(rowIndex,1);
            return Object.assign({},state.prototype,{data:newData});
        case ACTIONS.ADD_SLAVE:
            var newData=Object.assign({},state.data);
            newData.variables.push({name:'',label:'',type:'String'});
            return Object.assign({},state.prototype,{data:newData});
        default:
            return state;
    }
};
export default combineReducers({
    master,
    slave
});