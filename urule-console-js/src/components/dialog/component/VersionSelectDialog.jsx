/**
 * Created by jacky on 2016/6/20.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import Grid from '../../../components/grid/component/Grid.jsx';
import CommonDialog from './CommonDialog.jsx';
import * as event from '../../componentEvent.js';
import * as action from '../../componentAction.js';

export default class VersionSelectDialog extends Component{
    constructor(props){
        super(props);
        this.state={title:''};
    }
    componentDidMount(){
        event.eventEmitter.on(event.OPEN_VERSION_SELECT_DIALOG,(config)=>{
            var file=config.file;
            this.callback=config.callback;
            this.file=file;
            action.loadFileVersions(file,function(data){
                $(ReactDOM.findDOMNode(this)).modal('show');
                this.setState({data,title:"选择文件["+file+"]的版本"});
            }.bind(this));
        });
        event.eventEmitter.on(event.HIDE_VERSION_SELECT_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
        $(ReactDOM.findDOMNode(this)).modal('hide');
    }
    componentWillUnmount(){
        event.eventEmitter.removeAllListeners(event.OPEN_VERSION_SELECT_DIALOG);
        event.eventEmitter.removeAllListeners(event.HIDE_VERSION_SELECT_DIALOG);
    }
    render(){
        const headers=[
            {id:'v-name',name:'name',label:'版本名称',filterable:true,width:'100px'},
            {id:'v-comment',name:'comment',label:'版本描述'},
            {id:'v-createUser',name:'createUser',label:'创建人',filterable:true,width:'140px'},
            {id:'v-createDate',name:'createDate',label:'创建日期',width:'140px',dateFormat:'yyyy-MM-dd HH:mm:ss'}
        ];

        const operationConfig={
            width:'75px',
            operations:[
                {
                    label:'选择该版本',
                    icon:'rf rf-select',
                    style:{fontSize:'18px',color:'#337ab7',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        this.callback(this.file,rowData.name);
                        event.eventEmitter.emit(event.HIDE_VERSION_SELECT_DIALOG);
                        event.eventEmitter.emit(event.HIDE_KNOWLEDGE_TREE_DIALOG);
                    }.bind(this)
                }
            ]
        };

        const body=(
            <Grid headers={headers} operationConfig={operationConfig} rows={this.state.data || []}/>
        );
        const buttons=[];
        return (
            <CommonDialog title={this.state.title} body={body} buttons={buttons} large={true}/>
        );
    }
}
