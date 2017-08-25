/**
 * Created by Jacky.gao on 2016/5/23.
 */
import React,{Component,PropTypes} from 'react';
import TreeItem from './TreeItem.jsx';

export default class TreeParentItem extends Component{
    render(){
        const {children,dispatch,selectDir}=this.props;
        let result=[];
        children.forEach((item,index)=>{
            result.push(
                <TreeItem data={item} key={index} dispatch={dispatch} selectDir={selectDir} expandLevel={this.props.expandLevel}/>
            );
        });
        return (
            <ul style={{marginLeft:"-18px"}}>
                {result}
            </ul>
        );
    }
};