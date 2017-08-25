/**
 * Created by Jacky.gao on 2016/9/21.
 */
import Col from './Col.js';
export default class ConditionCol extends Col{
    constructor(table,name,width){
        super(table);
        this.name=name;
        this.width=width;
        this.type='condition';
        this.init();
    }
    init(){
        this.td=$(`<td style="width: ${this.width}px;padding-right: 0;background: #607D8B;color: #ffffff;border:1px solid #607D8B">${this.name}</td>`);
        this.td.append(this.buildColResizeTrigger());
        this.scoreCardTable.headerRow.append(this.td);
        this.bindColResize();
    }
    toXml(){
        let xml=" condition-col-width=\""+this.width+"\" condition-col-name=\""+this.name+"\"";
        return xml;
    }
}