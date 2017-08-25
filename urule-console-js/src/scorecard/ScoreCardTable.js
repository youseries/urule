/**
 * Created by Jacky.gao on 2016/9/18.
 */
import AttributeRow from './AttributeRow.js';
import CustomCol from './CustomCol.js';
import PropertyConfig from './PropertyConfig.js';
import TableAction from './TableAction.js';
import AttributeCol from './AttributeCol.js';
import ConditionCol from './ConditionCol.js';
import ScoreCol from './ScoreCol.js';
export default class ScoreCardTable{
    constructor(config){
        var remarkContainer=$("<div></div>");
        config.container.append(remarkContainer);
        this.remark=new Remark(remarkContainer);
        this.weightSupport=false;
        const configContainer=$(`<div style="margin: 5px;"></div>`);
        this.weightSupportContainer=$(`<span style="padding: 8px;margin-right:5px;border:solid 1px #9E9E9E">权重:</span>`);
        this.initWeightSupportOptions();
        configContainer.append(this.weightSupportContainer);
        const propertyContainer=$(`<span></span>`);
        config.container.append(configContainer);
        configContainer.append(propertyContainer);
        this.propertyConfig=new PropertyConfig(propertyContainer);
        this.attributeRows=[];
        this.customCols=[];
        this.config=config;
        this.table=$(`<table class="table table-bordered" style="width: auto;max-width: none;font-size: 12px"></table>`);
        config.container.append(this.table);
        const actionContainer=$(`<div style="border: solid 1px #ddd;border-radius:5px;padding:10px"></div>`);
        config.container.append(actionContainer);
        this.tableAction=new TableAction(actionContainer);
    }

    initWeightSupportOptions(){
        const container=$(`<span></span>`);
        this.weightSupportContainer.append(container);
        const supportLabel=$(`<label class="checkbox-inline" style="padding-left: 8px;"></label>`);
        container.append(supportLabel);
        this.weightSupportOption=$(`<input type="radio" name="weightSupport">支持</input>`);
        supportLabel.append(this.weightSupportOption);
        const nonsupportLabel=$(`<label class="checkbox-inline" style="padding-left: 8px;"></label>`);
        container.append(nonsupportLabel);
        this.weightNonsupportOption=$(`<input type="radio" name="weightSupport">不支持</input>`);
        nonsupportLabel.append(this.weightNonsupportOption);
        const _this=this;
        this.weightSupportOption.change(function () {
            if($(this).val()==='on'){
                for(let row of _this.attributeRows){
                    row.attributeCell.showWeight();
                }
                _this.weightSupport=true;
            }
        });
        this.weightNonsupportOption.change(function () {
            if($(this).val()==='on'){
                for(let row of _this.attributeRows){
                    row.attributeCell.hideWeight();
                }
                _this.weightSupport=false;
            }
        });
    }

    init(data){
        this.data=data || {};
        if(this.data.weightSupport){
            this.weightSupport=true;
            this.weightSupportOption.prop("checked",true);
        }else{
            this.weightSupport=false;
            this.weightNonsupportOption.prop("checked",true);
        }
        this.remark.setData(data["remark"]);
        this.propertyConfig.initData(data);
        this.tableAction.initData(data);
        const header=$(`<thead></thead>`);
        this.table.append(header);
        this.headerRow=$(`<tr></tr>`);
        header.append(this.headerRow);
        this.body=$(`<tbody></tbody>`);
        this.table.append(this.body);
        this.initAttributeCol(data);
        this.initConditionCol(data);
        this.initScoreCol(data);
        const customCols=data.customCols || [];
        for(let colData of customCols){
            this.addCustomCol(colData);
        }
        const rows=data.rows || [];
        for(let rowData of rows){
            this.addAttributeRow(rowData);
        }
    }

    getCell(row,col){
        if(!this.data){
            return null;
        }
        const cells=this.data.cells;
        for(let cell of cells){
            if(cell.row===row && cell.col===col){
                return cell;
            }
        }
        throw "Cell ["+row+","+col+"] not exist.";
    }

    initAttributeCol(data){
        const width=data.attributeColWidth || '200';
        const name=data.attributeColName || '属性';
        const variableCategory=data.attributeColVariableCategory;
        this.attributeCol=new AttributeCol(this,name,width,variableCategory);
    }
    initConditionCol(data){
        const width=data.conditionColWidth || '220';
        const name=data.conditionColName || '条件';
        this.conditionCol=new ConditionCol(this,name,width);
    }
    initScoreCol(data){
        const width=data.scoreColWidth || '180';
        const name=data.scoreColName || '分值';
        this.scoreCol=new ScoreCol(this,name,width);
    }
    addAttributeRow(rowData){
        const attributeRow=new AttributeRow(this,rowData);
        this.attributeRows.push(attributeRow);
        this.body.append(attributeRow.tr);
        if(rowData){
            attributeRow.initConditionRows(rowData);
        }
        window._setDirty();
    }
    addCustomCol(colData){
        if(colData){
            const col=new CustomCol(this,colData.name,colData.width);
            this.customCols.push(col);
            window._setDirty();
        }else{
            const _this=this;
            bootbox.prompt("请输入列名",function (name) {
                if(!name || name.length<1){
                    return;
                }
                const col=new CustomCol(_this,name);
                _this.customCols.push(col);
                window._setDirty();
            });
        }
    }
    toXml(){
        if(this.attributeRows.length===0){
            throw "属性至少要有一行";
        }
        var xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        xml+="<scorecard weight-support=\""+this.weightSupport+"\" "+this.propertyConfig.toXml()+this.attributeCol.toXml()+this.conditionCol.toXml()+this.scoreCol.toXml()+this.tableAction.toXml()+">";
        xml+=this.remark.toXml();
        $.each(window.parameterLibraries,function(index,item){
            xml+="<import-parameter-library path=\""+item+"\"/>";
        });
        $.each(window.variableLibraries,function(index,item){
            xml+="<import-variable-library path=\""+item+"\"/>";
        });
        $.each(window.constantLibraries,function(index,item){
            xml+="<import-constant-library path=\""+item+"\"/>";
        });
        $.each(window.actionLibraries,function(index,item){
            xml+="<import-action-library path=\""+item+"\"/>";
        });
        for(let row of this.attributeRows){
            xml+=row.cellsToXml();
        }
        for(let row of this.attributeRows){
            xml+=row.toXml();
        }
        for(let col of this.customCols){
            xml+=col.toXml();
        }
        xml+="</scorecard>";
        return xml;
    }
};