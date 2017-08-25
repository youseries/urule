/**
 * Created by Jacky.gao on 2016/9/18.
 */
export default class Cell{
    constructor(row,col,cellData){
        row.cells.push(this);
        this.row=row;
        this.col=col;
        this.init(cellData);
    }
    init(cellData){
        this.td=$(`<td style="border:1px solid #607D8B"></td>`);
        this.initCell(cellData);
    }
    toXml(){
        let xml="<card-cell type=\""+this.type+"\" row=\""+this.row.getRowNumber()+"\" col=\""+this.col.getColNumber()+"\"";
        if(this.type==='attribute'){
            if(!this.variable){
                throw "请先选择属性";
            }
            if(this.row.scoreCardTable.weightSupport){
                if(!this.weight){
                    throw "请先定义["+this.variable.label+"]属性的权重值";
                }else{
                    xml+=" weight=\""+this.weight+"\"";
                }
            }
            xml+=" var=\""+this.variable.name+"\"";
            xml+=" var-label=\""+this.variable.label+"\"";
            xml+=" datatype=\""+this.variable.type+"\">";
        }else if(this.type==='condition'){
            xml+=">";
            if(!this.cellCondition){
                throw "请配置好条件.";
            }
            xml+=this.cellCondition.toXml();
        }else if(this.type==='score'){
            if(!this.inputType){
                throw "请配置好分值";
            }
            const contentXml=this.inputType.toXml();
            if(contentXml===''){
                throw "请配置好分值";
            }
            xml+=">";
            xml+=contentXml;
        }else if(this.type==='custom'){
            if(!this.inputType){
                throw "请配置好自定义值";
            }
            const contentXml=this.inputType.toXml();
            if(contentXml===''){
                throw "请配置好自定义值";
            }
            xml+=">";
            xml+=contentXml;
        }
        xml+="</card-cell>";
        return xml;
    }
};
