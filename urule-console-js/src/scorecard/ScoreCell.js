/**
 * Created by Jacky.gao on 2016/9/19.
 */
import Cell from './Cell.js';
export default class ScoreCell extends Cell{
    constructor(row,col,cellData){
        super(row,col,cellData);
        this.type="score";
    }
    initCell(cellData){
        const container=$(`<div></div>`);
        this.inputType=new urule.InputType(null,"无");
        container.append(this.inputType.getContainer());
        if(cellData && cellData.value){
            const value=cellData.value;
            this.inputType.setValueType(value.valueType,value);
        }
        this.td.append(container);
    }
}

