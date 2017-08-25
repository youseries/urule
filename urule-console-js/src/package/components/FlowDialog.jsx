/**
 * Created by jacky on 2016/6/23.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import * as event from '../event.js';
import * as action from '../action.js';
import Grid from '../../components/grid/component/Grid.jsx';
import CommonDialog from '../../components/dialog/component/CommonDialog.jsx';

export default class FlowDialog extends Component{
    constructor(props){
        super(props);
        this.state={flows:null};
    }
    componentDidMount(){
        event.eventEmitter.on(event.OPEN_FLOW_DIALOG,(config)=>{
            this.rowData=null;
            $(ReactDOM.findDOMNode(this)).modal('show');
            var files=config.files;
            var data=config.data;
            action.loadFlows(files,function(result){
                this.setState({files,data,flows:result});
                const ce=window.parent.componentEvent;
                ce.eventEmitter.emit(ce.HIDE_LOADING);
            }.bind(this));
        });
        event.eventEmitter.on(event.HIDE_FLOW_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
    }
    componentWillUnmount(){
        event.eventEmitter.removeAllListeners(event.OPEN_FLOW_DIALOG);
        event.eventEmitter.removeAllListeners(event.HIDE_FLOW_DIALOG);
    }
    render(){
        const headers=[
            {id:'f-id',name:'id',label:'决策流ID',filterable:true},
        ];
        const {files,data}=this.state;
        const gridOperationCol={
            width:'70px',
            operations:[
                {
                    label:'测试',
                    icon:'glyphicon glyphicon-flash',
                    style:{fontSize:'20px',color:'#d9534f',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        const ce=window.parent.componentEvent;
                        ce.eventEmitter.emit(ce.SHOW_LOADING);
                        var flowId=rowData.id;
                        action.testFlow(files,data,flowId,function(result){
                            event.eventEmitter.emit(event.REFRESH_SIMULATOR_DATA,result);
                            ce.eventEmitter.emit(ce.HIDE_LOADING);
                            bootbox.alert("决策流["+flowId+"]执行完成，"+result.info);
                        });
                    }
                }
            ]
        };
        let body=(<div></div>);
        if(this.state.flows){
            body=(
                <Grid headers={headers} operationConfig={gridOperationCol} rows={this.state.flows}/>
            );
        }
        const buttons=[
            {
                name:'关闭',
                className:'btn btn-primary',
                icon:'fa fa-close',
                click:function () {
                    event.eventEmitter.emit(event.HIDE_FLOW_DIALOG);
                }
            }
        ];

        return (
            <CommonDialog title='测试决策流' body={body} buttons={buttons}/>
        );
    }
}