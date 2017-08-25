/**
 * Created by Jacky.gao on 2016/5/27.
 */
import React,{Component,PropTypes} from 'react';
import * as event from '../../../frame/event.js';
import ReactDOM from 'react-dom';

export default class Dialog extends Component{
    constructor(props){
        super(props);
        this.state={title:this.props.title || '',buttons:this.props.buttons || [],body:this.props.body || []};
    }
    componentDidMount(){
        event.eventEmitter.on(event.OPEN_DIALOG,(data)=>{
            const title=data.title || this.state.title;
            const body=data.body || this.state.body;
            const buttons=data.buttons || this.state.buttons;
            const init=data.init,destroy=data.destroy;
            this.setState({title,body,buttons,init,destroy});
            $(ReactDOM.findDOMNode(this)).modal('show');
        });
        event.eventEmitter.on(event.CLOSE_DIALOG,()=>{
            $(ReactDOM.findDOMNode(this)).modal('hide');
        });
        event.eventEmitter.on(event.DIALOG_CONTNET_CHANGE,(data)=>{
            const title=data.title || this.state.title;
            const body=data.body || this.state.body;
            const buttons=data.buttons || this.state.buttons;
            this.setState({title,body,buttons});
        });
    }
    componentWillUnmount(){
        event.eventEmitter.removeAllListeners(event.OPEN_DIALOG);
        event.eventEmitter.removeAllListeners(event.CLOSE_DIALOG);
    }
    componentDidUpdate(){
        const init=this.state.init;
        if(init){
            init(this.props.dispatch);
        }
    }
    componentWillUpdate(){
        const destroy=this.state.destroy;
        if(destroy){
            destroy();
        }
    }
    render(){
        const buttons=[];
        this.state.buttons.forEach((btn,index)=>{
            buttons.push(<button type="button" key={index} className={btn.className} onClick={(e)=>{
                btn.click(this.props.dispatch);
            }}><i className={btn.icon}></i> {btn.name}</button>)
        });
        return (
            <div className="modal fade" tabIndex="-1" role="dialog" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <button type="button" className="close" data-dismiss="modal" aria-hidden="true">
                                &times;
                            </button>
                            <h4 className="modal-title" id="myModalLabel">
                                {this.state.title}
                            </h4>
                        </div>
                        <div className="modal-body">
                            {this.state.body}
                        </div>
                        <div className="modal-footer">
                            {buttons}
                        </div>
                    </div>
                </div>
            </div>
        );
    }
};
