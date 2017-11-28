/**
 * Created by Jacky.gao on 2016/9/20.
 */
export default class PropertyConfig{
    constructor(container){
        this.container=container;
        this.properties=[];
        this.init();
    }
    init(){
        var self=this;
        this.namePropertyContainer=$("<span>名称：</span>");
        this.namePropertyContainer.css({
            "padding":"10px"
        });
        this.container.append(this.namePropertyContainer);
        this.nameEditor=$("<input type='text' class='form-control' style='display: inline-block;width:160px;height:28px'>");
        this.namePropertyContainer.append(this.nameEditor);
        this.nameEditor.change(function () {
            self.name=$(this).val();
            window._setDirty();
        });

        this.propertyContainer=$("<span>");
        this.propertyContainer.css({
            "padding":"10px"
        });
        var addProp=$("<button type='button' class='rule-add-property btn btn-link'>添加属性</button>");
        this.container.append(addProp);
        this.container.append(this.propertyContainer);
        var onClick=function(menuItem){
            var prop=new urule.RuleProperty(self,menuItem.name,menuItem.defaultValue,menuItem.editorType);
            self.addProperty(prop);
        };
        self.menu=new URule.menu.Menu({
            menuItems:[{
                label:"优先级",
                name:"salience",
                defaultValue:"10",
                editorType:1,
                onClick:onClick
            },{
                label:"生效日期",
                name:"effective-date",
                defaultValue:"",
                editorType:2,
                onClick:onClick
            },{
                label:"失效日期",
                name:"expires-date",
                defaultValue:"",
                editorType:2,
                onClick:onClick
            },{
                label:"是否启用",
                name:"enabled",
                defaultValue:true,
                editorType:3,
                onClick:onClick
            },{
                label:"允许调试信息输出",
                name:"debug",
                defaultValue:true,
                editorType:3,
                onClick:onClick
            }]
        });
        addProp.click(function(e){
            self.menu.show(e);
        });
    }
    initData(data){
        this.name=data.name;
        this.nameEditor.val(data.name);
        var salience=data["salience"];
        if(salience){
            this.addProperty(new urule.RuleProperty(this,"salience",salience,1));
        }
        var loop=data["loop"];
        if(loop!=null){
            this.addProperty(new urule.RuleProperty(this,"loop",loop,3));
        }
        var debug=data["debug"];
        if(debug!=null){
            this.addProperty(new urule.RuleProperty(this,"debug",debug,3));
        }
        var effectiveDate=data["effectiveDate"];
        if(effectiveDate){
            this.addProperty(new urule.RuleProperty(this,"effective-date",effectiveDate,2));
        }
        var expiresDate=data["expiresDate"];
        if(expiresDate){
            this.addProperty(new urule.RuleProperty(this,"expires-date",expiresDate,2));
        }
        var enabled=data["enabled"];
        if(enabled!=null){
            this.addProperty(new urule.RuleProperty(this,"enabled",enabled,3));
        }
    }
    addProperty(property){
        this.propertyContainer.append(property.getContainer());
        this.properties.push(property);
        window._setDirty();
    }
    toXml(){
        if(!this.name || this.name.length<1){
            throw "评分卡名称不能为空";
        }
        let xml=" name=\""+this.name+"\"";
        for(var i=0;i<this.properties.length;i++){
            var prop=this.properties[i];
            xml+=" "+prop.toXml();
        }
        return xml;
    }
}
