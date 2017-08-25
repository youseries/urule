/**
 * Created by Jacky.gao on 2016/6/3.
 */
import '../css/grid.css';
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import Cell from './Cell.jsx';
import {uniqueID} from '../../componentAction.js';

export default class Row extends Component{
    componentDidMount(){
        const $dom=$(ReactDOM.findDOMNode(this));
        const {rowData,rowIndex,rowClick}=this.props;
        $dom.click(function(e){
            if(rowClick){
                rowClick(rowData,rowIndex);
            }
            $(this).addClass("bg-warning").siblings().removeClass("bg-warning");
        });
    }
    render(){
        const {headers,rowData,rowIndex,operations,select}=this.props;
        const tds=[];
        headers.forEach((header,headerIndex)=>{
            tds.push(
                <Cell key={uniqueID()} onchange={(newValue)=>{
                                rowData[header.name]=newValue;
                            }} rowData={rowData} header={header}/>
            );
        });
        if(operations){
            const key='op-td'+rowIndex;
            tds.push(
                <td key={uniqueID()} style={{padding:"5px 5px"}}>
                    {
                        operations.map((op,index)=>{
                        if(op.icon){
                            return (
                                <i key={uniqueID()} className={op.icon} title={op.label} style={op.style} onClick={op.click.bind(this,rowIndex,rowData)}></i>
                             );
                        }else{
                            return (
                                <button key={uniqueID()} type="button" className="btn btn-link" style={{padding:'0px 1px'}}
                                        onClick={op.click.bind(this,rowIndex,rowData)}>{op.label}
                                </button>
                            );
                        }
                        })
                    }
                </td>
            );
        }
        let trClass=select ? 'bg-warning' : '';
        trClass+=' content-tr';
        return (
            <tr style={{height:'26px'}} className={trClass}>
            {tds}
            </tr>
        );
    }
}