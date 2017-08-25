/**
 * Created by jacky on 2016/6/12.
 */
import * as ACTIONS from './action.js';
export default function parameter(state={},action){
    switch (action.type){
        case ACTIONS.DEL:
            var rowIndex=action.rowIndex;
            var newData=[...state.data];
            newData.splice(rowIndex,1);
            return Object.assign({},state.prototype,{data:newData});
        case ACTIONS.SAVE:
            var data=state.data;
            ACTIONS.saveData(data,action.newVersion,action.file);
            return state;
        case ACTIONS.LOAD_DATA_COMPLETED:
            var data=action.data;
            return Object.assign({},state.prototype,{data});
        case ACTIONS.ADD:
            var newData=[...state.data];
            newData.push({name:'',label:'',type:'String'});
            return Object.assign({},state.prototype,{data:newData});
        default:
            return state;
    }
};
