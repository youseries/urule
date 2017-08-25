/**
 * Created by Jacky.gao on 2016/5/24.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import * as event from '../../componentEvent.js';

export default class IFrame extends Component{
    componentDidMount(){
        const $iframe=$(ReactDOM.findDOMNode(this));
        $iframe.ready(function(){
            event.eventEmitter.emit(event.SHOW_LOADING);
        });
        $iframe.load(function(){
            event.eventEmitter.emit(event.HIDE_LOADING);
        });
        $iframe.css({
            height:($(document).height()-47)+"px"
        });
        $(window).resize(()=>{
            $iframe.css({
                height:$(document).height()+"px"
            });
        });
    }
    render(){
        const path=encodeURI(encodeURI(this.props.path));
        const iframeId=this.props.id;
        return (
            <iframe src={path} id={iframeId} style={{width:'100%',border:0}} frameBorder="none"></iframe>
        );
    }
}