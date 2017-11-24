/**
 * Created by Jacky.gao on 2016/5/23.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import TreeParentItem from './TreeParentItem.jsx';
import Menu from '../../menu/component/Menu.jsx';
import * as event from '../../componentEvent.js';

class TreeItem extends Component{
    componentDidMount(){
        const $li=$(ReactDOM.findDOMNode(this));
        const $childrenSpan= $li.children("span");
        const {data,dispatch}=this.props;
        const _this=this;
        $childrenSpan.click(function (e) {
            let $span=$(this);
            if($li.hasClass("parent_li")){
                let $liChildren = $span.parent('li.parent_li').find(' > ul > li');
                if ($liChildren.is(":visible")) {
                    $liChildren.hide('fast');
                    $span.children('i:first').addClass('rf-plus').removeClass('rf-minus');
                } else {
                    $liChildren.show('fast');
                    $span.children('i:first').addClass('rf-minus').removeClass('rf-plus');
                }
                e.stopPropagation();
            }
        });
        if($li.hasClass("parent_li")){
            const expandLevel=this.props.expandLevel;
            if(data._level>=expandLevel){
                var $liChildren =  $childrenSpan.parent('li.parent_li').find(' > ul > li');
                $liChildren.hide();
                $childrenSpan.children('i:first').addClass('rf-plus').removeClass('rf-minus');
            }else{
                $childrenSpan.children('i:first').addClass('rf-minus').removeClass('rf-plus');
            }
        }
        this._bindContextMenu(data);
    }

    isFile(){
        const data=this.props.data;
        const name=data.name;
        let isFile=false;
        if(name.indexOf(".")>-1 || name==="ul" || name==='知识包'){
            isFile=true;
        }
        return isFile;
    }
    componentWillUnmount(){
        const data=this.props.data;
        const contextMenu=data.contextMenu;
        if(!contextMenu || contextMenu.length===0){
            return;
        }
        $("#node-"+data.id).contextmenu("destroy");
    }
    componentDidUpdate(){
        this._bindContextMenu(this.props.data);
    }
    _bindContextMenu(data){
        const $node=$("#node-"+data.id);
        $node.contextmenu("destroy");
        const contextMenu=data.contextMenu;
        if(!contextMenu || contextMenu.length===0){
            return;
        }
        const menuId='treenodemenu'+data.id;
        $node.contextmenu({
            target:'#'+menuId
        });
    }
    render(){
        const {data,dispatch}=this.props;
        const children=data.children;
        const spanId="node-"+data.id,menuId='treenodemenu'+data.id;
        let menu=[];
        if(data.contextMenu){
            menu.push(<Menu items={data.contextMenu} key={data.id} data={data} dispatch={dispatch} menuId={menuId}/>);
        }
        if(children && children.length>0){
            return (
                <li className='parent_li'>
                    <span id={spanId}>
                        <i className='rf rf-minus' style={{marginRight:"2px"}}></i>
                        <i className={data._icon} style={data._style}></i> <a href='###' style={data._style}> {data.name}</a>
                        <sup><i title={data.lock ? data.lockInfo : ''} className={data.lock ? 'rf rf-lock' : ''}></i></sup>
                    </span>
                    {menu}
                    <TreeParentItem dispatch={dispatch} children={children} expandLevel={this.props.expandLevel}/>
                </li>
            );
        }else{
            let isFile=this.isFile();
            return (
                <li>
                    <span id={spanId} onClick={(e)=>{
                        if(isFile){
                            const url=window._server+data.editorPath+"?file="+data.fullPath;
                            event.eventEmitter.emit(event.TREE_NODE_CLICK,{
                                id:data.id,
                                name:data.name,
                                fullPath:data.fullPath,
                                path:url,
                                active:true
                            });
                            $('.tree').find('.tree-active').removeClass('tree-active');
                            $("#"+spanId).addClass('tree-active');
                        }
                    }}>
                        <i className={data._icon} style={data._style}></i> <a href='###' style={data._style}> {data.name}</a>
                        <sup><i title={data.lock ? data.lockInfo : ''} className={data.lock ? 'rf rf-lock' : ''}></i></sup>
                    </span>
                    {menu}
                </li>
            );
        }
    }
};
export default TreeItem;