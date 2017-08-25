/**
 * Created by Jacky.gao on 2016/8/11.
 */
import '../../node_modules/bootstrap/dist/css/bootstrap.css';
import '../css/iconfont.css';
import React from 'react';
import ReactDOM from 'react-dom';
import {createStore,applyMiddleware} from 'redux';
import {Provider} from 'react-redux';
import thunk from 'redux-thunk';

import reducer from './reducer.js';
import ClientConfigEditor from './component/ClientConfigEditor.jsx';
import * as action from './action.js';
import {getParameter} from '../Utils.js';

$(document).ready(function () {
    const store=createStore(reducer,applyMiddleware(thunk));
    const project=getParameter('project');
    store.dispatch(action.loadData(project));
    ReactDOM.render(
        <Provider store={store}>
            <ClientConfigEditor project={project}/>
        </Provider>,
        document.getElementById('container')
    );
});