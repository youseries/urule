/**
 * Created by jacky on 2016/8/8.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import CommonDialog from '../components/dialog/component/CommonDialog.jsx';
import * as event from './event.js';

export default class ReferenceDialog extends Component{
    constructor(props){
        super(props);
        this.state={title:'',files:[]};
    }
    componentDidMount(){
        event.eventEmitter.on(event.OPEN_REFERENCE_DIALOG,(data,info)=>{
            $(ReactDOM.findDOMNode(this)).modal('show');
            const title=`引用文件[${data.path}]${info}的文件`;
            $.ajax({
                url:window._server+'/common/loadReferenceFiles',
                data:data,
                type:'POST',
                success:function (files) {
                    this.setState({files,title});
                }.bind(this),
                error:function () {
                    alert('加载引用文件信息失败.');
                }
            });
        });
        event.eventEmitter.on(event.CLOSE_REFERENCE_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
    }
    render(){
        const body=(
            <table className="table table-bordered">
                <thead>
                    <tr><td>文件路径</td><td style={{width:'100px'}}>类型</td><td style={{width:'80px'}}>操作</td></tr>
                </thead>
                <tbody>
                {
                    this.state.files.map(function (file, index) {
                        return (
                            <tr key={index}>
                                <td>{file.path}</td>
                                <td>{file.type}</td>
                                <td><button type="button" className="btn btn-link" style={{padding:'5px 5px'}} onClick={function(e) {
                                    const url=window._server+file.editor+'?file='+file.path;
                                    const config={
                                        id:file.path,
                                        name:file.name,
                                        fullPath:file.path,
                                        path:url
                                    };
                                    window.parent.componentEvent.eventEmitter.emit(window.parent.componentEvent.TREE_NODE_CLICK,config);
                                }}>设计器中打开</button></td>
                            </tr>
                        );
                    })
                }
                </tbody>
            </table>
        );
        return (<CommonDialog buttons={[]} body={body} title={this.state.title}/>);
    }
}