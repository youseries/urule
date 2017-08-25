/**
 * Created by Jacky.gao on 2016/5/24.
 */
import React,{Component,PropTypes} from 'react';
import QuickStart from '../../../frame/QuickStart.js';
import IFrame from './IFrame.jsx';
import * as event from '../../componentEvent.js';
import * as action from '../../../frame/action.js';
import Menu from '../../menu/component/Menu.jsx';
import {nextIFrameId} from '../../../Utils.js';

export default class FrameTab extends Component{
    constructor(props){
        super(props);
        this.state={data:[]};
    }
    addTab(newTabData){
        let data=this.state.data,exist=false,fullPath=this._processFullPath(newTabData.fullPath);
        for(let item of data){
            if(this._processFullPath(item.fullPath)===fullPath){
                exist=true;
            }
        }
        if(exist){
            setTimeout(function() {
                $('#tabLink'+fullPath).trigger('click');
            },100);
            return;
        }
        if(!exist){
            data.push(newTabData);
            this.setState({data});
        }
        setTimeout(function() {
            $('#tabLink'+fullPath).trigger('click');
        },100);
        const menuId='tabmenu'+fullPath;
        setTimeout(function () {
            $('#li'+fullPath).contextmenu({
                target:'#'+menuId
            });
        },100);
    }

    componentDidMount(){
        event.eventEmitter.on(event.TREE_NODE_CLICK,(data)=>{
           this.addTab(data);
        });
    };

    componentWillUnmount(){
        event.eventEmitter.removeAllListeners(event.TREE_NODE_CLICK);
    }

    _processFullPath(fullPath){
        fullPath=fullPath.replace(new RegExp('/','gm'),'');
        fullPath=fullPath.replace(new RegExp('\\.','gm'),'');
        fullPath=fullPath.replace(new RegExp(':','gm'),'');
        return fullPath;
    }
    
    render(){
        const data=this.state.data,{welcomePage}=this.props;
        let tabs=[],tabContainers=[];
        data.forEach((item,index)=>{
            const fullPath=this._processFullPath(item.fullPath),tabContainerId='iframeTab-'+fullPath,tableContainerLink='#'+tabContainerId;
            const active='',paneClass='tab-pane '+active,key='key'+fullPath;
            const liId='li'+fullPath,linkId='tabLink'+fullPath,menuId='tabmenu'+fullPath;
            const iframeId=nextIFrameId();
            tabContainers.push(
                <div className={paneClass} id={tabContainerId}  key={key}>
                    <IFrame id={iframeId} path={item.path}/>
                </div>
            );
            const fileName=item.name;
            const pointPos=fileName.indexOf('.');
            const fileType=fileName.substring(pointPos+1,fileName.length);
            let type='';
            if(fileType==='推送客户端配置'){
                type='>>'+item.project;
            }else if(fileType==='资源权限配置'){
                type='AUTH';
            }else if(fileType==='客户端访问权限配置'){
                type='AUTH';
            }else{
                type=action.buildType(fileType);
            }
            if(type==='package'){
                type=item.fullPath.substring(1,item.fullPath.length);
            }
            tabs.push(
                <li id={liId} className={active} key={key}>
                    <a id={linkId} href={tableContainerLink} data-toggle="tab">
                        <button className="close closeTab" type="button" style={{marginLeft:'5px'}} onClick={()=>{
                            const frame=$(`#${iframeId}`).get(0);
                            if(frame && frame.contentWindow && frame.contentWindow._dirty){
                                const result=confirm('当前页面内容未保存，确实要关闭吗？');
                                if(!result){
                                    return;
                                }
                            }
                            let pos=data.indexOf(item);
                            data.splice(pos,1);
                            let nextLinkId;
                            if(pos>0){
                                nextLinkId ='tabLink'+this._processFullPath(data[pos-1].fullPath);
                            }else if(data.length>0){
                                data[data.length-1].active=true;
                                nextLinkId ='tabLink'+this._processFullPath(data[data.length-1].fullPath);
                            }
                            this.setState({data});
                            if(nextLinkId){
                                setTimeout(function() {
                                      $('#'+nextLinkId).trigger('click');
                                    },100);
                            }
                        }}>×</button>
                        {(type==='AUTH') ? fileName : type+':'+fileName}
                    </a>
                    {
                        <Menu menuId={menuId} items={[
                            {
                                name:'关闭所有标签页',
                                click:function () {
                                    data.splice(0,data.length);
                                    this.setState({data});
                                }.bind(this)
                            },
                            {
                                name:'关闭其它标签页',
                                click:function () {
                                    data.splice(0,data.length);
                                    data.push(item);
                                    this.setState({data});
                                    setTimeout(function() {
                                      $('#'+linkId).trigger('click');
                                    },100);
                                }.bind(this)
                            }
                        ]} data={{id:'tab'+fullPath}}/>
                    }
                </li>
            );
        });
        if(tabs.length===0){
            if(welcomePage && welcomePage.length>0){
                if(welcomePage==='none'){
                    return (<div/>);
                }else{
                    return (
                        <iframe frameBorder="0" style={{border:0,width:'100%',height:'100%'}} src={welcomePage}></iframe>
                    );
                }
            }else{
                return <QuickStart/>;
            }
        }else{
            return (
                <div>
                    <div>
                        <ul className="nav nav-tabs" id='fornavframetab_' style={{fontSize:"12px"}}>
                            {tabs}
                        </ul>
                    </div>
                    <div className="tab-content">
                        {tabContainers}
                    </div>
                </div>
            );
        }
    }
};