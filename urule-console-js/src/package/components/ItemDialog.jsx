/**
 * Created by jacky on 2016/6/19.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import CommonDialog from '../../components/dialog/component/CommonDialog.jsx';
import * as event from '../event.js';
import * as action from '../action.js';
import * as componentEvent from '../../components/componentEvent.js';

export default class ItemDialog extends Component{
    constructor(props){
        super(props);
        this.state={title:''};
    }
    componentDidMount(){
        $(ReactDOM.findDOMNode(this)).bootstrapValidator({
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields:{
                itemName:{
                    validators: {
                        notEmpty: {
                            message: '文件名不能为空'
                        }
                    }
                },
                itemPath:{
                    validators: {
                        notEmpty: {
                            message: '文件路径不能为空'
                        }
                    }
                },
                itemVersion:{
                    validators: {
                        notEmpty: {
                            message: '文件版本不能为空'
                        }
                    }
                }
            }
        });
        event.eventEmitter.on(event.OPEN_CREATE_PACKAGE_ITEM_DIALOG,(data)=>{
            $(ReactDOM.findDOMNode(this)).modal('show');
            const create=data.create;
            const title=data.title;
            const rowIndex=data.rowIndex;
            if(create){
                $('[name=itemName]').val('');
                $('[name=itemPath]').val('');
                $('[name=itemVersion]').val('');
            }else{
                $('[name=itemName]').val(data.rowData.name);
                $('[name=itemPath]').val(data.rowData.path);
                $('[name=itemVersion]').val(data.rowData.version);
            }
            this.setState({create,title,rowIndex});
        });
        event.eventEmitter.on(event.HIDE_CREATE_PACKAGE_ITEM_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
    }
    componentWillUnmount(){
        event.eventEmitter.removeAllListeners(event.OPEN_CREATE_PACKAGE_ITEM_DIALOG);
        event.eventEmitter.removeAllListeners(event.HIDE_CREATE_PACKAGE_ITEM_DIALOG);
    }
    render(){
        const {dispatch}=this.props;
        const body=(
            <div>
                <div className="form-group">
                    <label>名称:</label>
                    <input type="text" className="form-control" name="itemName"/>
                </div>
                <div className="form-group">
                    <label>资源文件路径:</label>
                    <div className="input-group">
                        <input type="text" className="form-control" name="itemPath"/>
                        <span className="input-group-btn">
                            <button type="button" className="btn btn-default" onClick={()=>{
                                componentEvent.eventEmitter.emit(componentEvent.OPEN_KNOWLEDGE_TREE_DIALOG,{project:this.props.project,callback:function(file,version){
                                     $('[name=itemPath]').val('jcr:'+file);
                                     $('[name=itemVersion]').val(version);
                                }});
                            }}>选择文件</button>
                        </span>
                    </div>
                </div>
                <div className="form-group">
                    <label>版本号:</label>
                    <input type="text" className="form-control" name="itemVersion"/>
                </div>
            </div>
        );
        const buttons=[
            {
                name:'保存',
                className:'btn btn-success',
                icon:'fa fa-floppy-o',
                click:function () {
                    var validator=$(ReactDOM.findDOMNode(this)).data('bootstrapValidator');
                    validator.resetForm();
                    validator.validate();
                    var valid=validator.isValid();
                    if(!valid){
                        return;
                    }
                    var itemName=$('[name=itemName]').val();
                    var itemPath=$('[name=itemPath]').val();
                    var itemVersion=$('[name=itemVersion]').val();
                    var create=this.state.create;
                    var rowIndex=this.state.rowIndex;
                    if(create){
                        dispatch(action.addSlave({name:itemName,path:itemPath,version:itemVersion}));
                    }else{
                        dispatch(action.updateSlave({rowIndex,name:itemName,path:itemPath,version:itemVersion}));
                    }
                    event.eventEmitter.emit(event.HIDE_CREATE_PACKAGE_ITEM_DIALOG);
                }.bind(this)
            }
        ];
        return (<CommonDialog title={this.state.title} body={body} buttons={buttons}/>);
    };
}
