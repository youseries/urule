/**
 * Created by Jacky.gao on 2016/9/19.
 */
import Cell from './Cell.js';
export default class AttributeCell extends Cell{
    constructor(row,col,cellData){
        super(row,col,cellData);
        this.type="attribute";
    }
    initCell(cellData){
        const container=$(`<div></div>`);
        this.td.append(container);
        this.propContainer=generateContainer();
        if(cellData){
            this.variableLabel=cellData.variableLabel;
            this.variableName=cellData.variableName;
            this.weight=cellData.weight;
            URule.setDomContent(this.propContainer,this.variableLabel || this.variableName);
        }else{
            URule.setDomContent(this.propContainer,"请选择属性");
        }
        container.append(this.propContainer);
        this.propContainer.css({color:'green'});
        const _this=this;
        const del=$(`<span class="attribute-operation" style="color: #ff0600"><i class="glyphicon glyphicon-remove" style="cursor: pointer" title="删除当前属性行"></i></span>`);
        container.append(del);
        del.click(function () {
            bootbox.confirm("真的要删除？",function (result) {
                if(!result)return;
                _this.row.remove();
            });
        });
        const addCondition=$(`<span class="attribute-operation" style="color: #019dff"><i class="glyphicon glyphicon-plus-sign" style="cursor: pointer" title="添加条件行"></i></span>`);
        container.append(addCondition);
        addCondition.click(function () {
            _this.row.addConditionRow();
        });
        if(this.row.scoreCardTable.attributeCol.category){
            this.refreshAttributeCellMenus(this.row.scoreCardTable.attributeCol.category.variables);
        }
        this.weightContainer=$("<div style='margin-top: 5px;color:#999'><label>权重：</label></div>");
        if(!this.row.scoreCardTable.weightSupport){
            this.weightContainer.hide();
        }

        this.weightEditor=$(`<input type="text" class="form-control" style="width:60px;height: 25px;display: inline-block">`);
        this.weightContainer.append(this.weightEditor);
        if(this.weight){
            this.weightEditor.val(this.weight);
        }
        this.weightEditor.change(function () {
            _this.weight=$(this).val();
        });
        container.append(this.weightContainer);
    }

    showWeight(){
        this.weightContainer.show();
        this.weight=null;
        this.weightEditor.val('');
    }
    hideWeight(){
        this.weightContainer.hide();
        this.weight=null;
        this.weightEditor.val('');
    }

    refreshAttributeCellMenus(variables){
        const menuItems=[];
        const _this=this;
        for(let variable of variables){
            menuItems.push({
                label:variable.label || variable.name,
                onClick:function () {
                    _this.variable=variable;
                    URule.setDomContent(_this.propContainer,variable.label || variable.name);
                }
            })
        }
        if(!this.propContainer.menu){
            this.propContainer.menu=new URule.menu.Menu({menuItems});
            this.propContainer.click(function (e) {
                _this.propContainer.menu.show(e);
            });
        }else{
            this.propContainer.menu.setConfig({menuItems});
        }
    }
}
