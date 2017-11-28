/**
 * Created by jacky on 2016/6/15.
 */
import React,{Component,PropTypes} from 'react';
import {connect} from 'react-redux';
import Splitter from '../../components/splitter/component/Splitter.jsx';
import Grid from '../../components/grid/component/Grid.jsx';
import * as event from '../event.js';
import * as action from '../action.js';
import SelectMethodDialog from './SelectMethodDialog.jsx';
import * as refEvent from '../../reference/event.js';
import ReferenceDialog from '../../reference/ReferenceDialog.jsx';

class ActionEditor extends Component{
    render(){
        const masterGridHeaders=[
            {id:'m-id',name:'id',label:'Bean Id',filterable:true,editable:true,width:'180px'},
            {id:'m-name',name:'name',label:'动作名称',filterable:true,editable:true}
        ];
        const slaveGridHeaders=[
            {id:'s-name',name:'name',label:'方法名称',filterable:true,width:'160px',editable:true},
            {id:'s-methodName',name:'methodName',label:'方法名',filterable:true,editable:true}
        ];
        const parametersHeaders=[
            {id:'p-name',name:'name',label:'参数名称',filterable:true,width:'160px',editable:true},
            {id:'p-type',name:'type',label:'数据类型',editorType:'select',selectData:['String','Integer','Char','Double','Long','Float','BigDecimal','Boolean','Date','List','Set','Map','Enum','Object'],editable:true}
        ];
        const {masterData,masterRowData,slaveRowData,file,dispatch}=this.props;
        const masterGridOperationCol={
            width:'70px',
            operations:[
                {
                    label:'选择当前指定Bean中定义的方法',
                    icon:'glyphicon glyphicon-hand-up',
                    style:{fontSize:'18px',color:'#337ab7',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        let id=rowData.id;
                        if(!id || id.length<1){
                            bootbox.alert('请先指定Bean Id');
                            return;
                        }
                        event.eventEmitter.emit(event.OPEN_SELECT_METHOD_DIALOG,id);
                    }
                },
                {
                    label:'删除',
                    icon:'glyphicon glyphicon-trash',
                    style:{fontSize:'18px',color:'#d9534f',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex){
                        bootbox.confirm('真的要删除当前记录？',function(result) {
                            if(!result)return;
                            dispatch(action.deleteMaster(rowIndex));
                            dispatch(action.loadSlaveData({}));
                        });
                    }
                }
            ]
        };

        const slaveGridOperationCol={
            width:'65px',
            operations:[
                {
                    label:'删除',
                    icon:'glyphicon glyphicon-trash',
                    style:{fontSize:'18px',color:'#d9534f',padding:'0px 10px',cursor:'pointer'},
                    click:function(rowIndex){
                        bootbox.confirm('真的要删除当前记录？',function(result) {
                            if(!result)return;
                            dispatch(action.deleteSlave(rowIndex));
                            dispatch(action.loadMethodData({}));
                        })
                    }
                }
            ]
        };
        const methodGridOperationCol={
            width:'65px',
            operations:[
                {
                    label:'删除',
                    icon:'glyphicon glyphicon-trash',
                    style:{fontSize:'18px',color:'#d9534f',padding:'0px 10px',cursor:'pointer'},
                    click:function(rowIndex){
                        bootbox.confirm('真的要删除当前记录？',function(result) {
                            if(!result)return;
                            dispatch(action.deleteParameter(rowIndex));
                        })
                    }
                }
            ]
        };
        return (
            <div>
                <ReferenceDialog/>
                <Splitter orientation='vertical' position='35%'>
                    <div>
                        <div style={{margin:'2px'}}>
                            <div className="btn-group btn-group-sm" style={{margin:'2px'}}>
                                <button className="btn btn-primary" type="button" onClick={(e)=>{dispatch(action.addMaster())}}><i className="glyphicon glyphicon-plus-sign"></i> 添加Bean</button>
                            </div>
                            <div className="btn-group btn-group-sm" style={{margin:'2px'}}>
                                <button className="btn btn-danger" type="button" onClick={()=>{dispatch(action.save(false,file))}}><i className="rf rf-save"></i> 保存</button>
                                <button className="btn btn-danger" type="button" onClick={()=>{dispatch(action.save(true,file))}}><i className="rf rf-savenewversion"></i> 保存为新版本</button>
                            </div>
                            <div className="btn-group btn-group-sm" style={{margin:'2px'}}>
                                <button className="btn btn-info" type="button" onClick={(e)=>{
                                    if(!this.currentData){
                                        bootbox.alert('请先选择一条具体的动作方法');
                                        return;
                                    }
                                    const title=`动作"${this.masterData.name}.${this.currentData.name}"`;
                                    const data={
                                        path:file,
                                        beanName:this.masterData.id,
                                        beanLabel:this.masterData.name,
                                        methodName:this.currentData.methodName,
                                        methodLabel:this.currentData.name
                                    };
                                    refEvent.eventEmitter.emit(refEvent.OPEN_REFERENCE_DIALOG,data,title);
                                }}><i className="rf rf-link"></i> 查看引用</button>
                            </div>
                        </div>
                        <Grid headers={masterGridHeaders} rows={masterData} operationConfig={masterGridOperationCol} rowClick={(rowData,rowIndex)=>{
                            this.masterData=rowData;
                            this.currentData=null;
                            dispatch(action.loadSlaveData(rowData,rowIndex));
                        }}/>
                        <SelectMethodDialog/>
                    </div>
                    <div>
                        <div className="row" style={{margin:'0px'}}>
                            <div className="col-md-6 col-xs-6" style={{padding:'0px 4px 0px 2px'}}>
                                <div>
                                    <div className="btn-group btn-group-sm" >
                                        <button className="btn btn-primary" type="button" onClick={e=>{dispatch(action.addSlave())}}><i className="glyphicon glyphicon-plus"></i> 添加方法</button>
                                    </div>
                                </div>
                                <Grid headers={slaveGridHeaders} rows={masterRowData.methods} operationConfig={slaveGridOperationCol} rowClick={(rowData,rowIndex)=>{
                                    this.currentData=rowData;
                                    dispatch(action.loadMethodData(rowData,rowIndex));
                                }}/>
                            </div>
                            <div className="col-md-6 col-xs-6" style={{padding:'0px 2px 0px 1px'}}>
                                <div>
                                    <div className="btn-group btn-group-sm" >
                                        <button className="btn btn-primary" type="button" onClick={e=>{dispatch(action.addParameter())}}><i className="glyphicon glyphicon-plus-sign"></i> 添加参数</button>
                                    </div>
                                </div>
                                <Grid headers={parametersHeaders} rows={slaveRowData.parameters} operationConfig={methodGridOperationCol}/>
                            </div>
                        </div>
                    </div>
                </Splitter>
            </div>
        );
    }
};
function select(state){
    return {
        masterData:state.master.data || [],
        masterRowData:state.slave.data || {},
        slaveRowData:state.method.data || {}
    };
};
export default connect(select)(ActionEditor);