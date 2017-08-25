/**
 * Created by Jacky.gao on 2016/5/23.
 */
import React,{Component,PropTypes} from 'react';
import MenuItem from './MenuItem.jsx';

export default class Menu extends Component{
    render(){
        const {items,data,dispatch,menuId}=this.props;
        let result=[];
        items.forEach((item,index)=>{
            result.push(
                <MenuItem item={item} key={index} data={data} dispatch={dispatch}/>
            );
        });
        return (
            <div id={menuId}>
                <ul className="dropdown-menu" style={{color:'rgb(11, 54, 106)'}}>{result}</ul>
            </div>
        );
    }
}