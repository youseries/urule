/**
 * Created by Jacky.gao on 2016/9/18.
 */
import CustomCell from './CustomCell.js';
import ConditionCell from './ConditionCell.js';
import ScoreCell from './ScoreCell.js';
export default class Row{
    constructor(table){
        this.scoreCardTable=table;
        this.cells=[];
    }

    newConditionCell(){
        let cellData=null;
        if(this.rowData){
            cellData=this.scoreCardTable.getCell(this.rowData.rowNumber,2);
        }
        this.conditionCell=new ConditionCell(this,this.scoreCardTable.conditionCol,cellData);
        return this.conditionCell.td;
    }
    newScoreCell(){
        let cellData=null;
        if(this.rowData){
            cellData=this.scoreCardTable.getCell(this.rowData.rowNumber,3);
        }
        this.scoreCell=new ScoreCell(this,this.scoreCardTable.scoreCol,cellData);
        return this.scoreCell.td;
    }
    initCustomCells(){
        for(let col of this.scoreCardTable.customCols){
            let cellData=null;
            if(this.rowData){
                cellData=this.scoreCardTable.getCell(this.rowData.rowNumber,col.getColNumber());
            }
            const cell=new CustomCell(this,col,cellData);
            col.customCells.push(cell);
            this.tr.append(cell.td);
        }
    }
    getRowNumber(){
        if(this.attributeRow){
            //condition row
            const attributeRowNumber=this.attributeRow.getRowNumber();
            const pos=this.attributeRow.conditionRows.indexOf(this)+1;
            return attributeRowNumber+pos;
        }else{
            //attribute row
            const attributeRows=this.scoreCardTable.attributeRows;
            const pos=attributeRows.indexOf(this);
            let rowNum=0;
            for(let i=0;i<pos;i++){
                const row=attributeRows[i];
                rowNum+=1+row.conditionRows.length;
            }
            return rowNum+2;
        }
    }
    cellsToXml(){
        let xml="";
        for(let cell of this.cells){
            xml+=cell.toXml();
        }
        if(this.conditionRows){
            for(let row of this.conditionRows){
                xml+=row.cellsToXml();
            }
        }
        return xml;
    }
}