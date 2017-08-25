/**
 * Created by jacky on 2016/6/24.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import CommonDialog from '../../components/dialog/component/CommonDialog.jsx';
import * as event from '../event.js';

export default class ImportExcelDataDialog extends Component{
    constructor(props){
        super(props);
        this.state={files:''};
    }
    componentDidMount(){
        event.eventEmitter.on(event.OPEN_IMPORT_EXCEL_DIALOG,(files)=>{
            $(ReactDOM.findDOMNode(this)).modal('show');
            this.setState({files});
        });
        event.eventEmitter.on(event.HIDE_IMPORT_EXCEL_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
    }
    comonentWillUnmount(){
        event.eventEmitter.removeAllListeners(event.OPEN_IMPORT_EXCEL_DIALOG);
        event.eventEmitter.removeAllListeners(event.HIDE_IMPORT_EXCEL_DIALOG);
    }
    render(){
        const iframeName='upload_excel_iframe';
        const formId='upload_excel_form';
        const body=(
            <div>
                <form id={formId} method="post" encType="multipart/form-data" target={iframeName} action={window._server+'/packageeditor/importExcelTemplate'}>
                    <input name="file" type="file" style={{width:'100%'}}/>
                    <input type="hidden" name="targetFiles" value={this.state.files}/>
                </form>
                <iframe name={iframeName} height="0px" width="0px" frameBorder="0" onLoad={(e)=>{
                    try{
                        let jsonData=$.parseJSON($(e.target).contents().text());
                        $(ReactDOM.findDOMNode(this)).modal('hide');
                        event.eventEmitter.emit(event.OPEN_BATCH_TEST_DIALOG,{files:this.state.files,data:jsonData});
                    }catch(error){
                        bootbox.alert('导入Excel失败');
                    }
                }}></iframe>
            </div>
        );
        const buttons=[
            {
                name:'上传',
                className:'btn btn-primary',
                icon:'glyphicon glyphicon-cloud-upload',
                click:function () {
                    document.getElementById(formId).submit();
                }
            }
        ];
        return (<CommonDialog title="导入Excel" body={body} buttons={buttons}/>);
    }
}