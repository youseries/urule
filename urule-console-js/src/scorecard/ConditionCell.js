/**
 * Created by Jacky.gao on 2016/9/19.
 */
import {MsgBox} from 'flowdesigner';
import Cell from './Cell.js';
export default class ConditionCell extends Cell{
    constructor(row,col,cellData){
        super(row,col,cellData);
        this.type="condition";
    }
    initCell(cellData){
        const _this=this;
        const container=$(`<div></div>`);
        this.td.append(container);
        const conditionContainer=$('<span><span style="color:#999">无</span></span>');
        container.append(conditionContainer);
        const configCondition=$(`<span class="attribute-operation" style="color:#3c763d;margin-left: 5px"><i class="glyphicon glyphicon-cog" style="cursor: pointer" title="配置条件"></i></span>`);
        container.append(configCondition);
        if(cellData){
            this.cellCondition=new urule.CellCondition("<div/>");
            this.cellCondition.initData(cellData.joint);
            conditionContainer.empty();
            conditionContainer.append(this.cellCondition.getDisplayContainer());
        }
        configCondition.click(function () {
            const dialogContent=$("<div/>");
            if(!_this.cellCondition){
                _this.cellCondition=new urule.CellCondition("<div/>");
            }
            _this.cellCondition.renderTo(dialogContent);
            const caption="配置条件";
            MsgBox.showDialog(caption,dialogContent,[],[{
                name:'hide.bs.modal',
                callback:function(){
                    conditionContainer.empty();
                    conditionContainer.append(_this.cellCondition.getDisplayContainer());
                }
            }],true);
        });

        if(this.row.rowType && this.row.rowType==='condition'){
            const _this=this;
            const del=$(`<span class="attribute-operation" style="color: #03A9F4;margin-left: 3px"><i class="glyphicon glyphicon-trash" style="cursor: pointer" title="删除当前行"></i></span>`);
            container.append(del);
            del.click(function () {
                bootbox.confirm("真的要删除？",function (result) {
                    if(!result)return;
                    _this.row.remove();
                });
            });
        }
    }
}
