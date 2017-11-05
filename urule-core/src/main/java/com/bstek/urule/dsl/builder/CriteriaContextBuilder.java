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
package com.bstek.urule.dsl.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.urule.RuleException;
import com.bstek.urule.dsl.DSLUtils;
import com.bstek.urule.dsl.RuleParserParser.ActionParametersContext;
import com.bstek.urule.dsl.RuleParserParser.BeanMethodContext;
import com.bstek.urule.dsl.RuleParserParser.CommonFunctionContext;
import com.bstek.urule.dsl.RuleParserParser.ComplexValueContext;
import com.bstek.urule.dsl.RuleParserParser.ConditionLeftContext;
import com.bstek.urule.dsl.RuleParserParser.ExpAllContext;
import com.bstek.urule.dsl.RuleParserParser.ExpCollectContext;
import com.bstek.urule.dsl.RuleParserParser.ExpEvalContext;
import com.bstek.urule.dsl.RuleParserParser.ExpExistsContext;
import com.bstek.urule.dsl.RuleParserParser.ExprConditionContext;
import com.bstek.urule.dsl.RuleParserParser.ExpressionBodyContext;
import com.bstek.urule.dsl.RuleParserParser.FunctionInvokeContext;
import com.bstek.urule.dsl.RuleParserParser.JoinContext;
import com.bstek.urule.dsl.RuleParserParser.MethodInvokeContext;
import com.bstek.urule.dsl.RuleParserParser.NullValueContext;
import com.bstek.urule.dsl.RuleParserParser.ParameterContext;
import com.bstek.urule.dsl.RuleParserParser.PercentContext;
import com.bstek.urule.dsl.RuleParserParser.PropertyContext;
import com.bstek.urule.dsl.RuleParserParser.SingleConditionContext;
import com.bstek.urule.dsl.RuleParserParser.VariableContext;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rule.Op;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.lhs.AllLeftPart;
import com.bstek.urule.model.rule.lhs.CollectLeftPart;
import com.bstek.urule.model.rule.lhs.CollectPurpose;
import com.bstek.urule.model.rule.lhs.CommonFunctionLeftPart;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.EvalLeftPart;
import com.bstek.urule.model.rule.lhs.ExistLeftPart;
import com.bstek.urule.model.rule.lhs.FunctionLeftPart;
import com.bstek.urule.model.rule.lhs.JunctionType;
import com.bstek.urule.model.rule.lhs.Left;
import com.bstek.urule.model.rule.lhs.LeftPart;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.model.rule.lhs.MethodLeftPart;
import com.bstek.urule.model.rule.lhs.MultiCondition;
import com.bstek.urule.model.rule.lhs.PropertyCriteria;
import com.bstek.urule.model.rule.lhs.StatisticType;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;

/**
 * @author Jacky.gao
 * @since 2015年2月15日
 */
public class CriteriaContextBuilder extends AbstractContextBuilder implements ApplicationContextAware{
	private Collection<FunctionDescriptor> functionDescriptors;
	@Override
	public Criteria build(ParserRuleContext context) {
		SingleConditionContext ctx=(SingleConditionContext)context;
		ConditionLeftContext conditionLeftContext=ctx.conditionLeft();
		VariableContext variableContext=conditionLeftContext.variable();
		ParameterContext parameterContext=conditionLeftContext.parameter();
		FunctionInvokeContext functionInvokeContext=conditionLeftContext.functionInvoke();
		CommonFunctionContext commonFunctionContext=conditionLeftContext.commonFunction();
		MethodInvokeContext methodInvokeContext=conditionLeftContext.methodInvoke();
		ExpEvalContext expEvalContext=conditionLeftContext.expEval();
		ExpAllContext expAllContext=conditionLeftContext.expAll();
		ExpExistsContext expExistsContext=conditionLeftContext.expExists();
		ExpCollectContext expCollectContext=conditionLeftContext.expCollect();
		
		Criteria criteria=new Criteria();
		Left left=new Left();
		LeftPart leftPart=null;
		String variableCategory=null;
		String variableLabel=null;
		if(variableContext!=null){
			variableCategory=variableContext.variableCategory().Identifier().getText();
			variableLabel=variableContext.property().getText();
			VariableLeftPart part = new VariableLeftPart();
			part.setVariableCategory(variableCategory);
			part.setVariableLabel(variableLabel);
			left.setType(LeftType.variable);
			leftPart=part;
		}else if(parameterContext!=null){
			variableCategory=VariableCategory.PARAM_CATEGORY;
			variableLabel=parameterContext.Identifier().getText();
			VariableLeftPart part = new VariableLeftPart();
			part.setVariableCategory(variableCategory);
			part.setVariableLabel(variableLabel);
			left.setType(LeftType.variable);
			leftPart=part;
		}else if(functionInvokeContext!=null){
			FunctionLeftPart part=new FunctionLeftPart();
			String name=functionInvokeContext.Identifier().getText();
			ActionParametersContext parametersContext = functionInvokeContext.actionParameters();
			if(parametersContext!=null){
				List<Parameter> parameters=new ArrayList<Parameter>();
				for(ComplexValueContext complexValueContext:parametersContext.complexValue()){
					Parameter parameter=new Parameter();
					parameter.setValue(BuildUtils.buildValue(complexValueContext));
					parameters.add(parameter);
				}
				part.setParameters(parameters);
			}
			part.setName(name);
			left.setType(LeftType.function);
			leftPart=part;
		}else if(commonFunctionContext!=null){
			CommonFunctionLeftPart part=new CommonFunctionLeftPart();
			String nameorlabel=commonFunctionContext.Identifier().getText();
			for(FunctionDescriptor fun:functionDescriptors){
				if(nameorlabel.equals(fun.getName())){
					part.setName(fun.getName());
					part.setLabel(fun.getLabel());
					break;
				}else if(nameorlabel.equals(fun.getLabel())){
					part.setName(fun.getName());
					part.setLabel(fun.getLabel());
					break;
				}
			}
			if(part.getName()==null){
				throw new RuleException("Function["+nameorlabel+"] not exist.");
			}
			ComplexValueContext value=commonFunctionContext.complexValue();
			CommonFunctionParameter param=new CommonFunctionParameter();
			param.setObjectParameter(BuildUtils.buildValue(value));
			PropertyContext propertyContext=commonFunctionContext.property();
			if(propertyContext!=null){
				param.setProperty(propertyContext.getText());
			}
			part.setParameter(param);
			left.setType(LeftType.commonfunction);
			leftPart=part;
		}else if(methodInvokeContext!=null){
			MethodLeftPart part=new MethodLeftPart();
			BeanMethodContext beanMethodContext=methodInvokeContext.beanMethod();
			String beanLabel=beanMethodContext.Identifier(0).getText();
			String methodLabel=beanMethodContext.Identifier(1).getText();
			part.setBeanLabel(beanLabel);
			part.setMethodLabel(methodLabel);
			ActionParametersContext parametersContext=methodInvokeContext.actionParameters();
			if(parametersContext!=null){
				List<Parameter> parameters=new ArrayList<Parameter>();
				for(ComplexValueContext complexValueContext:parametersContext.complexValue()){
					Parameter parameter=new Parameter();
					parameter.setValue(BuildUtils.buildValue(complexValueContext));
					parameters.add(parameter);
				}
				part.setParameters(parameters);
			}
			left.setType(LeftType.method);
			leftPart=part;
		}else if(expEvalContext!=null){
			EvalLeftPart part=new EvalLeftPart();
			ExpressionBodyContext bodyContext=expEvalContext.expressionBody();
			part.setExpression(bodyContext.getText());
			left.setType(LeftType.eval);
			leftPart=part;
		}else if(expAllContext!=null){
			AllLeftPart part=new AllLeftPart();
			VariableContext vc=expAllContext.variable();
			ParameterContext pc=expAllContext.parameter();
			if(vc!=null){
				part.setVariableCategory(vc.variableCategory().getText());
				part.setVariableLabel(vc.property().getText());				
			}else if(pc!=null){
				part.setVariableCategory(VariableCategory.PARAM_CATEGORY);
				part.setVariableLabel(pc.Identifier().getText());				
			}
			TerminalNode numberNode=expAllContext.NUMBER();
			PercentContext percentContext=expAllContext.percent();
			if(numberNode!=null){
				part.setAmount(Integer.valueOf(numberNode.getText()));
				part.setStatisticType(StatisticType.amount);
			}else if(percentContext!=null){
				part.setPercent(Integer.valueOf(percentContext.NUMBER().getText()));
				part.setStatisticType(StatisticType.percent);
			}else{
				part.setStatisticType(StatisticType.none);				
			}
			ExprConditionContext conditionContext=expAllContext.exprCondition();
			MultiCondition condition=buildMultiCondition(conditionContext);
			part.setMultiCondition(condition);
			left.setType(LeftType.all);
			leftPart=part;
		}else if(expExistsContext!=null){
			ExistLeftPart part=new ExistLeftPart();
			VariableContext vc=expExistsContext.variable();
			ParameterContext pc=expExistsContext.parameter();
			if(vc!=null){
				part.setVariableCategory(vc.variableCategory().getText());
				part.setVariableLabel(vc.property().getText());				
			}else if(pc!=null){
				part.setVariableCategory(VariableCategory.PARAM_CATEGORY);
				part.setVariableLabel(pc.Identifier().getText());				
			}
			TerminalNode numberNode=expExistsContext.NUMBER();
			PercentContext percentContext=expExistsContext.percent();
			if(numberNode!=null){
				part.setAmount(Integer.valueOf(numberNode.getText()));
				part.setStatisticType(StatisticType.amount);
			}else if(percentContext!=null){
				part.setPercent(Integer.valueOf(percentContext.NUMBER().getText()));
				part.setStatisticType(StatisticType.percent);
			}else{
				part.setStatisticType(StatisticType.none);				
			}
			ExprConditionContext conditionContext=expExistsContext.exprCondition();
			MultiCondition condition=buildMultiCondition(conditionContext);
			part.setMultiCondition(condition);
			left.setType(LeftType.exist);
			leftPart=part;
		}else if(expCollectContext!=null){
			CollectLeftPart part=new CollectLeftPart();
			VariableContext vc=expCollectContext.variable();
			ParameterContext pc=expCollectContext.parameter();
			if(vc!=null){
				part.setVariableCategory(vc.variableCategory().getText());
				part.setVariableLabel(vc.property().getText());				
			}else if(pc!=null){
				part.setVariableCategory(VariableCategory.PARAM_CATEGORY);
				part.setVariableLabel(pc.Identifier().getText());				
			}
			ExprConditionContext conditionContext=expCollectContext.exprCondition();
			if(conditionContext!=null){
				MultiCondition condition=buildMultiCondition(conditionContext);
				part.setMultiCondition(condition);
			}
			if(expCollectContext.property()!=null){
				part.setProperty(expCollectContext.property().getText());
				if(expCollectContext.SUM()!=null){
					part.setPurpose(CollectPurpose.sum);				
				}else if(expCollectContext.MAX()!=null){
					part.setPurpose(CollectPurpose.max);
				}else if(expCollectContext.MIN()!=null){
					part.setPurpose(CollectPurpose.min);
				}else if(expCollectContext.AVG()!=null){
					part.setPurpose(CollectPurpose.avg);
				}
			}else{
				part.setPurpose(CollectPurpose.count);				
			}
			left.setType(LeftType.collect);
			leftPart=part;
		}
		left.setLeftPart(leftPart);
		criteria.setLeft(left);
		Op op=DSLUtils.parseOp(ctx.op());
		criteria.setOp(op);
		NullValueContext nullValueContext=ctx.nullValue();
		if(nullValueContext!=null){
			if(op.equals(Op.Equals)){
				criteria.setOp(Op.Null);
			}else if(op.equals(Op.NotEquals)){
				criteria.setOp(Op.NotNull);				
			}else{
				throw new RuleException("'null' value only support '==' or '!=' operator.");
			}
		}else{
			criteria.setValue(BuildUtils.buildValue(ctx.complexValue()));			
		}
		return criteria;
	}

	private MultiCondition buildMultiCondition(ExprConditionContext conditionContext){
		MultiCondition multiCondition=new MultiCondition();
		buildPropertyCriteria(conditionContext,multiCondition);
		return multiCondition;
	}

	private void buildPropertyCriteria(ExprConditionContext conditionContext,MultiCondition multiCondition) {
		List<JoinContext> joins=conditionContext.join();
		if(joins==null || joins.size()==0){
			multiCondition.addCondition(newPropertyCriteria(conditionContext));
		}else{
			JoinContext joinContext=joins.get(0);
			if(joinContext.AND()!=null){
				multiCondition.setType(JunctionType.and);
			}else{
				multiCondition.setType(JunctionType.or);				
			}
			List<ParseTree> children=conditionContext.children;
			for(ParseTree parseTree:children){
				if(parseTree instanceof ExprConditionContext){
					ExprConditionContext ecc=(ExprConditionContext)parseTree;
					if(ecc.property()==null){
						buildPropertyCriteria(ecc,multiCondition);
					}else{
						multiCondition.addCondition(newPropertyCriteria(ecc));						
					}
				}
			}			
		}
	}

	private PropertyCriteria newPropertyCriteria(ExprConditionContext conditionContext) {
		String property=conditionContext.property().getText();
		PropertyCriteria pc=new PropertyCriteria();
		pc.setProperty(property);
		Op op=DSLUtils.parseOp(conditionContext.op());
		pc.setOp(op);
		ComplexValueContext complexValueContext=conditionContext.complexValue();
		NullValueContext nullValueContext=conditionContext.nullValue();
		if(nullValueContext!=null && !op.equals(Op.Equals) && !op.equals(Op.NotEquals)){
			throw new RuleException("'$null' value only support '==' or '!=' operator.");
		}else{
			pc.setValue(BuildUtils.buildValue(complexValueContext));
		}
		return pc;
	}
	
	@Override
	public boolean support(ParserRuleContext context) {
		return context instanceof SingleConditionContext;
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		functionDescriptors=applicationContext.getBeansOfType(FunctionDescriptor.class).values();
	}
}
