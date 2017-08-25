/**
 * Created by jacky on 2016/6/11.
 */
import {combineReducers} from 'redux';
import * as ACTIONS from './action.js';

function master(state={},action){
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
            newData.push({name:'',type:'Custom',constants:[]});
            return Object.assign({},state.prototype,{data:newData});
        case ACTIONS.SAVE:
            var data=state.data;
            ACTIONS.saveData(data,action.newVersion,action.file);
            return state;
        default:
            return state;
    }
};
function slave(state={},action){
    switch (action.type){
        case ACTIONS.LOAD_SLAVE_COMPLETE:
            return Object.assign({},state.prototype,{data:action.masterRowData});
        case ACTIONS.DEL_SLAVE:
            var rowIndex=action.rowIndex;
            var newData=Object.assign({},state.data);
            newData.constants.splice(rowIndex,1);
            return Object.assign({},state.prototype,{data:newData});
        case ACTIONS.ADD_SLAVE:
            var newData=Object.assign({},state.data);
            newData.constants.push({name:'',label:'',type:'String'});
            return Object.assign({},state.prototype,{data:newData});
        default:
            return state;
    }
};

export default combineReducers({
    master,slave
});