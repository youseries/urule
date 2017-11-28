/**
 * Created by jacky on 2016/6/12.
 */
import React,{Component,PropTypes} from 'react';
import {connect} from 'react-redux';
import Grid from '../../components/grid/component/Grid.jsx';
import * as action from '../action.js';
import * as refEvent from '../../reference/event.js';
import ReferenceDialog from '../../reference/ReferenceDialog.jsx';

class ParameterEditor extends Component{
    render(){
        const {data,file,dispatch}=this.props;
        const headers=[
            {id:'p-name',name:'name',label:'名称',filterable:true,editable:true,width:'260px'},
            {id:'p-label',name:'label',label:'标题',filterable:true,editable:true,width:'260px'},
            {id:'p-type',name:'type',label:'数据类型',editable:true,editorType:'select',selectData:['String','Integer','Char','Double','Long','Float','BigDecimal','Boolean','Date','List','Set','Map','Enum','Object']}
        ];
        const operationConfig={
            width:'100px',
            operations:[
                {
                    label:'删除',
                    icon:'glyphicon glyphicon-trash',
                    style:{fontSize:'18px',color:'#d9534f',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex){
                        bootbox.confirm('真的要删除当前记录？',function(result) {
                            if(!result)return;
                            dispatch(action.del(rowIndex));
                        });
                    }
                }
            ]
        };
        return (
            <div>
                <ReferenceDialog/>
                <div style={{margin:'2px'}}>
                    <div className="btn-group btn-group-sm" style={{margin:'2px'}}>
                        <button className="btn btn-primary" type="button" onClick={(e)=>{dispatch(action.add())}}><i className="glyphicon glyphicon-plus-sign"></i> 添加</button>
                    </div>
                    <div className="btn-group btn-group-sm" style={{margin:'2px'}}>
                        <button className="btn btn-danger" type="button" onClick={()=>{dispatch(action.save(false,file))}}><i className="rf rf-save"></i> 保存</button>
                        <button className="btn btn-danger" type="button" onClick={()=>{dispatch(action.save(true,file))}}><i className="rf rf-savenewversion"></i> 保存为新版本</button>
                    </div>
                    <div className="btn-group btn-group-sm" style={{margin:'2px'}}>
                        <button className="btn btn-info" type="button" onClick={(e)=>{
                                    if(!this.currentData){
                                        bootbox.alert('请先选择一条具体的参数');
                                        return;
                                    }
                                    const title=`参数"${this.currentData.name}"`;
                                    const data={
                                        path:file,
                                        varLabel:this.currentData.label,
                                        varName:this.currentData.name
                                    };
                                    refEvent.eventEmitter.emit(refEvent.OPEN_REFERENCE_DIALOG,data,title);
                                }}><i className="rf rf-link"></i> 查看引用</button>
                    </div>
                </div>
                <Grid headers={headers} rows={data} dispatch={dispatch} operationConfig={operationConfig} rowClick={(rowData)=>{
                    this.currentData=rowData;
                }}/>
            </div>
        );
    }
};
function select(state){
    return {data:state.data || []};
};
export default connect(select)(ParameterEditor);
