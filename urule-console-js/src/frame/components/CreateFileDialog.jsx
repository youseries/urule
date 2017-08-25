/**
 * Created by jacky on 2016/6/7.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import Dialog from '../../components/dialog/component/Dialog.jsx';
import * as componentEvent from '../../components/componentEvent.js';
import * as event from '../event.js';
import * as action from '../action.js';

export default class CreateFileDialog extends Component{
    constructor(props){
        super(props);
        this.state={title:'',fileType:''};
    }
    componentDidMount(){
        event.eventEmitter.on(event.OPEN_CREATE_FILE_DIALOG,(data)=>{
            let fileType=data.fileType;
            let type=action.buildType(fileType);
            let title="创建一个";
            title+=type+'文件';
            this.setState({title,type,fileType,nodeData:data.nodeData});
            event.eventEmitter.emit(event.DIALOG_CONTNET_CHANGE,{title});
            $('input[name="newFileName"]').val('');
            $(ReactDOM.findDOMNode(this)).modal('show');
        });
        event.eventEmitter.on(event.CLOSE_CREATE_FILE_DIALOG,()=>{
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
                newFileName:{
                    validators: {
                        notEmpty: {
                            message: '文件名不能为空'
                        },
                        regexp: {
                            regexp: "^(?!_)(?!-)[\u4e00-\u9fa5_a-zA-Z0-9_-]{1,}$",
                            message: '名称只能包含中文及英文字母、数字、下划线、中划线,且不能以下划线、中划线开头'
                        },
                        remote:{
                            message:'文件名已存在',
                            type:"POST",
                            url:window._server+"/frame/fileExistCheck",
                            data: function(validator) {
                                return {
                                    fullFileName: _this.state.nodeData.fullPath+'/'+validator.getFieldElements('newFileName').val()+"."+_this.state.fileType
                                };
                            }
                        }
                    }
                }
            }
        });
    }
    componentWillUnmount(){
        event.eventEmitter.removeAllListeners(event.OPEN_CREATE_FILE_DIALOG);
        event.eventEmitter.removeAllListeners(event.CLOSE_CREATE_FILE_DIALOG);
    }
    render(){
        const {dispatch}=this.props;
        const body=(
            <div className="form-group">
                <label>文件名称</label>
                <input className="form-control" name="newFileName"/>
            </div>
        );
        const buttons=[
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
                    const newFileName=document.getElementsByName('newFileName')[0].value,{fileType,nodeData}=this.state;
                    setTimeout(function () {
                        dispatch(action.createNewFile(newFileName,fileType,nodeData));
                    },200);

                }.bind(this)
            }
        ];
        return (
            <Dialog title='' body={body} buttons={buttons}/>
        );
    }
};