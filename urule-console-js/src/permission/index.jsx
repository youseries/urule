/**
 * Created by Jacky.gao on 2016/8/31.
 */
import '../../node_modules/bootstrap/dist/css/bootstrap.css';
import '../css/iconfont.css';

import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux';
import {createStore,applyMiddleware} from 'redux';
import thunk from 'redux-thunk';
import PermissionConfigEditor from './components/PermissionConfigEditor.jsx';
import reducer from './reducer.js';
import * as action from './action.js';

$(document).ready(function () {
    const store=createStore(reducer,applyMiddleware(thunk));
    store.dispatch(action.loadMasterData());
    ReactDOM.render(
        <Provider store={store}>
            <PermissionConfigEditor/>
        </Provider>,
        document.getElementById("container")
    );
});