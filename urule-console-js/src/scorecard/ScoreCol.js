/**
 * Created by Jacky.gao on 2016/9/21.
 */
import Col from './Col.js';

export default class ScoreCol extends Col{
    constructor(table,name,width){
        super(table);
        this.name=name;
        this.width=width;
        this.type='score';
        this.init();
    }
    init(){
        this.td=$(`<td style="width: ${this.width}px;padding-right: 0;background: #fded02;border:1px solid #607D8B"></td>`);
        this.td.append(this.buildColResizeTrigger());
        const container=$(`<span style="cursor: pointer">${this.name}</span>`);
        this.td.append(container);
        this.scoreCardTable.headerRow.append(this.td);
        this.bindColResize();
    }
    toXml(){
        let xml=" score-col-width=\""+this.width+"\" score-col-name=\""+this.name+"\"";
        return xml;
    }
}
