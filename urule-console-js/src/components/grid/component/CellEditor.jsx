/**
 * Created by Jacky.gao on 2016/5/30.
 */
import React,{Component} from 'react';
import ReactDOM from 'react-dom';
import * as componentEvent from '../componentEvent.js';

export default class CellEditor extends Component{
    constructor(props){
        super(props);
        this.state={display:'none'};
    }
    componentDidMount(){
        componentEvent.eventEmitter.on(componentEvent.SHOW_CELL_EDITOR,(data)=>{
            const header=this.props.header;
            if(data.colId!==header.id){
                return;
            }
            const $targetDiv=$(data.targetDiv);
            const $targetTD=$targetDiv.parent();
            const $dom=$(ReactDOM.findDOMNode(this));
            $targetTD.append($dom);
            const rowData=data.rowData;
            $dom.val(rowData[header.name]);
            this.setState({$targetDiv,rowData,display:''});
        });
    }
    componentWillUnmount(){
        componentEvent.eventEmitter.removeAllListeners(componentEvent.SHOW_CELL_EDITOR);
    }
    blur(){
        const $targetDiv=this.state.$targetDiv;
        if(!$targetDiv)return;
        $targetDiv.css({display:''});
        const value=ReactDOM.findDOMNode(this).value;
        $targetDiv.html(value);
        const header=this.props.header;
        const rowData=this.state.rowData;
        rowData[header.name]=value;
        this.setState({display:'none'});
    }
    componentDidUpdate(){
        if(this.state.display===''){
            ReactDOM.findDOMNode(this).focus();
        }
    }
    render(){
        const styleObj={display:this.state.display,height:'31px',padding:'0px 5px'};
        const header=this.props.header;
        const {editorType,selectData}=header;
        switch (editorType){
            case "select":
                return (<select style={styleObj} onBlur={this.blur.bind(this)} className="form-control">
                    {selectData.map((option,index)=>{
                        return (<option key={index}>{option}</option>);
                    })}
                </select>);
            case "boolean":
                return (
                    <select  onBlur={this.blur.bind(this)} className="form-control">
                        <option value="true">true</option>
                        <option value="false" selected="selected">false</option>
                    </select>
                );
            case "date":
                return (<input style={styleObj} onBlur={this.blur.bind(this)} type="date" className="form-control"></input>);
            case "number":
                return (<input style={styleObj} onBlur={this.blur.bind(this)} type="number" className="form-control"></input>);
            default:
                return (<input style={styleObj} onBlur={this.blur.bind(this)} type="text" className="form-control"></input>);
        }
    }
};