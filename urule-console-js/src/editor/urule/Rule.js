/**
 * @author GJ
 */
import {MsgBox} from 'flowdesigner';

urule.Rule=function(parent,container,data){
	this.uuid=Math.uuid();
	this.namedMap=new Map();
	this.namedReferenceValues=[];
	container.prop('id',this.uuid);
	this.parent=parent;
	this.container=container;
	this.data=data;
	this.actions=[];
	this.elseActions=[];
	this.properties=[];
	this.init();
	this.initData();
};
urule.Rule.prototype.init=function(){
	this.ruleContainer=$("<div>");
	this.container.append(this.ruleContainer);
    this.initRemark();
	this.initHeader();
	this.initIf();
	this.initThen();
	this.initElse();
};
urule.Rule.prototype.initData=function(){
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
};
urule.Rule.prototype.initTopJoin=function(){
	var context=new urule.Context(this.conditionContainer,this);
	this.join=new urule.Join(context);
	this.join.init(null);
	this.join.initTopJoin(this.conditionContainer);
	this.join.setType("and");	
};
urule.Rule.prototype.initCriterion=function(criterion){
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
urule.Rule.prototype.addProperty=function(property){
	this.propertyContainer.append(property.getContainer());
	this.properties.push(property);
	window._setDirty();
};
urule.Rule.prototype.initRemark=function(){
	var remarkContainer=$("<div></div>");
    this.remark=new Remark(remarkContainer);
    this.ruleContainer.append(remarkContainer);
};
urule.Rule.prototype.initHeader=function(){
	this.nameContainer=$("<div></div>");
	this.ruleContainer.append(this.nameContainer);
	this.label=$("<span style='line-height:30px'><Strong>规则 <Strong></span>");
	this.nameContainer.append(this.label);
	this.nameEditor=$(`<input type='text' class="form-control rule-text-editor">`).hide();
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
		var value=self.nameEditor.prop("value");
		if(!value || value.length<2){
			bootbox.alert("规则名不合法");
			return;
		}
		self.name=value;
		self.nameEditor.hide();
		self.nameLabel.show();
		URule.setDomContent(self.nameLabel,self.name);
		window._setDirty();
	});
	this.nameEditor.hide();
	var del=$(`<i class="glyphicon glyphicon-remove rule-delete"></i>`);
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
		},{
			label:"允许循环触发",
			name:"loop",
			defaultValue:false,
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

urule.Rule.prototype.initIf=function(){
	this.ifLabel=$("<div><strong>如果</strong></div>");
	this.ruleContainer.append(this.ifLabel);
	this.conditionContainer=$("<div>");
	this.conditionContainer.css({
		height:"40px",
		position:"relative"
	});
	this.ruleContainer.append(this.conditionContainer);
};
urule.Rule.prototype.initThen=function(){
	this.thenLabel=$("<span><strong>那么</strong></span>");
	this.ruleContainer.append(this.thenLabel);
	this.actionContainer=$("<div style='padding: 5px;'>");
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
urule.Rule.prototype.initElse=function(){
	this.elseContainer=$("<div style='margin-top: 5px;'>");
	this.ruleContainer.append(this.elseContainer);
	this.elseLabel=$("<span><strong>否则</strong></span>");
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
urule.Rule.prototype.addAction=function(data,iselse){
	var self=this;
	var actionDiv=$("<div class='rule-action'>");
	var del=$(`<i class="glyphicon glyphicon-remove rule-delete-action"></i>`);
	actionDiv.append(del);
	var action=null;
	if(!iselse){
		action=new urule.ActionType(actionDiv,this);
	}else{
		action=new urule.ActionType(actionDiv);
	}
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
urule.Rule.prototype.toXml=function(){
	var xml="<rule name=\""+this.name+"\"";
	for(var i=0;i<this.properties.length;i++){
		var prop=this.properties[i];
		xml+=" "+prop.toXml();
	}
	xml+=">";
    xml+=this.remark.toXml();
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
	xml+="</rule>";
	return xml;
};