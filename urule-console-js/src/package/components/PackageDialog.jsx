/**
 * Created by jacky on 2016/6/19.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import CommonDialog from '../../components/dialog/component/CommonDialog.jsx';
import * as event from '../event.js';
import * as action from '../action.js';

export default class PackageDialog extends Component{
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
                packageId:{
                    validators: {
                        notEmpty: {
                            message: '知识包编码不能为空'
                        }
                    }
                },
                packageName:{
                    validators: {
                        notEmpty: {
                            message: '知识包名称不能为空'
                        }
                    }
                }
            }
        });
        event.eventEmitter.on(event.OPEN_CREATE_PACKAGE_DIALOG,(data)=>{
            $(ReactDOM.findDOMNode(this)).modal('show');
            const create=data.create;
            const title=data.title;
            const rowIndex=data.rowIndex;
            if(create){
                $('[name=packageId]').val('').prop('disabled',false);
                $('[name=packageName]').val('');
                $(ReactDOM.findDOMNode(this)).data('bootstrapValidator').enableFieldValidators('packageId', true);
            }else{
                $('[name=packageId]').val(data.rowData.id).prop('disabled',true);
                $('[name=packageName]').val(data.rowData.name);
                $(ReactDOM.findDOMNode(this)).data('bootstrapValidator').enableFieldValidators('packageId', false);
            }
            this.setState({create,title,rowIndex});
        });
        event.eventEmitter.on(event.HIDE_CREATE_PACKAGE_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
    }
    componentWillUnmount(){
        event.eventEmitter.removeAllListeners(event.OPEN_CREATE_PACKAGE_DIALOG);
        event.eventEmitter.removeAllListeners(event.HIDE_CREATE_PACKAGE_DIALOG);
    }
    render(){
        const {dispatch}=this.props;
        const body=(
            <div>
                <div className="form-group">
                    <label>包ID:</label>
                    <input type="text" className="form-control" name="packageId"/>
                </div>
                <div className="form-group">
                    <label>包名称:</label>
                    <input type="text" className="form-control" name="packageName"/>
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
                    validator.validate();
                    var valid=validator.isValid();
                    if(!valid){
                        return;
                    }
                    var packageId=$('[name=packageId]').val();
                    var packageName=$('[name=packageName]').val();
                    var {rowIndex,create}=this.state;
                    if(create){
                        dispatch(action.addMaster({id:packageId,name:packageName}));
                    }else{
                        dispatch(action.updateMaster({packageId,packageName,rowIndex}));
                    }
                    event.eventEmitter.emit(event.HIDE_CREATE_PACKAGE_DIALOG);
                }.bind(this)
            }
        ];
        return (<CommonDialog title={this.state.title} body={body} buttons={buttons}/>);
    };
}
