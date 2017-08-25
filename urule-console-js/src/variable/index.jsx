/**
 * Created by Jacky.gao on 2016/5/30.
 */
import '../../node_modules/bootstrap/dist/css/bootstrap.css';
import '../css/iconfont.css';

import React from 'react';
import ReactDOM from 'react-dom';
import {createStore,applyMiddleware} from 'redux';
import thunk from 'redux-thunk';
import {Provider} from 'react-redux';
import reducer from './reducer.js';
import * as action from './action.js';
import VariableEditor from './components/VariableEditor.jsx';

$(document).ready(function () {
    const store=createStore(reducer,applyMiddleware(thunk));
    const file=_getParameter('file');
    if(!file || file.length<1){
        bootbox.alert('请先指定要加载的变量库文件.');
        return;
    }
    store.dispatch(action.loadMasterData(file));

    ReactDOM.render(
        <div>
            <Provider store={store}>
                <VariableEditor file={file}/>
            </Provider>
        </div>,
        document.getElementById("container")
    );
});
function _getParameter(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)return unescape(r[2]);
    return null;
};