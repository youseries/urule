/**
 * @author GJ
 */
import {MsgBox} from 'flowdesigner';

urule.LoopRule=function(parent,container,data){
	this.uuid=Math.uuid();
	this.namedMap=new Map();
	this.namedReferenceValues=[];
	container.prop('id',this.uuid);
	this.parent=parent;
	this.container=container;
	this.data=data;
	this.actions=[];
	this.elseActions=[];
	this.loopStartActions=[];
	this.loopEndActions=[];
	this.properties=[];
	this.init();
	this.initData();
};
urule.LoopRule.prototype.init=function(){
	this.ruleContainer=$("<div>");
	this.container.append(this.ruleContainer);
	this.initRemark();
	this.initHeader();
    this.initLoopTarget();
    this.initLoopStart();
	this.initIf();
	this.initThen();
	this.initElse();
	this.initLoopEnd();
};
urule.LoopRule.prototype.initData=function(){
	if(!this.data){
		return;
	}
	this.name=this.data["name"];
	this.nameLabel.prop("innerText",this.name);
	var salience=this.data["salience"];
	if(salience){
		this.addProperty(new urule.RuleProperty(this,"salience",salience,1));
	}
	var loop=this.data["loop"];
	if(loop!=null){
		this.addProperty(new urule.RuleProperty(this,"loop",loop,3));
	}
	var effectiveDate=this.data["effectiveDate"];
	if(effectiveDate){
		this.addProperty(new urule.RuleProperty(this,"effective-date",effectiveDate,2));
	}
	var expiresDate=this.data["expiresDate"];
	if(expiresDate){
		this.addProperty(new urule.RuleProperty(this,"expires-date",expiresDate,2));
	}
	var enabled=this.data["enabled"];
	if(enabled!=null){
		this.addProperty(new urule.RuleProperty(this,"enabled",enabled,3));
	}
	var debug=this.data["debug"];
	if(debug!=null){
		this.addProperty(new urule.RuleProperty(this,"debug",debug,3));
	}
	var activationGroup=this.data["activationGroup"];
	if(activationGroup){
		this.addProperty(new urule.RuleProperty(this,"activation-group",activationGroup,1));
	}
	var agendaGroup=this.data["agendaGroup"];
	if(agendaGroup){
		this.addProperty(new urule.RuleProperty(this,"agenda-group",agendaGroup,1));
	}
	var autoFocus=this.data["autoFocus"];
	if(autoFocus!=null){
		this.addProperty(new urule.RuleProperty(this,"auto-focus",autoFocus,3));
	}
	var ruleflowGroup=this.data["ruleflowGroup"];
	if(ruleflowGroup){
		this.addProperty(new urule.RuleProperty(this,"ruleflow-group",autoFocus,1));
	}
    var remark=this.data["remark"];
    this.remark.setData(remark);

    var loopTarget=this.data["loopTarget"];
    if(loopTarget){
        var value=loopTarget.value;
        if(value){
            var valueType=value.valueType;
            this.loopTargetInputType.setValueType(valueType,value);
        }
    }

    var loopStart=this.data["loopStart"];
    if(loopStart){
        var actions=loopStart.actions;
        if(actions){
            for(var i=0;i<actions.length;i++){
                var action=actions[i];
                this.addLoopStartAction(action);
            }
        }
    }

	var lhs=this.data["lhs"];
	if(lhs){
		var criterion=lhs["criterion"];
		if(criterion){
			this.initCriterion(criterion);
		}else{
			this.initCriterion();			
		}
	}else{
		this.initCriterion();
	}
	var rhs=this.data["rhs"];
	if(rhs){
		var actions=rhs["actions"];
		if(actions){
			for(var i=0;i<actions.length;i++){
				var action=actions[i];
				this.addAction(action);
			}
		}
	}
	var elseData=this.data["other"];
	if(elseData){
		var actions=elseData["actions"];
		if(actions){
			for(var i=0;i<actions.length;i++){
				var action=actions[i];
				this.addAction(action,true);
			}
		}
	}

    var loopEnd=this.data["loopEnd"];
    if(loopEnd){
        var actions=loopEnd.actions;
        if(actions){
            for(var i=0;i<actions.length;i++){
                var action=actions[i];
                this.addLoopEndAction(action);
            }
        }
    }
};
urule.LoopRule.prototype.initTopJoin=function(){
	var context=new urule.Context(this.conditionContainer,this);
	this.join=new urule.Join(context);
	this.join.init(null);
	this.join.initTopJoin(this.conditionContainer);
	this.join.setType("and");	
};
urule.LoopRule.prototype.initCriterion=function(criterion){
	var context=new urule.Context(this.conditionContainer,this);
	this.join=new urule.Join(context);
	this.join.init(null);
	this.join.initTopJoin(this.conditionContainer);
	var junctionType="and";
	if(criterion){
		junctionType=criterion["junctionType"];		
	}
	this.join.setType(junctionType);
	if(criterion){		
		this.join.initData(criterion);
	}
};
urule.LoopRule.prototype.addProperty=function(property){
	this.propertyContainer.append(property.getContainer());
	this.properties.push(property);
	window._setDirty();
};

urule.LoopRule.prototype.initRemark=function(){
	var remarkContainer=$("<div></div>");
	this.remark=new Remark(remarkContainer);
	this.ruleContainer.append(remarkContainer);
};

urule.LoopRule.prototype.initHeader=function(){
	this.nameContainer=$("<div></div>");
	this.ruleContainer.append(this.nameContainer);
	this.label=$("<span style='line-height:30px;'><Strong>循环规则 <Strong></span>");
	this.nameContainer.append(this.label);
	this.nameEditor=$("<input type='text' class='form-control rule-text-editor'>").hide();
	this.name="rule";
	this.nameLabel=$("<span>"+this.name+"</span>");
	this.label.append(this.nameEditor);
	this.label.append(this.nameLabel);
	var self=this;
	this.nameLabel.click(function(){
		self.nameLabel.hide();
		self.nameEditor.show();
		self.nameEditor.prop("value",self.name);
		self.nameEditor.focus();
	});

	this.nameEditor.blur(function(){
		self.name=self.nameEditor.prop("value");
		self.nameEditor.hide();
		self.nameLabel.show();
		URule.setDomContent(self.nameLabel,self.name);
		window._setDirty();
	});
	this.nameEditor.hide();
	var del=$(`<i class="glyphicon glyphicon-remove rule-delete"></i>`);
	del.css({
		"vertical-align":"middle",
		"cursor":"pointer"
	});
	del.click(function(){
		const msg="真的要删除规则"+self.name+"？";
		MsgBox.confirm(msg,function(){
			var pos=self.parent.rules.indexOf(self);
			self.parent.rules.splice(pos,1);
			self.container.remove();
			window._setDirty();
		});
	});

	this.nameContainer.append(del);
	
	this.propertyContainer=$("<span>");
	this.propertyContainer.css({
		"padding":"10px"
	});
	
	var addProp=$("<span class='rule-add-property'>添加属性</span>");
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
		},{
			label:"互斥组",
			name:"activation-group",
			defaultValue:"",
			editorType:1,
			onClick:onClick
		},{
			label:"执行组",
			name:"agenda-group",
			defaultValue:"",
			editorType:1,
			onClick:onClick
		},{
			label:"自动获取焦点",
			name:"auto-focus",
			defaultValue:true,
			editorType:3,
			onClick:onClick
		}]
	});
	addProp.click(function(e){
		self.menu.show(e);
	});
	this.ruleContainer.append(addProp);
	this.ruleContainer.append(this.propertyContainer);
};

urule.LoopRule.prototype.initLoopStart=function(){
	this.loopStartLabel=$("<span><strong>开始前动作</strong></span>");
	this.ruleContainer.append(this.loopStartLabel);
	this.loopStartActionContainer=$("<div style='padding: 5px'>");
	const _this=this;
	this.loopStartActionContainer.sortable({
		delay: 200,
		update: function (event, ui) {
			var children=_this.loopStartActionContainer.children("div");
			children.each(function(index,div){
				let item=$(div),id=item.prop("id"),actions=_this.loopStartActions,targetAction=null;
				for(let action of actions){
					if(action.uuid===id){
						targetAction=action;
						break;
					}
				}
				if(targetAction){
					const pos=actions.indexOf(targetAction);
					actions.splice(pos,1);
					actions.splice(index,0,targetAction);
				}
			});
			window._setDirty();
		}
	});
	this.ruleContainer.append(this.loopStartActionContainer);
	this.addLoopStartActionButton=$("<span class='rule-add-action'>添加动作</span>");
	var self=this;
	this.addLoopStartActionButton.click(function(){
		self.addLoopStartAction();
	});
	this.loopStartLabel.append(this.addLoopStartActionButton);
};

urule.LoopRule.prototype.addLoopStartAction=function(data){
	var self=this;
	var actionDiv=$("<div style='padding: 5px'>");
	var del=$(`<i class="glyphicon glyphicon-remove rule-delete-action"></i>`);
	actionDiv.append(del);
	var action=new urule.ActionType(actionDiv);
	del.click(function(){
		var pos=self.loopStartActions.indexOf(action);
		self.loopStartActions.splice(pos, 1);
		actionDiv.remove();
		window._setDirty();
	});
	this.loopStartActions.push(action);
	this.loopStartActionContainer.append(actionDiv);
	if(data){
		action.initData(data);
	}
	window._setDirty();
};

urule.LoopRule.prototype.initIf=function(){
    this.ifLabel=$("<div style='margin-left: 5px;color:#337ab7'><strong>如果</strong></div>");
    this.ruleContainer.append(this.ifLabel);
    this.conditionContainer=$("<div style='margin-left: 5px'>");
    this.conditionContainer.css({
        height:"40px",
        position:"relative"
    });
    this.ruleContainer.append(this.conditionContainer);
};
urule.LoopRule.prototype.initLoopTarget=function(){
    this.ruleContainer.append("<div><strong>循环对象</strong></div>");
    this.loopTargetContainer=$("<div style='padding: 5px'>");
    this.ruleContainer.append(this.loopTargetContainer);
    this.loopTargetInputType = new urule.InputType();
    this.loopTargetContainer.append(this.loopTargetInputType.getContainer());
};

urule.LoopRule.prototype.initThen=function(){
	this.thenLabel=$("<span style='margin-left: 5px;color:#337ab7'><strong>那么</strong></span>");
	this.ruleContainer.append(this.thenLabel);
	this.actionContainer=$("<div style='padding: 5px'>");
	this.ruleContainer.append(this.actionContainer);

	const _this=this;
	this.actionContainer.sortable({
		delay: 200,
		update: function (event, ui) {
			var children=_this.actionContainer.children("div");
			children.each(function(index,div){
				let item=$(div),id=item.prop("id"),actions=_this.actions,targetAction=null;
				for(let action of actions){
					if(action.uuid===id){
						targetAction=action;
						break;
					}
				}
				if(targetAction){
					const pos=actions.indexOf(targetAction);
					actions.splice(pos,1);
					actions.splice(index,0,targetAction);
				}
			});
			window._setDirty();
		}
	});

	this.addActionButton=$("<span class='rule-add-action'>添加动作</span>");
	var self=this;
	this.addActionButton.click(function(){
		self.addAction();
	});
	this.thenLabel.append(this.addActionButton);
};
urule.LoopRule.prototype.initElse=function(){
	this.elseContainer=$("<div style='margin-top: 5px;'>");
	this.ruleContainer.append(this.elseContainer);
	this.elseLabel=$("<span style='margin-left: 5px;color:#337ab7'><strong>否则</strong></span>");
	this.elseContainer.append(this.elseLabel);
	this.elseActionContainer=$("<div style='padding: 5px'>");
	this.elseContainer.append(this.elseActionContainer);

	const _this=this;
	this.elseActionContainer.sortable({
		delay: 200,
		update: function (event, ui) {
			var children=_this.elseActionContainer.children("div");
			children.each(function(index,div){
				let item=$(div),id=item.prop("id"),actions=_this.elseActions,targetAction=null;
				for(let action of actions){
					if(action.uuid===id){
						targetAction=action;
						break;
					}
				}
				if(targetAction){
					const pos=actions.indexOf(targetAction);
					actions.splice(pos,1);
					actions.splice(index,0,targetAction);
				}
			});
			window._setDirty();
		}
	});

	this.addElseActionButton=$("<span class='rule-add-action'>添加动作</span>");
	var self=this;
	this.addElseActionButton.click(function(){
		self.addAction(null,true);
	});
	this.elseLabel.append(this.addElseActionButton);
};
urule.LoopRule.prototype.addAction=function(data,iselse){
	var self=this;
	var actionDiv=$("<div style='padding: 5px'>");
	var del=$(`<i class="glyphicon glyphicon-remove rule-delete-action"></i>`);
	actionDiv.append(del);
	var action=new urule.ActionType(actionDiv,this);
	del.click(function(){
		if(iselse){
			var pos=self.elseActions.indexOf(action);
			self.elseActions.splice(pos, 1);
		}else{
			var pos=self.actions.indexOf(action);
			self.actions.splice(pos, 1);
		}
		actionDiv.remove();
		window._setDirty();
	});
	if(iselse){
		this.elseActions.push(action);
		this.elseActionContainer.append(actionDiv);
	}else{
		this.actions.push(action);
		this.actionContainer.append(actionDiv);
	}
	if(data){
		action.initData(data);
	}
	window._setDirty();
};


urule.LoopRule.prototype.initLoopEnd=function(){
	this.loopEndLabel=$("<span><strong>结束后动作</strong></span>");
	this.ruleContainer.append(this.loopEndLabel);
	this.loopEndActionContainer=$("<div style='padding: 5px;'>");
	this.ruleContainer.append(this.loopEndActionContainer);

	const _this=this;
	this.loopEndActionContainer.sortable({
		delay: 200,
		update: function (event, ui) {
			var children=_this.loopEndActionContainer.children("div");
			children.each(function(index,div){
				let item=$(div),id=item.prop("id"),actions=_this.loopEndActions,targetAction=null;
				for(let action of actions){
					if(action.uuid===id){
						targetAction=action;
						break;
					}
				}
				if(targetAction){
					const pos=actions.indexOf(targetAction);
					actions.splice(pos,1);
					actions.splice(index,0,targetAction);
				}
			});
			window._setDirty();
		}
	});

	this.addLoopEndActionButton=$("<span class='rule-add-action'>添加动作</span>");
	var self=this;
	this.addLoopEndActionButton.click(function(){
		self.addLoopEndAction();
	});
	this.loopEndLabel.append(this.addLoopEndActionButton);
};

urule.LoopRule.prototype.addLoopEndAction=function(data){
	var self=this;
	var actionDiv=$("<div style='padding: 5px'>");
	var del=$(`<i class="glyphicon glyphicon-remove rule-delete-action"></i>`);
	actionDiv.append(del);
	var action=new urule.ActionType(actionDiv);
	del.click(function(){
		var pos=self.loopEndActions.indexOf(action);
		self.loopEndActions.splice(pos, 1);
		actionDiv.remove();
		window._setDirty();
	});
	this.loopEndActions.push(action);
	this.loopEndActionContainer.append(actionDiv);
	if(data){
		action.initData(data);
	}
	window._setDirty();
};

urule.LoopRule.prototype.toXml=function(){
	var xml="<loop-rule name=\""+this.name+"\"";
	for(var i=0;i<this.properties.length;i++){
		var prop=this.properties[i];
		xml+=" "+prop.toXml();
	}
	xml+=">";
    xml+=this.remark.toXml();
    var loopTargetValue=this.loopTargetInputType.toXml();
    if(loopTargetValue===""){
        throw "循环规则的【循环对象】必须要定义";
    }
    xml+="<loop-target>"+loopTargetValue+"</loop-target>";
    if(this.loopStartActions.length>0){
        xml+="<loop-start>";
        for(var i=0;i<this.loopStartActions.length;i++){
            var action=this.loopStartActions[i];
            xml+=action.toXml();
        }
        xml+="</loop-start>";
    }
	xml+="<if>";
	xml+=this.join.toXml();
	xml+="</if>";
	xml+="<then>";
	for(var i=0;i<this.actions.length;i++){
		var action=this.actions[i];
		xml+=action.toXml();
	}
	xml+="</then>";
	xml+="<else>";
	for(var i=0;i<this.elseActions.length;i++){
		var action=this.elseActions[i];
		xml+=action.toXml();
	}
	xml+="</else>";
    if(this.loopEndActions.length>0){
        xml+="<loop-end>";
        for(var i=0;i<this.loopEndActions.length;i++){
            var action=this.loopEndActions[i];
            xml+=action.toXml();
        }
        xml+="</loop-end>";
    }
	xml+="</loop-rule>";
	return xml;
};