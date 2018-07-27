/**
 * Created by Jacky.gao on 2016/8/31.
 */
import React,{Component} from 'react';
import {connect} from 'react-redux';
import Grid from '../../components/grid/component/Grid.jsx';
import Splitter from '../../components/splitter/component/Splitter.jsx';
import * as action from '../action.js';

class ResourceSecurityConfigEditor extends Component{
    refreshDetail(data={}){
        $('.detailPart').show();
        if(!data.readProject){
            $("input[name='readProject'][value='false']").prop("checked",true);
            $(".filePermissionConfig").hide();
            return;
        }
        $(".filePermissionConfig").show();
        $("input[name='readProject'][value='true']").prop("checked",true);

        resetDetail(data.readPackage,"Package");
        resetDetail(data.readVariableFile,"VariableFile");
        resetDetail(data.readParameterFile,"ParameterFile");
        resetDetail(data.readConstantFile,"ConstantFile");
        resetDetail(data.readActionFile,"ActionFile");
        resetDetail(data.readRuleFile,"RuleFile");
        resetDetail(data.readDecisionTableFile,"DecisionTableFile");
        resetDetail(data.readDecisionTreeFile,"DecisionTreeFile");
        resetDetail(data.readScorecardFile,"ScorecardFile");
        resetDetail(data.readFlowFile,"FlowFile");

        function resetDetail(permission, name) {
            if(permission){
                $("input[name='read"+name+"'][value='true']").prop("checked",true);
                $(".write"+name+"Config").show();
                if(data["write"+name]){
                    $("input[name='write"+name+"'][value='true']").prop("checked",true);
                }else{
                    $("input[name='write"+name+"'][value='false']").prop("checked",true);
                }
            }else{
                $("input[name='read"+name+"'][value='false']").prop("checked",true);
                $(".write"+name+"Config").hide();
            }
        }
    }
    permissionChange(prop,permission){
        this.currentProject[prop]=permission;
        this.refreshDetail(this.currentProject);
    }

    render(){
        const {dispatch,masterData,slaveData}=this.props;
        const masterHeaders=[
            {id:'master-username',name:'username',label:'用户名',filterable:true,width:'200px'}
        ];
        const slaveHeaders=[
            {id:'slave-project',name:'project',label:'项目名称',width:'200px'}
        ];
        return (
            <div>
                <Splitter orientation='vertical' position='20%'>
                    <div>
                        <div style={{margin:'2px'}}>
                            <div className="btn-group" style={{margin:'2px'}}>
                                <button className="btn btn-danger" type="button" onClick={()=>{action.save(masterData)}}><i className="rf rf-save"></i> 保存</button>
                            </div>
                        </div>
                        <Grid headers={masterHeaders} dispatch={dispatch} rows={masterData} rowClick={(rowData)=>{
                            $('.detailPart').hide();
                            dispatch(action.loadSlave(rowData));
                        }}></Grid>
                    </div>
                    <div>
                        <div className="row" style={{marginRight:'0'}}>
                            <div className="col-sm-4" style={{marginTop:'45px'}}>
                                <Grid headers={slaveHeaders} uniqueKey={true} dispatch={dispatch} rows={slaveData || []} rowClick={(rowData)=>{
                                    this.currentProject=rowData;
                                    this.refreshDetail(rowData);
                                }}></Grid>
                            </div>
                            <div className="col-sm-8 detailPart" style={{marginTop:'40px',display:'none',paddingLeft:0}}>
                                <div className="panel panel-default" style={{padding:'5px',margin:'5px',width:'350px',lineHeight:'30px'}}>
                                    <div className="row">
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许查看当前项目</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readProject" value="true" onClick={this.permissionChange.bind(this,'readProject',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readProject" value="false" onClick={this.permissionChange.bind(this,'readProject',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="panel panel-default filePermissionConfig" style={{padding:'5px',margin:'5px',width:'350px',lineHeight:'30px'}}>
                                    <div className="row">
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许查看知识包</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readPackage" value="true" onClick={this.permissionChange.bind(this,'readPackage',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readPackage" value="false" onClick={this.permissionChange.bind(this,'readPackage',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                    <div className="row writePackageConfig well" style={{padding:'0',marginBottom:'0'}}>
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许修改知识包</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writePackage" value="true" onClick={this.permissionChange.bind(this,'writePackage',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writePackage" value="false" onClick={this.permissionChange.bind(this,'writePackage',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="panel panel-default filePermissionConfig" style={{padding:'5px',margin:'5px',width:'350px',lineHeight:'30px'}}>
                                    <div className="row">
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许查看变量库文件</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readVariableFile" value="true" onClick={this.permissionChange.bind(this,'readVariableFile',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readVariableFile" value="false" onClick={this.permissionChange.bind(this,'readVariableFile',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                    <div className="row writeVariableFileConfig well" style={{padding:'0',marginBottom:'0'}}>
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许修改变量库文件</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writeVariableFile" value="true" onClick={this.permissionChange.bind(this,'writeVariableFile',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writeVariableFile" value="false" onClick={this.permissionChange.bind(this,'writeVariableFile',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="panel panel-default filePermissionConfig" style={{padding:'5px',margin:'5px',width:'350px',lineHeight:'30px'}}>
                                    <div className="row">
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许查看参数库文件</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readParameterFile" value="true" onClick={this.permissionChange.bind(this,'readParameterFile',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readParameterFile" value="false" onClick={this.permissionChange.bind(this,'readParameterFile',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                    <div className="row writeParameterFileConfig well" style={{padding:'0',marginBottom:'0'}}>
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许修改参数库文件</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writeParameterFile" value="true" onClick={this.permissionChange.bind(this,'writeParameterFile',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writeParameterFile" value="false" onClick={this.permissionChange.bind(this,'writeParameterFile',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="panel panel-default filePermissionConfig" style={{padding:'5px',margin:'5px',width:'350px',lineHeight:'30px'}}>
                                    <div className="row">
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许查看常量库文件</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readConstantFile" value="true" onClick={this.permissionChange.bind(this,'readConstantFile',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readConstantFile" value="false" onClick={this.permissionChange.bind(this,'readConstantFile',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                    <div className="row writeConstantFileConfig well" style={{padding:'0',marginBottom:'0'}}>
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许修改常量库文件</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writeConstantFile" value="true" onClick={this.permissionChange.bind(this,'writeConstantFile',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writeConstantFile" value="false" onClick={this.permissionChange.bind(this,'writeConstantFile',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="panel panel-default filePermissionConfig" style={{padding:'5px',margin:'5px',width:'350px',lineHeight:'30px'}}>
                                    <div className="row">
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许查看动作库文件</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readActionFile" value="true" onClick={this.permissionChange.bind(this,'readActionFile',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readActionFile" value="false" onClick={this.permissionChange.bind(this,'readActionFile',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                    <div className="row writeActionFileConfig well" style={{padding:'0',marginBottom:'0'}}>
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许修改动作库文件</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writeActionFile" value="true" onClick={this.permissionChange.bind(this,'writeActionFile',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writeActionFile" value="false" onClick={this.permissionChange.bind(this,'writeActionFile',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="panel panel-default filePermissionConfig" style={{padding:'5px',margin:'5px',width:'350px',lineHeight:'30px'}}>
                                    <div className="row">
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许查看规则集文件</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readRuleFile" value="true" onClick={this.permissionChange.bind(this,'readRuleFile',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readRuleFile" value="false" onClick={this.permissionChange.bind(this,'readRuleFile',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                    <div className="row writeRuleFileConfig well" style={{padding:'0',marginBottom:'0'}}>
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许修改规则集文件</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writeRuleFile" value="true" onClick={this.permissionChange.bind(this,'writeRuleFile',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writeRuleFile" value="false" onClick={this.permissionChange.bind(this,'writeRuleFile',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="panel panel-default filePermissionConfig" style={{padding:'5px',margin:'5px',width:'350px',lineHeight:'30px'}}>
                                    <div className="row">
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许查看决策表文件</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readDecisionTableFile" value="true" onClick={this.permissionChange.bind(this,'readDecisionTableFile',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readDecisionTableFile" value="false" onClick={this.permissionChange.bind(this,'readDecisionTableFile',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                    <div className="row writeDecisionTableFileConfig well" style={{padding:'0',marginBottom:'0'}}>
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许修改决策表文件</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writeDecisionTableFile" value="true" onClick={this.permissionChange.bind(this,'writeDecisionTableFile',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writeDecisionTableFile" value="false" onClick={this.permissionChange.bind(this,'writeDecisionTableFile',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="panel panel-default filePermissionConfig" style={{padding:'5px',margin:'5px',width:'350px',lineHeight:'30px'}}>
                                    <div className="row">
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许查看决策树文件</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readDecisionTreeFile" value="true" onClick={this.permissionChange.bind(this,'readDecisionTreeFile',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readDecisionTreeFile" value="false" onClick={this.permissionChange.bind(this,'readDecisionTreeFile',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                    <div className="row writeDecisionTreeFileConfig well" style={{padding:'0',marginBottom:'0'}}>
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许修改决策树文件</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writeDecisionTreeFile" value="true" onClick={this.permissionChange.bind(this,'writeDecisionTreeFile',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writeDecisionTreeFile" value="false" onClick={this.permissionChange.bind(this,'writeDecisionTreeFile',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="panel panel-default filePermissionConfig" style={{padding:'5px',margin:'5px',width:'350px',lineHeight:'30px'}}>
                                    <div className="row">
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许查看评分卡文件</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readScorecardFile" value="true" onClick={this.permissionChange.bind(this,'readScorecardFile',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readScorecardFile" value="false" onClick={this.permissionChange.bind(this,'readScorecardFile',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                    <div className="row writeScorecardFileConfig well" style={{padding:'0',marginBottom:'0'}}>
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许修改评分卡文件</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writeScorecardFile" value="true" onClick={this.permissionChange.bind(this,'writeScorecardFile',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writeScorecardFile" value="false" onClick={this.permissionChange.bind(this,'writeScorecardFile',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="panel panel-default filePermissionConfig" style={{padding:'5px',margin:'5px',width:'350px',lineHeight:'30px'}}>
                                    <div className="row">
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许查看决策流文件</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readFlowFile" value="true" onClick={this.permissionChange.bind(this,'readFlowFile',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="readFlowFile" value="false" onClick={this.permissionChange.bind(this,'readFlowFile',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                    <div className="row writeFlowFileConfig well" style={{padding:'0',marginBottom:'0'}}>
                                        <div className="col-sm-7" style={{textAlign:'right'}}>是否允许修改决策流文件</div>
                                        <div className="col-sm-5">
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writeFlowFile" value="true" onClick={this.permissionChange.bind(this,'writeFlowFile',true)} defaultChecked="false"></input> 是
                                            </label>
                                            <label className="checkbox-inline">
                                                <input type="radio" name="writeFlowFile" value="false" onClick={this.permissionChange.bind(this,'writeFlowFile',false)} defaultChecked="true"></input> 否
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </Splitter>
            </div>
        );
    }
};
function select(state = {}) {
    return {
        masterData: state.master.masterData || [],
        slaveData: state.slave.slaveData || []
    };
};

export default connect(select)(ResourceSecurityConfigEditor);