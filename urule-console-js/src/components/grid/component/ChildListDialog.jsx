/**
 * Created by jacky on 2016/9/4.
 */
import React,{Component} from 'react';
import ReactDOM from 'react-dom';
import Grid from './Grid.jsx';
import CommonDialog from '../../dialog/component/CommonDialog.jsx';
import * as event from '../componentEvent.js';

export default class ChildListDialog extends Component{
    constructor(props){
        super(props);
        this.state={rows:[],variables:[]};
    }
    componentDidMount(){
        event.eventEmitter.on(event.SHOW_CHILD_LIST_DIALOG,config=>{
            $(ReactDOM.findDOMNode(this)).modal('show');
            this.setState({rows:config.rows,variables:config.variables,callback:config.callback});
        });
    }
    render(){
        const {variables,rows,callback} = this.state;
        const headers=[],_this=this;
        for(let item of variables){
            headers.push({id:`list-${item.name}`,name:item.name,label:item.label,editable:true,width:'200px'});
        }
        const gridOperationCol={
            operations:[
                {
                    label:'删除',
                    icon:'glyphicon glyphicon-trash',
                    style:{fontSize:'20px',color:'#d9534f',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        const pos=rows.indexOf(rowData);
                        rows.splice(pos,1);
                        _this.setState({rows});
                    }
                }
            ]
        };
        const buttons=[
            {
                name:'添加记录',
                className:'btn btn-primary',
                click:function(){
                    rows.push({});
                    _this.setState({rows});
                }
            },
            {
                name:'确定',
                className:'btn btn-danger',
                click:function(){
                    callback(rows);
                    $(ReactDOM.findDOMNode(_this)).modal('hide');
                }
            }
        ];
        const body=(
            <div style={{overflow:'scroll',padding:'10px'}}>
                <Grid headers={headers} rows={rows} operationConfig={gridOperationCol} uniqueKey={true}/>
            </div>
        );
        return (<CommonDialog title="定义子对象" body={body} buttons={buttons} large={true} holdState={true}/>);
    }
}
