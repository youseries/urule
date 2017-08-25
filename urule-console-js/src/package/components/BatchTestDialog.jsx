/**
 * Created by jacky on 2016/6/24.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import {uniqueID} from '../../components/componentAction.js';
import Grid from '../../components/grid/component/Grid.jsx';
import CommonDialog from '../../components/dialog/component/CommonDialog.jsx';
import * as event from '../event.js';
import * as action from '../action.js';

export default class BatchTestDialog extends Component{
    constructor(props){
        super(props);
        this.state={data:null,files:null,slaveData:null,filters:[]};
    }
    componentDidMount(){
        event.eventEmitter.on(event.OPEN_BATCH_TEST_DIALOG,(config)=>{
            $(ReactDOM.findDOMNode(this)).modal({
                show:true,
                backdrop:'static',
                keyboard: false
            });
            this.setState({data:config.data,files:config.files});
        });
        event.eventEmitter.on(event.HIDE_BATCH_TEST_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
    }
    render(){
        let body=(<div></div>);
        let {data,slaveData,filters,files}=this.state;
        let slaveTableWidth=null;
        if(data){
            let masterHeaders=[
                {id:'bt-name',name:'name',label:'类型名称',filterable:true}
            ];
            let slaveHeaders=[];
            let masterData=[];
            slaveTableWidth=0;
            data.forEach((item,index)=>{
                let slaves=item.data;
                masterData.push({
                    id:item.id,
                    name:item.name,
                    children:slaves
                });
                if(index===0 && !slaveData && slaves.length>0){
                    const child=slaves[0];
                    for(var key in child){
                        if(filters.length>0){
                            var contain=false;
                            for(var filterValue of filters){
                                if(key.indexOf(filterValue)>-1){
                                    contain=true;
                                    break;
                                }
                            }
                            if(!contain){
                                continue;
                            }
                        }
                        slaveTableWidth+=150;
                        slaveHeaders.push({
                            id:'bs-'+key,
                            name:key,
                            label:key,
                            width:'150px',
                            filterable:true
                        });
                    }
                }
            });
            if(slaveData){
                var slaves=slaveData.children;
                if(slaves.length>0){
                    var child=slaves[0];
                    for(var key in child){
                        if(filters.length>0){
                            var contain=false;
                            for(var filterValue of filters){
                                if(key.indexOf(filterValue)>-1){
                                    contain=true;
                                    break;
                                }
                            }
                            if(!contain){
                                continue;
                            }
                        }
                        slaveTableWidth+=150;
                        slaveHeaders.push({
                            id:'bs-'+key,
                            name:key,
                            label:key,
                            width:'150px',
                            filterable:true
                        });
                    }
                }
            }else{
                slaveData=masterData.length>0 ? masterData[0] : {};
            }
            slaveTableWidth=slaveTableWidth+'px';
            body=(
                <div className="row"  style={{margin:0}}>
                    <div className="col-xs-2 col-md-2" style={{paddingLeft:0,paddingRight:'5px'}}>
                        <Grid headers={masterHeaders} rows={masterData} rowClick={(rowData,rowIndex)=>{
                            this.setState({slaveData:rowData});
                        }}/>
                    </div>
                    <div className="col-xs-10 col-md-10" style={{padding:'5px',overflowX:'scroll',border:'solid 1px #eee'}}>
                        <div className="form-group">
                            <label>列过滤(多个值用","分隔)</label>
                            <div className="col-xs-10 col-md-10" style={{padding:'5px'}}>
                                <input className="form-control" id="_filter_editor"></input>
                            </div>
                            <div className="col-xs-2 col-md-2" style={{padding:'0px 10px 10px 15px'}}>
                                <button type="button" className="btn btn-primary" onClick={function(){
                                    var filterValue=$("#_filter_editor").val();
                                    if(!filterValue || filterValue===''){
                                        filterValue=[];
                                    }else{
                                        filterValue=filterValue.split(',');
                                    }
                                    var filters=filterValue;
                                    this.setState({filters});
                                }.bind(this)}><i className="glyphicon glyphicon-filter"></i> 过滤</button>
                            </div>
                        </div>
                        <div style={{margin:'5px'}}>
                            <Grid uniqueKey={true} width={slaveTableWidth} headers={slaveHeaders} rows={slaveData.children || []}/>
                        </div>
                    </div>
                </div>
            );
        }
        const buttons=[
            {
                name:'测试',
                className:'btn btn-danger',
                icon:'glyphicon glyphicon-flash',
                click:function () {
                    const ce=window.parent.componentEvent;
                    ce.eventEmitter.emit(ce.SHOW_LOADING);
                    action.doBatchTest(this.state.files,function(result){
                        var data=result.data;
                        this.setState({data});
                        bootbox.alert(result.info);
                    }.bind(this));
                }.bind(this)
            }
        ];
        return (<CommonDialog large={true} title='对导入的Excel数据进行批量测试' body={body} buttons={buttons}/>);
    }
};