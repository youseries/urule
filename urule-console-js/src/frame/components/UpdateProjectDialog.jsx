/**
 * Created by Jacky.gao on 2016/5/27.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import Dialog from '../../components/dialog/component/Dialog.jsx';
import * as componentEvent from '../../components/componentEvent.js';
import * as event from '../event.js';
import * as action from '../action.js';

export default class UpdateProjectDialog extends Component{
    constructor(props){
        super(props);
        this.state={data:{}};
    }
    componentDidMount(){
        $(ReactDOM.findDOMNode(this)).bootstrapValidator({
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields:{
                projectName:{
                    validators: {
                        notEmpty: {
                            message: '项目名称不能为空'
                        },
                        regexp: {
                            regexp: "^(?!_)(?!-)[\u4e00-\u9fa5_a-zA-Z0-9_-]{1,}$",
                            message: '名称只能包含中文及英文字母、数字、下划线、中划线,且不能以下划线、中划线开头'
                        }
                    }
                }
            }
        });
        event.eventEmitter.on(event.OPEN_UPDATE_PROJECT_DIALOG,(data)=>{
            this.setState({data});
            document.getElementsByName('projectName')[0].value=data.name;
            $(ReactDOM.findDOMNode(this)).modal('show');
        });
        event.eventEmitter.on(event.CLOSE_UPDATE_PROJECT_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
    }
    componentWillUnmount(){
        event.eventEmitter.removeAllListeners(event.OPEN_UPDATE_PROJECT_DIALOG);
        event.eventEmitter.removeAllListeners(event.CLOSE_UPDATE_PROJECT_DIALOG);
    }
    render(){
        const dispatch=this.props.dispatch;
        const body=(
            <div className="form-group">
                <label>项目名称</label>
                <input type="text" className="form-control" name="projectName"></input>
            </div>
        );
        const buttons=[];
        buttons.push(
            {
                name:'保存',
                className:'btn btn-success',
                icon:'fa fa-floppy-o',
                click:function () {
                    const data=this.state.data;
                    var newProjectName=document.getElementsByName('projectName')[0].value;
                    componentEvent.eventEmitter.emit(componentEvent.SHOW_LOADING);
                    setTimeout(function () {
                        dispatch(action.fileRename(data,newProjectName));
                    },200);
                }.bind(this)
            }
        );
        return (
            <Dialog title="项目名称修改" body={body} buttons={buttons}></Dialog>
        );
    }
}