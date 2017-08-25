/**
 * Created by Jacky.gao on 2016/8/11.
 */
import * as ACTIONS from './action.js';

export default function clientConfig(state={},action) {
    switch (action.type){
        case ACTIONS.LOADED_DATA:
            var data=action.data;
            return Object.assign({},state.prototype,{data})
            break;
        case ACTIONS.DEL:
            var index=action.index,newData=[...state.data];
            newData.splice(index,1);
            return Object.assign({},state.prototype,{data:newData});
            break;
        case ACTIONS.ADD:
            var data=[...state.data];
            data.push({});
            return Object.assign({},state.prototype,{data});
    }
}
