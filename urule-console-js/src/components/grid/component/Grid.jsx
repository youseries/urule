/**
 * Created by Jacky.gao on 2016/5/30.
 */
import React,{Component,PropTypes} from 'react';
import ReactDOM from 'react-dom';
import Row from './Row.jsx';
import CellEditor from './CellEditor.jsx';
import {uniqueID} from '../../componentAction.js';

class Grid extends Component{
    constructor(props){
        super(props);
        this.state={display:'none'};
        this.filterData={};
    }
    onFilter(colIndex,e){
        console.log(e.key);
        if(e.key!=='Enter'){
            return;
        }
        const value=e.target.value;
        const name=e.target.name;
        var oldData=this.filterData[name];
        if(value===oldData){
            return;
        }
        this.filterData[name]=value;
        const $dom=$(ReactDOM.findDOMNode(this));
        const $tbody=$dom.children('tbody');
        const $trs=$tbody.children('tr');
        for(var i=1;i<$trs.length;i++){
            const $tr=$($trs[i]);
            let contain=false;
            $tr.children('td').each((index,td)=>{
                let $td=$(td);
                if(index===colIndex){
                    let content=$td.children('div').html();
                    if(content.indexOf(value)>-1){
                        contain=true;
                    }
                    return false;
                }
            });
            if(!contain){
                $tr.hide();
            }else{
                $tr.show();
            }
        }
    }
    render(){
        const {headers,operationConfig,dispatch,selectFirst,uniqueKey}=this.props;
        const rows=this.props.rows || [];
        const headerContent=[];
        const bodyContent=[];
        headers.forEach((header,index)=>{
            if(header.editable){
                headerContent.push(
                    <td key={uniqueID()} style={{width:header.width}}>
                        <label>{header.label}</label>
                        <CellEditor onchange={this.props.onchange} onblur={(e)=>{
                         this.setState({display:'none'});
                         this.props.onblur(e);
                    }} header={header} display={this.state.display}/>
                    </td>
                );
            }else{
                headerContent.push(
                    <td key={uniqueID()} style={{width:header.width}}>
                        <label>{header.label}</label>
                    </td>
                );
            }
        });
        if(operationConfig){
            headerContent.push(
                <td key={uniqueID()} style={{width:operationConfig.width}}><label>操作列</label></td>
            )
        }
        const filterRow=(
            <tr key='filterrow' style={{background:'#eee'}}>
                {headers.map((header,index)=>{
                    if(header.filterable){
                        return (<td key={uniqueID()}>
                            <input type="text" onKeyPress={this.onFilter.bind(this,index)} name={header.id} className="form-control" style={{height:'26px'}} placeholder='请输入过滤条件，回车查询...'></input>
                        </td>);
                    }else{
                        return (<td key={uniqueID()}>&nbsp;</td>);
                    }
                })}
                {operationConfig ? (<td></td>) : null}
            </tr>
        );
        bodyContent.push(filterRow);
        rows.forEach((row,rowIndex)=>{
            if(!row.id){
                row.id=uniqueID();
            }
            var rowKey=row.id;
            if(rowIndex===0 && selectFirst){
                bodyContent.push(
                    <Row key={rowKey} select={true} ready={this.props.ready} headers={headers} dispatch={dispatch} rowClick={this.props.rowClick} operations={operationConfig ? operationConfig.operations : null} rowData={row} rowIndex={rowIndex}/>
                );
            }else{
                bodyContent.push(
                    <Row key={rowKey} ready={this.props.ready} headers={headers} dispatch={dispatch} rowClick={this.props.rowClick} operations={operationConfig ? operationConfig.operations : null} rowData={row} rowIndex={rowIndex}/>
                );
            }
        });
        const tableStyle={margin:0,width:(this.props.width ? this.props.width : '100%')};
        return (
            <table className="table table-bordered" style={tableStyle}>
                <thead><tr className="well">{headerContent}</tr></thead>
                <tbody>{bodyContent}</tbody>
            </table>
        );
    }
};
export default Grid;