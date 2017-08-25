/**
 * Created by Jacky.gao on 2016/6/6.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import Dialog from '../../components/dialog/component/Dialog.jsx';
import * as event from '../event.js';
import * as action from '../action.js';

export default class ImportXmlDialog extends Component{
    componentDidMount(){
        event.eventEmitter.on(event.OPEN_IMPORT_XML_DIALOG,(rowIndex)=> {
            $(ReactDOM.findDOMNode(this)).modal('show');
            this.setState({rowIndex});
        });
    }
    componentWillUnmount(){
        event.eventEmitter.removeAllListeners(event.OPEN_IMPORT_XML_DIALOG);
    }
    render(){
        const dispatch=this.props.dispatch;
        const iFrameName='upload_iframe';
        const formId='import_xml_form';
        const body=(
            <div>
                <form id={formId} method="post" encType="multipart/form-data" target={iFrameName} action={window._server+'/variableeditor/importXml'}>
                    <input name="file" style={{width:'100%'}} type="file"/>
                </form>
                <iframe name={iFrameName} height="0px" width="0px" frameBorder="0" onLoad={(e)=>{
                    try{
                        const content=$(e.target).contents().text();
                        if(!content || content === ''){
                            return;
                        }
                        let jsonResult=$.parseJSON(content);
                        dispatch(action.importFields(this.state.rowIndex,jsonResult));
                        $(ReactDOM.findDOMNode(this)).modal('hide');
                    }catch(error){
                        bootbox.alert('上传文件不合法');
                    }
                }}></iframe>
            </div>
        );
        const buttons=[
            {
                name:'上传',
                className:'btn btn-danger',
                icon:'glyphicon glyphicon-cloud-upload',
                click:function () {
                    document.getElementById(formId).submit();
                }
            }
        ];
        return (
            <Dialog title="导入对象属性XML文件" body={body} buttons={buttons}></Dialog>
        );
    }
}