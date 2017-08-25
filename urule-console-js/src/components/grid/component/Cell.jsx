/**
 * Created by Jacky.gao on 2016/5/30.
 */
import React,{Component} from 'react';
import ReactDOM from 'react-dom';
import * as componentEvent from '../componentEvent.js';
import {formatDate} from '../../../Utils.js';
import {MsgBox} from 'flowdesigner';

export default class Cell extends Component{
    constructor(props){
        super(props);
    }
    _storeData(){

    }
    _showEditor(colName){
        const rowData=this.props.rowData;
        var $dom=$(ReactDOM.findDOMNode(this));
        if(!this.cellEditor){
            this.cellEditor=null;
            switch (rowData._editorType){
                case 'number':
                    this.cellEditor=$('<input type="number" class="form-control" style="height:31px">');
                    break;
                case 'date':
                    this.cellEditor=$('<input type="date" class="form-control" style="height:31px">');
                    break;
                case 'boolean':
                    this.cellEditor=$('<select class="form-control" style="height:31px"><option value="true">true</option><option value="false" selected="selected">false</option></select>');
                    break;
                case 'list':
                    this.cellEditor=$('<input type="text" class="form-control" style="height:31px" title="双击打开窗口编辑列表值">');
                    this.cellEditor.dblclick(function(){
                        const _this=this;
                        let editorValue=rowData[colName];
                        if(!editorValue || editorValue===''){
                            editorValue='{}';
                        }
                        let cellJsonData=JSON.parse(editorValue) || {};
                        let rows= cellJsonData.rows || [],targetType=cellJsonData.type;
                        const callback=function(rows){
                            const jsonData={type:targetType,rows};
                            const data=JSON.stringify(jsonData);
                            rowData[colName]=data;
                            var $div=$dom.find('div');
                            $div.html(data);
                        };
                        if(!targetType){
                            const simulatorCategoryData=window.simulatorCategoryData || [];
                            const categorySelect=$(`<select class="form-control"></select>`);
                            for(let category of simulatorCategoryData){
                                categorySelect.append(`<option value="${category.clazz}">${category.name}</option>`);
                            }
                            categorySelect.append(`<option value="" selected></option>`);
                            MsgBox.dialog('选择子对象类型',categorySelect,function(){
                                targetType=categorySelect.val();
                                if(!targetType || targetType===''){
                                    MsgBox.alert('请先选择子对象类型!');
                                    return;
                                }
                                let categoryTarget=null;
                                for(let category of simulatorCategoryData){
                                    if(targetType===category.clazz){
                                        categoryTarget=category;
                                        break;
                                    }
                                }
                                const variables=categoryTarget.variables || [];
                                componentEvent.eventEmitter.emit(componentEvent.SHOW_CHILD_LIST_DIALOG,{variables,rows,callback});
                            });
                        }else{
                            let categoryTarget=null;
                            for(let category of simulatorCategoryData){
                                if(targetType===category.clazz){
                                    categoryTarget=category;
                                    break;
                                }
                            }
                            const variables=categoryTarget.variables || [];
                            componentEvent.eventEmitter.emit(componentEvent.SHOW_CHILD_LIST_DIALOG,{variables,rows,callback});
                        }
                    });
                    break;
                default:
                    this.cellEditor=$('<input type="text" class="form-control" style="height:31px">');
                    break;
            }
            $dom.append(this.cellEditor);
            this.cellEditor.blur(function(){
                rowData[colName]=$(this).val();
                var $div=$dom.find('div');
                $div.html(rowData[colName]);
                $div.show();
                $(this).hide();
            });
        }
        this.cellEditor.val(rowData[colName]);
        this.cellEditor.show();
        this.cellEditor.focus();
    }
    render(){
        const {rowData,header}=this.props;
        const dateFormat=header.dateFormat;
        let data=rowData[header.name];
        if(dateFormat){
            var d=new Date(data);
            data=formatDate(d,dateFormat);
        }
        if(data && (typeof data === 'object')){
            data=JSON.stringify(data);
        }
        const styleObj={marginTop:'5px',minHeight:'26px',wordBreak:'break-all'};
        return (
            <td style={{padding:'1px 5px'}}>
                    <div style={styleObj} onClick={(e)=>{
                                if(header.editable){
                                    const targetDiv=e.target;
                                    $(targetDiv).css({display:'none'});
                                    if(rowData._editorType){
                                        this._showEditor(header.name);
                                    }else{
                                        componentEvent.eventEmitter.emit(componentEvent.SHOW_CELL_EDITOR,{targetDiv,rowData,colId:header.id})
                                    }
                                }
                            }}>{data}
                    </div>
            </td>

        );
    }
}