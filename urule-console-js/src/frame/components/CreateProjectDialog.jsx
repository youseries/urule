/**
 * Created by Jacky.gao on 2016/5/27.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import Dialog from '../../components/dialog/component/Dialog.jsx';
import * as componentEvent from '../../components/componentEvent.js';
import * as event from '../event.js';
import * as action from '../action.js';

export default class CreateProjectDialog extends Component{
    componentDidMount(){
        $(ReactDOM.findDOMNode(this)).bootstrapValidator({
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields:{
                newProjectName:{
                    validators: {
                        notEmpty: {
                            message: '项目名不能为空'
                        },
                        regexp: {
                            regexp: "^(?!_)(?!-)[\u4e00-\u9fa5_a-zA-Z0-9_-]{1,}$",
                            message: '名称只能包含中文及英文字母、数字、下划线、中划线,且不能以下划线、中划线开头'
                        },
                        remote:{
                            message:'项目已存在',
                            type:"POST",
                            url:window._server+"/frame/projectExistCheck"
                        }
                    }
                }
            }
        });
        event.eventEmitter.on(event.OPEN_NEW_PROJECT_DIALOG,(nodeData)=>{
            this.setState({nodeData});
            $(ReactDOM.findDOMNode(this)).modal('show');
        });
        event.eventEmitter.on(event.CLOSE_NEW_PROJECT_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
    }
    componentWillUnmount(){
        event.eventEmitter.removeAllListeners(event.OPEN_NEW_PROJECT_DIALOG);
        event.eventEmitter.removeAllListeners(event.CLOSE_NEW_PROJECT_DIALOG)
    }
    render(){
        const dispatch=this.props.dispatch;
        const body=(
            <div className="form-group">
                <label>新项目名称</label>
                <input type="text" className="form-control" name="newProjectName"></input>
            </div>
        );
        const buttons=[];
        buttons.push(
            {
                name:'保存',
                className:'btn btn-success',
                icon:'fa fa-floppy-o',
                click:function () {
                    $(ReactDOM.findDOMNode(this)).data('bootstrapValidator').validate();
                    const isValid=$(ReactDOM.findDOMNode(this)).data('bootstrapValidator').isValid();
                    if(!isValid){
                        return;
                    }
                    componentEvent.eventEmitter.emit(componentEvent.SHOW_LOADING);
                    var newProjectName=document.getElementsByName('newProjectName')[0].value;
                    setTimeout(function () {
                        dispatch(action.createNewProject(newProjectName,this.state.nodeData));
                    }.bind(this),200);
                }.bind(this)
            }
        );
        return (
            <Dialog title="创建新项目" body={body} buttons={buttons}></Dialog>
        );
    }
}