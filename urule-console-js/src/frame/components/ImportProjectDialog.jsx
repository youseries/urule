/**
 * Created by Jacky.gao on 2016/6/28.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import CommonDialog from '../../components/dialog/component/CommonDialog.jsx';
import * as event from '../event.js';

export default class ImportProjectDialog extends Component{
    componentDidMount(){
        event.eventEmitter.on(event.OPEN_IMPORT_PROJECT_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('show');
        });
        event.eventEmitter.on(event.CLOSE_IMPORT_PROJECT_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
    }
    componentWillUnmount(){
        event.eventEmitter.removeAllListeners(event.OPEN_IMPORT_PROJECT_DIALOG);
        event.eventEmitter.removeAllListeners(event.CLOSE_IMPORT_PROJECT_DIALOG);
    }
    render(){
        const formId='import_xml_form';
        const body=(
            <div>
                <form id={formId} method="post" encType="multipart/form-data" target='_self' action={window._server+'/frame/importProject'}>
                    <div className="row" style={{marginBottom:'20px'}}>
                        <div className="form-group">
                            <div className="col-xs-4" style={{textAlign:'right',padding:0}}>
                                <label>是否覆盖已存在项目：</label>
                            </div>
                            <div className="col-xs-8">
                                是<input type="radio" name="overwriteProject" className="radio-inline" defaultValue="true" defaultChecked style={{marginRight:'30px'}}/>
                                否<input type="radio" name="overwriteProject" className="radio-inline" defaultValue="false"/>
                            </div>
                        </div>
                    </div>
                    <div className="row">
                        <div className="form-group">
                            <div className="col-xs-4" style={{textAlign:'right',padding:0}}>
                                <label>选择要导入的项目备份文件：</label>
                            </div>
                            <div className="col-xs-8">
                                <input name="file" style={{width:'100%'}} type="file"/>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        );

        const buttons=[
            {
                name:'导入',
                className:'btn btn-danger',
                icon:'fa fa-upload',
                click:function () {
                    var file=$('[name=file]').val();
                    if(!file || file.length<2){
                        bootbox.alert('请选择要导入的文件');
                        return;
                    }
                    document.getElementById(formId).submit();
                }
            }
        ];
        return (<CommonDialog title="导入项目" body={body} buttons={buttons}/>)
    }
}