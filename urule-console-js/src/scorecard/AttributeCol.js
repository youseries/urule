/**
 * Created by Jacky.gao on 2016/9/21.
 */
import Col from './Col.js';
export default class AttributeCol extends Col{
    constructor(table,name,width,variableCategory){
        super(table);
        this.name=name;
        this.width=width;
        this.variableCategory=variableCategory;
        this.type='attribute';
        this.init();
    }
    init(){
        this.td=$(`<td class="dropdown" style="width: ${this.width}px;padding-right: 0;background: #3c763d;color: #ffffff;border:1px solid #607D8B"></td>`);
        const container=$(`<span style="cursor: pointer" class="dropdown-toggle" data-toggle="dropdown"></span>`);
        this.objectContainer=$(`<span></span>`);
        if(this.variableCategory){
            URule.setDomContent(this.objectContainer,this.name+" ["+this.variableCategory+"]");
        }else{
            URule.setDomContent(this.objectContainer,this.name);
        }
        container.append(this.objectContainer);
        container.append(` <span class="caret"></span>`);
        this.td.append(container);
        this.td.append(this.buildColResizeTrigger());
        this.scoreCardTable.headerRow.append(this.td);
        this.initMenu([]);
        window._VariableValueArray.push(this);
        this.bindColResize();
    }

    initMenu(data){
        const _this=this;
        if(this.menuDef){
            this.menuDef.empty();
        }else{
            this.menuDef=$(`<ul class="dropdown-menu" role="menu"></ul>`);
            this.td.append(this.menuDef);
            $('.dropdown-toggle').dropdown();
        }
        for(let categories of data){
            for(let category of categories){
                if(!this.category && this.variableCategory){
                    if(category.name===this.variableCategory){
                        this.category=category;
                        const variables=category.variables;
                        for(let attributeRow of _this.scoreCardTable.attributeRows){
                            if(!attributeRow.attributeCell.variableLabel){
                                continue;
                            }
                            for(let variable of variables){
                                if(variable.label === attributeRow.attributeCell.variableLabel && variable.name === attributeRow.attributeCell.variableName){
                                    attributeRow.attributeCell.variable=variable;
                                    break;
                                }
                            }
                            attributeRow.attributeCell.refreshAttributeCellMenus(variables);
                        }
                    }
                }
                const menuItem=$(`<li><a href="###">${category.name}</a></li>`);
                this.menuDef.append(menuItem);
                menuItem.click(function () {
                    _this.category=category;
                    URule.setDomContent(_this.objectContainer,_this.name+" ["+category.name+"]");
                    for(let attributeRow of _this.scoreCardTable.attributeRows){
                        attributeRow.attributeCell.refreshAttributeCellMenus(category.variables);
                        attributeRow.attributeCell.variable=null;
                        URule.setDomContent(attributeRow.attributeCell.propContainer,"请选择属性");
                    }
                });
            }
        }
    }
    toXml(){
        if(!this.category){
            throw "请先定义属性对象";
        }
        let xml=" attr-col-width=\""+this.width+"\" attr-col-name=\""+this.name+"\" attr-col-category=\""+this.category.name+"\"";
        return xml;
    }
}
