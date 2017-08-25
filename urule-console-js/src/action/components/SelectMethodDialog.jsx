/**
 * Created by jacky on 2016/6/16.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import {connect} from 'react-redux';
import CommonDialog from '../../components/dialog/component/CommonDialog.jsx';
import Grid from '../../components/grid/component/Grid.jsx';
import * as action from '../action.js';
import * as event from '../event.js';

class SelectMethodDialog extends Component{
    componentDidMount(){
        event.eventEmitter.on(event.CLOSE_SELECT_METHOD_DIALOG,()=>{
           $( ReactDOM.findDOMNode(this)).modal('hide');
        });
        const {dispatch}=this.props;
        event.eventEmitter.on(event.OPEN_SELECT_METHOD_DIALOG,(beanId)=>{
            $( ReactDOM.findDOMNode(this)).modal('show');
            dispatch(action.loadBeanMethods(beanId));
        });
    }
    componentWillUnmount(){
        event.eventEmitter.removeAllListeners(event.CLOSE_SELECT_METHOD_DIALOG);
    }
    render(){
        const {data,dispatch}=this.props;
        const headers=[
            {id:'method-methodName',name:'methodName',label:'方法名称',filterable:true,width:'200px'},
            {id:'method-name',name:'name',label:'名称',filterable:true}
        ];
        const operationCol={
            width:'80px',
            operations:[
                {
                    label:'选择此方法',
                    icon:'glyphicon glyphicon-hand-up',
                    style:{fontSize:'18px',color:'#337ab7',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        dispatch(action.addSlave(rowData));
                        bootbox.alert('添加成功.');
                    }
                }
            ]
        };
        const body=(
            <Grid rows={data} headers={headers} operationConfig={operationCol}/>
        );
        const buttons=[
            {
                name:'关闭',
                className:'btn btn-primary',
                icon:'glyphicon glyphicon-remove',
                click:function(){
                    event.eventEmitter.emit(event.CLOSE_SELECT_METHOD_DIALOG);
                }
            }
        ];
        return (
            <CommonDialog title="选择方法" body={body} buttons={buttons}/>
        );
    }
};

function select(state){
    return {data:state.methodList.data || []};
}
export default connect(select)(SelectMethodDialog);