/**
 * Created by Jacky.gao on 2016/5/23.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';

export default class MenuItem extends Component{
    constructor(props){
        super(props);
    }
    componentDidMount(){
        const {item,data,dispatch}=this.props;
        if(item.click){
            const dom=ReactDOM.findDOMNode(this);
            $(dom).click(function (e) {
                item.click(data,dispatch);
            });
        }
    }
    render(){
        const item=this.props.item;
        return (
            <li>
                <a href='###'><i className={item.icon} style={{color:'#00A0E8'}}></i> {item.name}</a>
            </li>
        );
    }
}