/**
 * Created by jacky on 2016/6/17.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import Grid from '../../components/grid/component/Grid.jsx';
import CommonDialog from '../../components/dialog/component/CommonDialog.jsx';
import * as event from '../event.js';
import * as action from '../action.js';

export default class SimulatorPage extends Component{
    constructor(props){
        super(props);
        this.state={title:'',simulatorCategoryData:[],simulatorCategoryRow:{},testResultinfo:''};
    }
    componentDidMount(){
        event.eventEmitter.on(event.OPEN_SIMULATOR_DIALOG,(rowData)=>{
            var resourceItems=rowData.resourceItems;
            if(!resourceItems || resourceItems.length===0){
                bootbox.alert("知识包["+rowData.name+"]下未定义具体的文件，不能进行仿真测试!");
                return;
            }
            var files="";
            resourceItems.forEach((item,index)=>{
                if(index>0){
                    files+=';';
                }
                files+=item.path+","+item.version;
            });
            action.loadSimulatorCategoryData(files,function(simulatorCategoryData){
                const ce=window.parent.componentEvent;
                ce.eventEmitter.emit(ce.HIDE_LOADING);
                window.simulatorCategoryData=simulatorCategoryData;
                this.setState({simulatorCategoryData,simulatorCategoryRow:(simulatorCategoryData.length>0 ? simulatorCategoryData[0] : [])})
            }.bind(this));
            this.setState({files,title:"对知识包["+rowData.name+"]进行仿真测试"});
            $(ReactDOM.findDOMNode(this)).modal({
                show:true,
                backdrop:'static',
                keyboard: false
            });
        });
        event.eventEmitter.on(event.HIDE_SIMULATOR_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
        event.eventEmitter.on(event.REFRESH_SIMULATOR_DATA,(result)=>{
            var info=result.info;
            var data=result.data;
            action.buildSimulatorVariableEditorType(data);
            this.setState({simulatorCategoryData:data,simulatorCategoryRow:(data.length>0 ? data[0] : {}),testResultinfo:'测试结果：'+info});
        });
    }

    componentWillUnmount(){
        event.eventEmitter.removeAllListeners(event.OPEN_SIMULATOR_DIALOG);
        event.eventEmitter.removeAllListeners(event.HIDE_SIMULATOR_DIALOG);
        event.eventEmitter.removeAllListeners(event.REFRESH_SIMULATOR_DATA);
    }
    render(){
        if(this.state.simulatorCategoryData.length>0){
            const masterHeaders=[
                {id:'tm-name',name:'name',label:'类型名称',filterable:true}
            ];
            const slaveHeaders=[
                {id:'ts-defaultValue',name:'defaultValue',label:'值',editable:true,width:'200px'},
                {id:'ts-label',name:'label',filterable:true,label:'标题'},
                {id:'ts-type',name:'type',label:'数据类型',width:'100px'}
            ];
            const body = (
                <div style={{minHeight:'400px'}}>
                    <div style={{padding:'8px',marginBottom:'5px',border:'solid 1px rgb(219, 215, 215)',borderRadius:'5px'}}>
                        <div className="btn-group btn-group-sm" style={{margin:'2px'}}>
                            <button className="btn btn-primary" type="button" onClick={(e)=>{
                                    const ce=window.parent.componentEvent;
                                    ce.eventEmitter.emit(ce.SHOW_LOADING);
                                    action.doTest(this.state.files,this.state.simulatorCategoryData,function(result){
                                        var info='测试结果：'+result.info;
                                        bootbox.alert(info);
                                        var data=result.data;
                                        action.buildSimulatorVariableEditorType(data);
                                        this.setState({simulatorCategoryData:data,simulatorCategoryRow:(data.length>0 ? data[0] : {}),testResultinfo:info});
                                        ce.eventEmitter.emit(ce.HIDE_LOADING);
                                    }.bind(this));
                                }}><i className="glyphicon glyphicon-flash"></i> 测试决策包</button>
                        </div>
                        <div className="btn-group btn-group-sm" style={{margin:'2px'}}>
                            <button className="btn btn-info" type="button" onClick={(e)=>{
                                const ce=window.parent.componentEvent;
                                ce.eventEmitter.emit(ce.SHOW_LOADING);
                                event.eventEmitter.emit(event.OPEN_FLOW_DIALOG,{files:this.state.files,data:this.state.simulatorCategoryData});
                                }}><i className="glyphicon glyphicon-random"></i> 测试决策流</button>
                        </div>
                        <div className="btn-group btn-group-sm" style={{margin:'2px'}}>
                            <button className="btn btn-success" type="button" onClick={(e)=>{
                                event.eventEmitter.emit(event.OPEN_RETE_DIAGRAM_DIALOG,this.state.files);
                                }}><i className="glyphicon glyphicon-tree-conifer"></i> 查看Rete树</button>
                        </div>
                        <div className="btn-group btn-group-sm" style={{margin:'2px'}}>
                            <button className="btn btn-warning" type="button" onClick={()=>{
                                   action.exportExcelTemplate(this.state.files);
                                }}><i className="glyphicon glyphicon-download"></i> 下载Excel测试数据模版</button>
                            <button className="btn btn-danger" type="button" onClick={()=>{
                                event.eventEmitter.emit(event.OPEN_IMPORT_EXCEL_DIALOG,this.state.files);
                                }}><i className="glyphicon glyphicon-upload"></i> 上传Excel测试数据</button>
                        </div>
                    </div>
                    <div className="row" style={{margin:0}}>
                        <div className="col-xs-3 col-md-3" style={{paddingLeft:0,paddingRight:'5px'}}>
                            <Grid selectFirst={true} headers={masterHeaders} rows={this.state.simulatorCategoryData} rowClick={(rowData,rowIndex)=>{
                            var data=this.state.simulatorCategoryData;
                            setTimeout(function(){this.setState({simulatorCategoryRow:data[rowIndex]});}.bind(this),10);
                        }}/>
                        </div>
                        <div className="col-xs-9 col-md-9" style={{padding:0}}>
                            <Grid headers={slaveHeaders} rows={this.state.simulatorCategoryRow.variables || []} uniqueKey={true}/>
                        </div>
                    </div>
                </div>
            );
            return (<CommonDialog title={this.state.title} body={body} large={true} buttons={[]}/>);
        }else{
            return (<CommonDialog title={this.state.title} body={[]} large={true} buttons={[]}/>);
        }
    }
}

