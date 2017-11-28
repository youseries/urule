/**
 * Created by Jacky.gao on 2016/6/3.
 */
import React,{Component,PropTypes} from 'react';
import Grid from '../../components/grid/component/Grid.jsx';
import * as action from '../action.js';
import * as refEvent from '../../reference/event.js';
import ReferenceDialog from '../../reference/ReferenceDialog.jsx';
import {connect} from 'react-redux';
import Splitter from '../../components/splitter/component/Splitter.jsx';
import ImportXmlDialog from './ImportXmlDialog.jsx';
import * as event from '../event.js';

class VariableEditor extends Component{
    constructor(props){
        super(props);
    }
    render(){
        const {masterData,masterRowData,dispatch,file}=this.props;
        const masterGridHeaders=[
            {id:'master-name',name:'name',label:'名称',filterable:true,editable:true,width:'130px'},
            {id:'master-clazz',name:'clazz',label:'类路径',filterable:true,editable:true}
        ];
        const slaveGridHeaders=[
            {id:'slave-name',name:'name',label:'字段名',filterable:true,editable:true},
            {id:'slave-label',name:'label',label:'标题',filterable:true,width:'220px',editable:true},
            {id:'slave-type',name:'type',label:'数据类型',width:'95px',editorType:'select',selectData:['String','Integer','Char','Double','Long','Float','BigDecimal','Boolean','Date','List','Set','Map','Enum','Object'],editable:true}
        ];

        const masterGridOperationCol={
            width:'110px',
            operations:[
                {
                    label:'通过指定类生成该类所有属性',
                    icon:'glyphicon glyphicon-flash',
                    style:{fontSize:'16px',color:'#337ab7',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        let clazz=rowData.clazz;
                        if(!clazz || clazz.length<1){
                            bootbox.alert('请先指定类路径');
                            return;
                        }
                        bootbox.confirm("真的生成类["+clazz+"]中所有字段吗？",function(result) {
                            if(!result)return;
                            dispatch(action.generateFields(rowIndex,clazz));
                        });
                    }
                },
                {
                    label:'导入通过ClassUtils类生成的包含类属性的XML文件',
                    icon:'glyphicon glyphicon-cloud-upload',
                    style:{fontSize:'16px',color:'#337ab7',padding:'0px 4px',cursor:'pointer'},
                    click:function(rowIndex,rowData){
                        event.eventEmitter.emit(event.OPEN_IMPORT_XML_DIALOG,rowIndex)
                    }
                },
                {
                    label:'删除',
                    icon:'glyphicon glyphicon-trash',
                    style:{fontSize:'16px',color:'#d9534f',padding:'0px 4px',cursor:'pointer'},
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
            width:'70px',
            operations:[
                {
                    label:'删除',
                    icon:'glyphicon glyphicon-trash',
                    style:{fontSize:'16px',color:'#d9534f',padding:'0px 10px',cursor:'pointer'},
                    click:function(rowIndex){
                        bootbox.confirm('真的要删除当前记录？',function(result) {
                            if(!result)return;
                            dispatch(action.deleteSlave(rowIndex));
                        })
                    }
                }
            ]
        };

        return (
            <div style={{margin:'0px'}}>
                <ReferenceDialog/>
                <Splitter orientation='vertical' position='50%'>
                    <div  style={{padding:'0px'}}>
                        <div style={{margin:'2px'}}>
                            <div className="btn-group btn-group-sm" style={{margin:'2px'}}>
                                <button className="btn btn-primary" type="button" onClick={(e)=>{dispatch(action.addMaster())}}><i className="glyphicon glyphicon-plus-sign"></i> 添加</button>
                            </div>
                            <div className="btn-group btn-group-sm" style={{margin:'2px'}}>
                                <button className="btn btn-danger" type="button" onClick={()=>{dispatch(action.save(false,file))}}><i className="rf rf-save"></i> 保存</button>
                                <button className="btn btn-danger" type="button" onClick={()=>{dispatch(action.save(true,file))}}><i className="rf rf-savenewversion"></i> 保存为新版本</button>
                            </div>
                            <div className="btn-group btn-group-sm" style={{margin:'2px'}}>
                                <button className="btn btn-info" type="button" onClick={(e)=>{
                                    if(!this.currentData){
                                        bootbox.alert('请先选择一条具体的变量');
                                        return;
                                    }
                                    const text=`var-category="${this.masterData.name}" var="${this.currentData.name}"`;
                                    const title=`变量"${this.masterData.name}.${this.currentData.name}"`;
                                    const data={
                                        path:file,
                                        varCategory:this.masterData.name,
                                        varLabel:this.currentData.label,
                                        varName:this.currentData.name
                                    };
                                    refEvent.eventEmitter.emit(refEvent.OPEN_REFERENCE_DIALOG,data,title);
                                }}><i className="rf rf-link"></i> 查看引用</button>
                            </div>
                        </div>

                        <Grid headers={masterGridHeaders} dispatch={dispatch} rows={masterData} operationConfig={masterGridOperationCol} rowClick={(rowData)=>{
                            this.masterData=rowData;
                            this.currentData=null;
                            setTimeout(function(){dispatch(action.loadSlaveData(rowData));},1);
                        }}></Grid>
                        <ImportXmlDialog dispatch={dispatch}/>
                    </div>
                    <div  style={{padding:'0px'}}>
                        <div style={{margin:'2px'}}>
                            <div className="btn-group btn-group-sm" style={{margin:'2px'}}>
                                <button className="btn btn-primary" type="button" onClick={(e)=>{dispatch(action.addSlave())}}><i className="glyphicon glyphicon-plus-sign"></i> 添加字段</button>
                            </div>
                        </div>
                        <Grid headers={slaveGridHeaders} dispatch={dispatch} operationConfig={slaveGridOperationCol} rows={masterRowData.variables || []} rowClick={(rowData)=>{
                            this.currentData=rowData;
                        }}></Grid>
                    </div>
                </Splitter>
            </div>
        );
    }
};
function select(state) {
    return {
        masterData:state.master.data || [],
        masterRowData:state.slave.data || {}
    };
}
export default connect(select)(VariableEditor);