/**
 * Created by jacky on 2016/6/19.
 */
import '../../../css/iconfont.css';
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import CommonDialog from './CommonDialog.jsx';
import CommonTree from '../../tree/component/CommonTree.jsx';
import * as action from '../../componentAction.js';
import * as event from '../../componentEvent.js';
import VersionSelectDialog from './VersionSelectDialog.jsx';

export default class KnowledgeTreeDialog extends Component {
    constructor(props){
        super(props);
        this.state={title:'选择资源'};
    }
    componentDidMount(){
        event.eventEmitter.on(event.OPEN_KNOWLEDGE_TREE_DIALOG,(config)=>{
            this._config=config;
            this.callback=config.callback;
            action.loadResourceTreeData({project:config.project,forLib:config.forLib,fileType:config.fileType},function(data){
                this.setState({data,fileType:config.fileType});
                $("#_knowledge_tree_dialog_container").children('.modal').modal('show');
            }.bind(this));
        });
        event.eventEmitter.on(event.HIDE_KNOWLEDGE_TREE_DIALOG,()=>{
            $("#_knowledge_tree_dialog_container").children('.modal').modal('hide');
        });
        event.eventEmitter.on(event.TREE_NODE_CLICK,(nodeData)=>{
            this.currentNodeData=nodeData;
        });
        event.eventEmitter.on(event.TREE_DIR_NODE_CLICK,(nodeData)=>{
            this.currentNodeData=nodeData;
        });
    }
    search(){
        const searchFileName=$('.resSearchText').val();
        const config=this._config;
        action.loadResourceTreeData({project:config.project,forLib:config.forLib,fileType:config.fileType,searchFileName},function(data){
            this.setState({data,fileType:config.fileType});
        }.bind(this));
    }
    componentWillUnmount(){
        event.eventEmitter.removeAllListeners(event.OPEN_KNOWLEDGE_TREE_DIALOG);
        event.eventEmitter.removeAllListeners(event.HIDE_KNOWLEDGE_TREE_DIALOG);
        event.eventEmitter.removeAllListeners(event.TREE_NODE_CLICK);
    }
    render(){
        const body=(
            <div className='tree' style={{marginLeft:'10px'}}>
                <div>
                    <input type="text" className="form-control resSearchText" placeholder="请输入要查询的文件名..." style={{display:'inline-block',width:'220px'}}></input>
                    <a href="###" onClick={this.search.bind(this)} style={{margin:'6px',fontSize:'16px'}}><i className="glyphicon glyphicon-search"></i></a>
                </div>
                <CommonTree data={this.state.data} selectDir={this.props.selectDir}/>
            </div>
        );
        const fileType=this.state.fileType || '';
        const buttons=[
            {
                name:'确定',
                className:'btn btn-danger',
                icon:'glyphicon glyphicon-floppy-saved',
                click:function () {
                    if(this.currentNodeData){
                        this.callback(this.currentNodeData.fullPath,'LATEST');
                        event.eventEmitter.emit(event.HIDE_KNOWLEDGE_TREE_DIALOG);
                    }else{
                        bootbox.alert("请先选择一个文件");
                    }
                }.bind(this)
            }
        ];
        buttons.push({
            name:'选择版本',
            className:'btn btn-primary',
            icon:'glyphicon glyphicon-hand-up',
            click:function () {
                if(this.currentNodeData){
                    event.eventEmitter.emit(event.OPEN_VERSION_SELECT_DIALOG,{file:this.currentNodeData.fullPath,callback:this.callback});
                }else{
                    bootbox.alert("请先选择一个文件");
                }
            }.bind(this)
        });
        return (
            <div>
                <VersionSelectDialog/>
                <div id="_knowledge_tree_dialog_container">
                    <CommonDialog title={this.state.title} body={body} buttons={buttons}/>
                </div>
            </div>
        );
    }
}