/**
 * Created by jacky on 2016/6/11.
 */
import '../../node_modules/bootstrap/dist/css/bootstrap.css';
import '../css/iconfont.css';

import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux';
import {createStore,applyMiddleware} from 'redux';
import thunk from 'redux-thunk';
import reducer from './reducer.js';
import ConstantEditor from './components/ConstantEditor.jsx';
import * as action from './action.js';

$(document).ready(function(){
    const store=createStore(reducer,applyMiddleware(thunk));
    const file=_getParameter('file');
    if(!file || file.length<1){
        bootbox.alert('请先指定要加载的常量库文件.');
        return;
    }
    store.dispatch(action.loadMasterData(file));
    ReactDOM.render(
        <Provider store={store}>
            <ConstantEditor file={file}/>
        </Provider>,
        document.getElementById('container')
    );
});
function _getParameter(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)return unescape(r[2]);
    return null;
};