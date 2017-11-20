/*******************************************************************************
 * Copyright 2017 Bstek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.bstek.urule.dsl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;

import com.bstek.urule.Configure;
import com.bstek.urule.RuleException;
import com.bstek.urule.action.Action;
import com.bstek.urule.dsl.RuleParserParser.ActionContext;
import com.bstek.urule.dsl.RuleParserParser.AttributeContext;
import com.bstek.urule.dsl.RuleParserParser.ComplexValueContext;
import com.bstek.urule.dsl.RuleParserParser.ExpressionBodyContext;
import com.bstek.urule.dsl.RuleParserParser.FunctionImportContext;
import com.bstek.urule.dsl.RuleParserParser.JoinContext;
import com.bstek.urule.dsl.RuleParserParser.LeftContext;
import com.bstek.urule.dsl.RuleParserParser.LoopEndContext;
import com.bstek.urule.dsl.RuleParserParser.LoopRuleDefContext;
import com.bstek.urule.dsl.RuleParserParser.LoopStartContext;
import com.bstek.urule.dsl.RuleParserParser.LoopTargetContext;
import com.bstek.urule.dsl.RuleParserParser.MultiConditionsContext;
import com.bstek.urule.dsl.RuleParserParser.NamedConditionContext;
import com.bstek.urule.dsl.RuleParserParser.NamedConditionSetContext;
import com.bstek.urule.dsl.RuleParserParser.OtherContext;
import com.bstek.urule.dsl.RuleParserParser.ParenConditionsContext;
import com.bstek.urule.dsl.RuleParserParser.ResourceContext;
import com.bstek.urule.dsl.RuleParserParser.RightContext;
import com.bstek.urule.dsl.RuleParserParser.RuleDefContext;
import com.bstek.urule.dsl.RuleParserParser.RuleSetBodyContext;
import com.bstek.urule.dsl.RuleParserParser.RuleSetContext;
import com.bstek.urule.dsl.RuleParserParser.RuleSetHeaderContext;
import com.bstek.urule.dsl.RuleParserParser.RulesContext;
import com.bstek.urule.dsl.RuleParserParser.SingleConditionContext;
import com.bstek.urule.dsl.RuleParserParser.SingleNamedConditionSetContext;
import com.bstek.urule.dsl.builder.BuildUtils;
import com.bstek.urule.dsl.builder.ContextBuilder;
import com.bstek.urule.dsl.builder.NamedConditionBuilder;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.Other;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.CriteriaUnit;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.Lhs;
import com.bstek.urule.model.rule.lhs.NamedCriteria;
import com.bstek.urule.model.rule.lhs.Or;
import com.bstek.urule.model.rule.loop.LoopEnd;
import com.bstek.urule.model.rule.loop.LoopRule;
import com.bstek.urule.model.rule.loop.LoopStart;
import com.bstek.urule.model.rule.loop.LoopTarget;

/**
 * @author Jacky.gao
 * @since 2015年2月14日
 */
public class BuildRulesVisitor extends RuleParserBaseVisitor<Object> {
	private Map<ParseTree,Junction> map=new HashMap<ParseTree,Junction>();
	private Collection<ContextBuilder> builders;
	private NamedConditionBuilder namedConditionBuilder=new NamedConditionBuilder();
	private CommonTokenStream tokenStream;
	public BuildRulesVisitor(Collection<ContextBuilder> builders,CommonTokenStream tokenStream) {
		this.builders=builders;
		this.tokenStream=tokenStream;
	}
	
	@Override
	public RuleSet visitRuleSet(RuleSetContext ctx) {
		RuleSet ruleSet=new RuleSet();
		RuleSetHeaderContext ruleSetHeaderContext=ctx.ruleSetHeader();
		List<ResourceContext> resourcesContext=ruleSetHeaderContext.resource();
		if(resourcesContext!=null){
			for(ResourceContext context:resourcesContext){
				ruleSet.addLibrary(visitResource(context));
			}
		}
		StringBuffer sb=null;
		List<FunctionImportContext> functionImportContextList=ruleSetHeaderContext.functionImport();
		if(functionImportContextList!=null){
			sb=new StringBuffer();
			for(FunctionImportContext importContext:functionImportContextList){
				sb.append("import ");
				sb.append(importContext.packageDef().getText());
				sb.append(";");
			}
		}
		RuleSetBodyContext ruleSetBodyContext=ctx.ruleSetBody();
		List<RulesContext> rulesContextList=ruleSetBodyContext.rules();
		if(rulesContextList!=null){
			List<Rule> rules=new ArrayList<Rule>();
			ruleSet.setRules(rules);
			for(RulesContext ruleContext:rulesContextList){
				RuleDefContext ruleDefContext=ruleContext.ruleDef();
				if(ruleDefContext!=null){
					Rule rule=visitRuleDef(ruleDefContext);
					rules.add(rule);					
				}
				LoopRuleDefContext loopRuleDefContext=ruleContext.loopRuleDef();
				if(loopRuleDefContext!=null){
					LoopRule rule=visitLoopRuleDef(loopRuleDefContext);
					rules.add(rule);
				}
			}
		}
		return ruleSet;
	}
	
	@SuppressWarnings("unused")
	private String buildFunctionBody(ExpressionBodyContext expressionBodyContext){
		StringBuffer sb=new StringBuffer();
		for(ParseTree node:expressionBodyContext.children){
			Interval interval=node.getSourceInterval();
			int index=interval.a;
			List<Token> leftTokens=tokenStream.getHiddenTokensToLeft(index);
			if(leftTokens!=null){
				Token token=leftTokens.get(0);
				String text=token.getText();
				sb.append(text);
			}
			sb.append(node.getText());
			List<Token> rightTokens=tokenStream.getHiddenTokensToRight(index);
			if(rightTokens!=null){
				Token token=rightTokens.get(0);
				String text=token.getText();
				sb.append(text);
			}
		}
		return sb.toString();
	}
	
	@Override
	public Library visitResource(ResourceContext ctx) {
		return (Library)doBuilder(ctx);
	}
	
	@Override
	public LoopRule visitLoopRuleDef(LoopRuleDefContext ctx) {
		SimpleDateFormat sd=new SimpleDateFormat(Configure.getDateFormat());
		LoopRule rule=new LoopRule();
		String name=ctx.STRING().getText();
		name=name.substring(1,name.length()-1);
		rule.setName(name);
		
		LoopTargetContext target=ctx.loopTarget();
		ComplexValueContext valueContext=target.complexValue();
		LoopTarget loopTarget=new LoopTarget();
		loopTarget.setValue(BuildUtils.buildValue(valueContext));
		rule.setLoopTarget(loopTarget);
		
		LoopStartContext startContext=ctx.loopStart();
		if(startContext!=null){
			List<ActionContext> actionContextList=startContext.action();
			if(actionContextList!=null){
				LoopStart loopStart=new LoopStart();
				loopStart.setActions(buildActions(actionContextList));
				rule.setLoopStart(loopStart);
			}
		}
		
		LoopEndContext endContext=ctx.loopEnd();
		if(endContext!=null){
			List<ActionContext> actionContextList=endContext.action();
			if(actionContextList!=null){
				LoopEnd loopEnd=new LoopEnd();
				loopEnd.setActions(buildActions(actionContextList));
				rule.setLoopEnd(loopEnd);
			}
		}
		
		
		List<AttributeContext> attributesContext=ctx.attribute();
		if(attributesContext!=null){
			for(AttributeContext context:attributesContext){
				if(context.salienceAttribute()!=null){
					rule.setSalience(Integer.valueOf(context.salienceAttribute().NUMBER().getText()));
				}else if(context.loopAttribute()!=null){
					rule.setLoop(Boolean.valueOf(context.loopAttribute().Boolean().getText()));
				}else if(context.effectiveDateAttribute()!=null){
					try {
						String dateValue=context.effectiveDateAttribute().STRING().getText();
						dateValue=dateValue.substring(1,dateValue.length()-1);
						rule.setEffectiveDate(sd.parse(dateValue));
					} catch (ParseException e) {
						throw new RuleException(e);
					}
				}else if(context.expiresDateAttribute()!=null){
					try {
						String dateValue=context.expiresDateAttribute().STRING().getText();
						dateValue=dateValue.substring(1,dateValue.length()-1);
						rule.setExpiresDate(sd.parse(dateValue));
					} catch (ParseException e) {
						throw new RuleException(e);
					}
				}else if(context.enabledAttribute()!=null){
					rule.setEnabled(Boolean.valueOf(context.enabledAttribute().Boolean().getText()));
				}else if(context.debugAttribute()!=null){
					rule.setDebug(Boolean.valueOf(context.debugAttribute().Boolean().getText()));
				}else if(context.activationGroupAttribute()!=null){
					String value=context.activationGroupAttribute().STRING().getText();
					value=value.substring(1,value.length()-1);
					rule.setActivationGroup(value);
				}else if(context.agendaGroupAttribute()!=null){
					String value=context.agendaGroupAttribute().STRING().getText();
					value=value.substring(1,value.length()-1);
					rule.setAgendaGroup(value);
				}else if(context.autoFocusAttribute()!=null){
					rule.setAutoFocus(Boolean.valueOf(context.autoFocusAttribute().Boolean().getText()));
				}else if(context.ruleflowGroupAttribute()!=null){
					String value=context.ruleflowGroupAttribute().STRING().getText();
					value=value.substring(1,value.length()-1);
					rule.setRuleflowGroup(value);
				}
			}
		}
		LeftContext leftContext=ctx.left();
		ParseTree parseTree=leftContext.getChild(1);
		Lhs lhs=new Lhs();
		rule.setLhs(lhs);
		Criterion criterion = buildCriterion(parseTree);
		lhs.setCriterion(criterion);
		Rhs rhs=new Rhs();
		rhs.setActions(visitRight(ctx.right()));
		rule.setRhs(rhs);
		
		Other other=new Other();
		other.setActions(visitOther(ctx.other()));
		rule.setOther(other);
		return rule;
	}
	
	@Override
	public Rule visitRuleDef(RuleDefContext ctx) {
		SimpleDateFormat sd=new SimpleDateFormat(Configure.getDateFormat());
		Rule rule=new Rule();
		String name=ctx.STRING().getText();
		name=name.substring(1,name.length()-1);
		rule.setName(name);
		List<AttributeContext> attributesContext=ctx.attribute();
		if(attributesContext!=null){
			for(AttributeContext context:attributesContext){
				if(context.salienceAttribute()!=null){
					rule.setSalience(Integer.valueOf(context.salienceAttribute().NUMBER().getText()));
				}else if(context.loopAttribute()!=null){
					rule.setLoop(Boolean.valueOf(context.loopAttribute().Boolean().getText()));
				}else if(context.effectiveDateAttribute()!=null){
					try {
						String dateValue=context.effectiveDateAttribute().STRING().getText();
						dateValue=dateValue.substring(1,dateValue.length()-1);
						rule.setEffectiveDate(sd.parse(dateValue));
					} catch (ParseException e) {
						throw new RuleException(e);
					}
				}else if(context.expiresDateAttribute()!=null){
					try {
						String dateValue=context.expiresDateAttribute().STRING().getText();
						dateValue=dateValue.substring(1,dateValue.length()-1);
						rule.setExpiresDate(sd.parse(dateValue));
					} catch (ParseException e) {
						throw new RuleException(e);
					}
				}else if(context.enabledAttribute()!=null){
					rule.setEnabled(Boolean.valueOf(context.enabledAttribute().Boolean().getText()));
				}else if(context.debugAttribute()!=null){
					rule.setDebug(Boolean.valueOf(context.debugAttribute().Boolean().getText()));
				}else if(context.activationGroupAttribute()!=null){
					String value=context.activationGroupAttribute().STRING().getText();
					value=value.substring(1,value.length()-1);
					rule.setActivationGroup(value);
				}else if(context.agendaGroupAttribute()!=null){
					String value=context.agendaGroupAttribute().STRING().getText();
					value=value.substring(1,value.length()-1);
					rule.setAgendaGroup(value);
				}else if(context.autoFocusAttribute()!=null){
					rule.setAutoFocus(Boolean.valueOf(context.autoFocusAttribute().Boolean().getText()));
				}else if(context.ruleflowGroupAttribute()!=null){
					String value=context.ruleflowGroupAttribute().STRING().getText();
					value=value.substring(1,value.length()-1);
					rule.setRuleflowGroup(value);
				}
			}
		}
		LeftContext leftContext=ctx.left();
		ParseTree parseTree=leftContext.getChild(1);
		Lhs lhs=new Lhs();
		rule.setLhs(lhs);
		Criterion criterion = buildCriterion(parseTree);
		lhs.setCriterion(criterion);
		Rhs rhs=new Rhs();
		rhs.setActions(visitRight(ctx.right()));
		rule.setRhs(rhs);
		
		Other other=new Other();
		other.setActions(visitOther(ctx.other()));
		rule.setOther(other);
		return rule;
	}
	
	@Override
	public Criteria visitSingleCondition(SingleConditionContext ctx) {
		return (Criteria)doBuilder(ctx);
	}
	@Override
	public Criterion visitParenConditions(ParenConditionsContext ctx) {
		ParseTree parseTree=ctx.getChild(1);
		return buildCriterion(parseTree);
	}
	
	@Override
	public Criterion visitSingleNamedConditionSet(SingleNamedConditionSetContext ctx) {
		NamedCriteria criteria=new NamedCriteria();
		NamedConditionSetContext conditionSet=ctx.namedConditionSet();
		if(conditionSet.refName()!=null){
			criteria.setReferenceName(conditionSet.refName().getText());			
		}
		criteria.setVariableCategory(conditionSet.refObject().getText());
		NamedConditionContext namedConditionContext=conditionSet.namedCondition();
		CriteriaUnit unit=namedConditionBuilder.buildNamedCriteria(namedConditionContext, criteria.getVariableCategory());
		criteria.setUnit(unit);
		return criteria;
	}
	
	@Override
	public Criterion visitMultiConditions(MultiConditionsContext ctx) {
		Junction topJunction=null;
		Criterion criterion=null;
		Junction junction=map.get(ctx);
		int childCount=ctx.getChildCount();
		for(int i=0;i<childCount;i++){
			ParseTree parseTree=ctx.getChild(i);
			if(parseTree instanceof JoinContext){
				JoinContext joinContext=(JoinContext)parseTree;
				if(joinContext.AND()!=null){
					if(junction==null){
						junction=new And();
						topJunction=junction;
						junction.addCriterion(criterion);
					}else if(!(junction instanceof And)){
						And newAnd=new And();
						junction.addCriterion(newAnd);
						junction=newAnd;
					}
				}else{
					if(junction==null){
						junction=new Or();
						topJunction=junction;
						junction.addCriterion(criterion);
					}else if(!(junction instanceof Or)){
						Or newOr=new Or();
						junction.addCriterion(newOr);
						junction=newOr;
					}					
				}
			}else{
				boolean isMulti=false;
				if(parseTree instanceof MultiConditionsContext){
					isMulti=true;
				}
				if(junction!=null && isMulti){
					map.put(parseTree, junction);
				}
				criterion=buildCriterion(parseTree);
				if(junction!=null && !isMulti){
					junction.addCriterion(criterion);
				}
			}
		}
		if(topJunction!=null){
			return topJunction;
		}
		return criterion;
	}
	
	@Override
	public List<Action> visitRight(RightContext ctx) {
		if(ctx==null ||ctx.action()==null){
			return null;
		}
		List<ActionContext> actionContexts=ctx.action();
		
		return buildActions(actionContexts);
	}

	private List<Action> buildActions(List<ActionContext> actionContexts) {
		List<Action> actions=new ArrayList<Action>();
		for(ActionContext actionContext:actionContexts){
			Action action=(Action)doBuilder(actionContext);
			actions.add(action);
		}
		return actions;
	}
	
	@Override
	public List<Action> visitOther(OtherContext ctx) {
		if(ctx==null ||ctx.action()==null){
			return null;
		}
		List<Action> actions=new ArrayList<Action>();
		for(ActionContext actionContext:ctx.action()){
			Action action=(Action)doBuilder(actionContext);
			actions.add(action);
		}
		return actions;
	}
	
	private Criterion buildCriterion(ParseTree parseTree) {
		Criterion criterion=null;
		if(parseTree instanceof ParenConditionsContext){
			criterion=visitParenConditions((ParenConditionsContext)parseTree);			
		}else if(parseTree instanceof SingleConditionContext){
			criterion=visitSingleCondition((SingleConditionContext)parseTree);
		}else if(parseTree instanceof MultiConditionsContext){
			criterion=visitMultiConditions((MultiConditionsContext)parseTree);
		}else if(parseTree instanceof SingleNamedConditionSetContext){
			criterion=visitSingleNamedConditionSet((SingleNamedConditionSetContext)parseTree);
		}
		return criterion;
	}
	private Object doBuilder(ParserRuleContext context){
		for(ContextBuilder builder:builders){
			if(builder.support(context)){
				return builder.build(context);
			}
		}
		return null;
	}
}
