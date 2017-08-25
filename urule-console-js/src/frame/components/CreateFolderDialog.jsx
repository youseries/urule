/**
 * Created by Jacky.gao on 2016/5/27.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import Dialog from '../../components/dialog/component/Dialog.jsx';
import * as componentEvent from '../../components/componentEvent.js';
import * as event from '../event.js';
import * as action from '../action.js';

export default class CreateFolderDialog extends Component{
    constructor(props){
        super(props);
    }
    componentDidMount(){
        event.eventEmitter.on(event.OPEN_CREATE_FOLDER_DIALOG,(data)=>{
            this.setState({nodeData:data.nodeData});
            $('input[name="newFolderName"]').val('');
            $(ReactDOM.findDOMNode(this)).modal('show');
        });
        event.eventEmitter.on(event.CLOSE_CREATE_FOLDER_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
        
        const _this=this;
        $(ReactDOM.findDOMNode(this)).bootstrapValidator({
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields:{
                newFolderName:{
                    validators: {
                        notEmpty: {
                            message: '目录名不能为空'
                        },
                        regexp: {
                            regexp: "^(?!_)(?!-)[\u4e00-\u9fa5_a-zA-Z0-9_-]{1,}$",
                            message: '名称只能包含中文及英文字母、数字、下划线、中划线,且不能以下划线、中划线开头'
                        },
                        remote:{
                            message:'目录已存在',
                            type:"POST",
                            url:window._server+"/frame/fileExistCheck",
                            data: function(validator) {
                                return {
                                    fullFileName: _this.state.nodeData.fullPath+'/'+validator.getFieldElements('newFolderName').val()
                                };
                            }
                        }
                    }
                }
            }
        });
    }
    componentWillUnmount(){
        event.eventEmitter.removeAllListeners(event.OPEN_CREATE_FOLDER_DIALOG);
        event.eventEmitter.removeAllListeners(event.CLOSE_CREATE_FOLDER_DIALOG)
    }
    render(){
        const dispatch=this.props.dispatch;
        const body=(
            <div className="form-group">
                <label>新目录名称</label>
                <input type="text" className="form-control" name="newFolderName"></input>
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
                    var newFolderName=document.getElementsByName('newFolderName')[0].value,nodeData=this.state.nodeData;
                    setTimeout(function () {
                        dispatch(action.createNewFolder(newFolderName,nodeData));
                    }.bind(this),200);
                }.bind(this)
            }
        );
        return (
            <Dialog title="创建新目录" body={body} buttons={buttons}></Dialog>
        );
    }
}