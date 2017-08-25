/**
 * Created by Jacky.gao on 2016/9/18.
 */
import Col from './Col.js';
export default class CustomCol extends Col{
    constructor(table,name,width){
        super(table);
        this.customCells=[];
        this.name=name;
        this.width=width || 160;
        this.type='custom';
        this.init();
    }
    init(){
        const _this=this;
        this.td=$(`<td style="width:${this.width}px;background: #dbfd60;border:1px solid #607D8B;padding-right: 0px">${this.name}</td>`);
        const del=$("<span style='color: #999;margin-left: 4px;cursor: pointer'><i class='glyphicon glyphicon-remove'></i></span>");
        this.td.append(del);
        del.click(function () {
            bootbox.confirm("真的要删除当前列？",function (result) {
                if(!result)return;
                _this.remove();
            });
        });
        this.td.append(this.buildColResizeTrigger());
        for(let row of this.scoreCardTable.attributeRows){
            row.addCustomCol(this);
        }
        this.scoreCardTable.headerRow.append(this.td);
        this.bindColResize();
    }
    remove(){
        for(let row of this.scoreCardTable.attributeRows){
            row.removeCustomCol(this);
        }
        const pos=this.scoreCardTable.customCols.indexOf(this);
        this.scoreCardTable.customCols.splice(pos,1);
        for(let cell of this.customCells){
            cell.td.remove();
        }
        this.td.remove();
        window._setDirty();
    }
    toXml(){
        let xml="<custom-col col-number=\""+this.getColNumber()+"\" name=\""+this.name+"\" width=\""+this.width+"\"/>";
        return xml;
    }
}
