/**
 * Created by jacky on 2016/6/18.
 */
import '../css/loading.css';
import * as event from '../../componentEvent.js';
import React,{Component,PropTypes} from 'react';
export default class Loading extends Component{
    constructor(){
        super();
        this.state={content:'数据加载中...'};
    }
    componentDidMount(){
        event.eventEmitter.on(event.SHOW_LOADING,content=>{
            const $window=$(window);
            const width=$window.width();
            const height=$window.height();
            if(content){
                this.setState({content});
            }else{
                this.setState({content:'数据加载中...'});
            }
            $('.loading-cover').css({height:height,width:width}).fadeIn();
        });
        event.eventEmitter.on(event.HIDE_LOADING,()=>{
            $('.loading-cover').fadeOut();
        });
    }
    componentWillUnmount(){
        event.eventEmitter.removeAllListeners(event.SHOW_LOADING);
        event.eventEmitter.removeAllListeners(event.HIDE_LOADING);
    }
    render(){
        const $window=$(window);
        const width=$window.width();
        const height=$window.height();
        const show=this.props.show;
        const styleObj={width:width,height:height,display:show?'block':'none'};
        const coverTop=(height/2)-20;
        const coverLeft=(width/2)-20;
        const loadStyle={marginTop:coverTop,marginLeft:coverLeft,width:'40px',height:'40px'};
        return (
            <div className="loading-cover" style={styleObj} >
                <div className="spinner" style={loadStyle}>
                    <div className="spinner-container container1">
                        <div className="circle1"></div>
                        <div className="circle2"></div>
                        <div className="circle3"></div>
                        <div className="circle4"></div>
                    </div>
                    <div className="spinner-container container2">
                        <div className="circle1"></div>
                        <div className="circle2"></div>
                        <div className="circle3"></div>
                        <div className="circle4"></div>
                    </div>
                    <div className="spinner-container container3">
                        <div className="circle1"></div>
                        <div className="circle2"></div>
                        <div className="circle3"></div>
                        <div className="circle4"></div>
                    </div>
                </div>
            </div>
        );
    }
}
