/**
 * Created by Jacky.gao on 2016/8/31.
 */
import * as ACTION from './action.js';
import {combineReducers} from 'redux';

function master(state = {}, action) {
    switch (action.type){
        case ACTION.MASTER_LOADED:
            return Object.assign({},state.prototype,{masterData:action.data});
        default:
            return state;
    }
}
function slave(state = {}, action) {
    switch (action.type){
        case ACTION.SLAVE_LOADED:
            return Object.assign({},state.prototype,{slaveData:action.data});
        case ACTION.PERMISSION_CHANGE:
            let data=action.data;
            switch (action.prop){
                case "readProject":
                    data.readProject=action.permission;
                    break;
                default:
            }
            return Object.assign({},state.prototype,{slaveData:state.slaveData});
        default:
            return state;
    }
}
export default combineReducers({
    master,slave
});