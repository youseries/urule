/**
 * Created by Jacky.gao on 2016/9/20.
 */
export default class TableAction{
    constructor(container){
        this.container=container;
        this.initScoringSetting();
        this.initAssignSetting();
    }

    initData(data){
        const scoringType=data.scoringType;
        if(scoringType){
            this.scoringSettingSelect.val(scoringType);
            this.scoringType=scoringType;
            if(scoringType==='custom'){
                this.customContainer.show();
                this.customScoringBean=data.scoringBean;
                this.customBeanEditor.val(this.customScoringBean);
            }
        }
        const assignTargetType=data.assignTargetType;
        if(assignTargetType){
            this.assignTargetType=assignTargetType;
            if(assignTargetType==='variable'){
                this.variableTarget.getContainer().show();
                this.variableTarget.setValue(data);
                URule.setDomContent(this.assignTargetContainer,".");
                this.assignTargetContainer.css({
                    "color":"white"
                });
            }else if(assignTargetType==='parameter'){
                this.parameterTarget.getContainer().show();
                this.parameterTarget.setValue(data);
                URule.setDomContent(this.assignTargetContainer,".");
                this.assignTargetContainer.css({
                    "color":"white"
                });
            }else{
                URule.setDomContent(this.assignTargetContainer,"不赋值");
                this.assignTargetContainer.css({
                    "color":"#999"
                });
            }
        }
    }

    initScoringSetting(){
        const scoringSettingContainer=$(`<div style="margin: 5px;">得分计算方式：</div>`);
        this.container.append(scoringSettingContainer);
        this.scoringSettingSelect=$(`<select class="form-control" style="display: inline-block;width:120px;height:30px;padding: 3px;"></select>`);
        scoringSettingContainer.append(this.scoringSettingSelect);
        this.scoringSettingSelect.append(`<option value="sum">求和</option>`);
        this.scoringSettingSelect.append(`<option value="weightsum">加权求和</option>`);
        this.scoringSettingSelect.append(`<option value="custom">自定义</option>`);
        this.customContainer=$(`<span style="margin: 15px;">自定义计算得分的Bean ID：</span>`);
        scoringSettingContainer.append(this.customContainer);
        this.customContainer.hide();
        this.customBeanEditor=$(`<input type="text" class="form-control" style="width: 200px;display: inline-block">`);
        this.customContainer.append(this.customBeanEditor);
        const _this=this;
        this.customBeanEditor.change(function () {
           _this.customScoringBean=$(this).val();
        });

        this.scoringSettingSelect.change(function () {
            _this.scoringType=$(this).val();
            if(_this.scoringType==='custom'){
                _this.customContainer.show();
            }else{
                _this.customContainer.hide();
            }
        });
    }
    initAssignSetting(){
        const assignSettingContainer=$(`<div style="margin: 15px 5px">将得分值赋给：</div>`);
        this.container.append(assignSettingContainer);
        this.assignTargetContainer=generateContainer();
        assignSettingContainer.append(this.assignTargetContainer);
        URule.setDomContent(this.assignTargetContainer,"请选择值类型");
        this.assignTargetContainer.css({
            "color":"blue"
        });
        this.variableTarget=new urule.VariableValue(null,null,"Out");
        this.parameterTarget=new urule.ParameterValue(null,null,"Out");
        this.variableTarget.getContainer().hide();
        this.parameterTarget.getContainer().hide();
        assignSettingContainer.append(this.variableTarget.getContainer());
        assignSettingContainer.append(this.parameterTarget.getContainer());
        var self=this;
        self.menu=new URule.menu.Menu({
            menuItems:[{
                label:"选择变量",
                onClick:function(){
                    self.parameterTarget.getContainer().hide();
                    self.variableTarget.getContainer().show();
                    self.assignTargetType="variable";
                    URule.setDomContent(self.assignTargetContainer,".");
                    self.assignTargetContainer.css({
                        "color":"white"
                    });
                }
            },{
                label:"选择参数",
                onClick:function(){
                    self.variableTarget.getContainer().hide();
                    self.parameterTarget.getContainer().show();
                    self.assignTargetType="parameter";
                    URule.setDomContent(self.assignTargetContainer,".");
                    self.assignTargetContainer.css({
                        "color":"white"
                    });
                }
            },{
                label:"不赋值",
                onClick:function(){
                    self.variableTarget.getContainer().hide();
                    self.parameterTarget.getContainer().hide();
                    self.assignTargetType="none";
                    URule.setDomContent(self.assignTargetContainer,"不赋值");
                    self.assignTargetContainer.css({
                        "color":"#999"
                    });
                }
            }]
        });
        this.assignTargetContainer.click(function(e){
            self.menu.show(e);
        });
    }
    toXml(){
        if(!this.scoringType){
            throw "请选择得分计算方式";
        }
        if(!this.assignTargetType){
            throw "请选择得分赋值对象";
        }
        if(this.scoringType==='custom' && (!this.customScoringBean || this.customScoringBean.length<1)){
            throw "请输入自定义计算得分的Bean ID";
        }
        let xml=" scoring-type=\""+this.scoringType+"\" assign-target-type=\""+this.assignTargetType+"\" ";
        if(this.assignTargetType==='variable'){
            xml+= this.variableTarget.toXml();
        }else if(this.assignTargetType==='parameter'){
            xml+= this.parameterTarget.toXml();
        }
        if(this.scoringType==='custom'){
            xml+=" custom-scoring-bean=\""+this.customScoringBean+"\"";
        }
        return xml;
    }
}