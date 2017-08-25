/**
 * Created by jacky on 2016/6/12.
 */
import '../../node_modules/bootstrap/dist/css/bootstrap.css';
import '../css/iconfont.css';

import React from 'react';
import ReactDOM from 'react-dom';
import {createStore,applyMiddleware} from 'redux';
import {Provider} from 'react-redux';
import thunk from 'redux-thunk';
import reducer from './reducer.js';
import ParameterEditor from './components/ParameterEditor.jsx';
import * as action from './action.js';
import {getParameter} from '../Utils.js';

$(document).ready(function(){
    const store=createStore(reducer,applyMiddleware(thunk));
    const file=getParameter("file");
    store.dispatch(action.loadData(file));
    ReactDOM.render(
        <Provider store={store}>
            <ParameterEditor file={file}/>
        </Provider>,
        document.getElementById('container')
    );
});