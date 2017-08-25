/**
 * Created by Jacky.gao on 2016/5/23.
 */
import '../css/tree.css';
import '../../../css/iconfont.css';
import TreeItem from './TreeItem.jsx';
import React,{Component,PropTypes} from 'react';

export default class CommonTree extends Component{
    render(){
        const {data,selectDir}=this.props;
        if(data){
            buildTreeDataLevel(data,1);
            return (
                <ul style={{paddingLeft:'20px'}}>
                    <TreeItem data={data} selectDir={selectDir} expandLevel={this.props.expandLevel}/>
                </ul>
            );
        }else{
            return (<ul></ul>);
        }
    }
};

function buildTreeDataLevel(data,level) {
    data._level=level++;
    var children=data.children;
    if(children){
        children.forEach((child,index)=>{
            buildTreeDataLevel(child,level);
        });
    }
};
CommonTree.defaultProps={ expandLevel:3};