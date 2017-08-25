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

import java.util.Collection;

import org.antlr.v4.runtime.ParserRuleContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.urule.RuleException;
import com.bstek.urule.action.Action;
import com.bstek.urule.action.ConsolePrintAction;
import com.bstek.urule.action.ExecuteCommonFunctionAction;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.action.VariableAssignAction;
import com.bstek.urule.dsl.RuleParserParser.ActionContext;
import com.bstek.urule.dsl.RuleParserParser.ActionParametersContext;
import com.bstek.urule.dsl.RuleParserParser.AssignActionContext;
import com.bstek.urule.dsl.RuleParserParser.BeanMethodContext;
import com.bstek.urule.dsl.RuleParserParser.CommonFunctionContext;
import com.bstek.urule.dsl.RuleParserParser.ComplexValueContext;
import com.bstek.urule.dsl.RuleParserParser.MethodInvokeContext;
import com.bstek.urule.dsl.RuleParserParser.NamedVariableContext;
import com.bstek.urule.dsl.RuleParserParser.OutActionContext;
import com.bstek.urule.dsl.RuleParserParser.ParameterContext;
import com.bstek.urule.dsl.RuleParserParser.PropertyContext;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import com.bstek.urule.model.rule.lhs.LeftType;

/**
 * @author Jacky.gao
 * @since 2015年2月15日
 */
public class ActionContextBuilder extends AbstractContextBuilder implements ApplicationContextAware{
	private Collection<FunctionDescriptor> functionDescriptors;
	@Override
	public Action build(ParserRuleContext context) {
		ActionContext ctx=(ActionContext)context;
		if(ctx.outAction()!=null){
			return buildConsolePrintAction(ctx.outAction());
		}else if(ctx.assignAction()!=null){
			return buildVariableAssignAction(ctx.assignAction());
		}else if(ctx.methodInvoke()!=null){
			return buildExecuteMethodAction(ctx.methodInvoke());
		}else if(ctx.commonFunction()!=null){
			return buildExecuteCommonFunctionAction(ctx.commonFunction());
		}
		return null;
	}
	
	private ExecuteCommonFunctionAction buildExecuteCommonFunctionAction(CommonFunctionContext context){
		ExecuteCommonFunctionAction action=new ExecuteCommonFunctionAction();
		String nameorlabel=context.Identifier().getText();
		for(FunctionDescriptor fun:functionDescriptors){
			if(nameorlabel.equals(fun.getName())){
				action.setName(fun.getName());
				action.setLabel(fun.getLabel());
				break;
			}else if(nameorlabel.equals(fun.getLabel())){
				action.setName(fun.getName());
				action.setLabel(fun.getLabel());
				break;
			}
		}
		if(action.getName()==null){
			throw new RuleException("Function["+nameorlabel+"] not exist.");
		}
		ComplexValueContext value=context.complexValue();
		CommonFunctionParameter param=new CommonFunctionParameter();
		param.setObjectParameter(BuildUtils.buildValue(value));
		PropertyContext propertyContext=context.property();
		if(propertyContext!=null){
			param.setProperty(propertyContext.getText());
		}
		action.setParameter(param);
		return action;
	}
		
	private ExecuteMethodAction buildExecuteMethodAction(MethodInvokeContext context){
		ExecuteMethodAction action=new ExecuteMethodAction();
		BeanMethodContext methodContext=context.beanMethod();
		action.setBeanLabel(methodContext.getChild(0).getText());
		action.setMethodLabel(methodContext.getChild(2).getText());
		ActionParametersContext parametersContext=context.actionParameters();
		if(parametersContext!=null){
			for(ComplexValueContext ctx:parametersContext.complexValue()){
				Parameter parameter=new Parameter();
				parameter.setValue(BuildUtils.buildValue(ctx));
				action.addParameter(parameter);
			}
		}
		return action;
	}
	
	private VariableAssignAction buildVariableAssignAction(AssignActionContext context){
		VariableAssignAction action=new VariableAssignAction();
		ParameterContext parameterContext=context.parameter();
		NamedVariableContext namedVariableContext=context.namedVariable();
		if(namedVariableContext!=null){
			action.setReferenceName(namedVariableContext.namedVariableCategory().getText());
			action.setVariableLabel(namedVariableContext.property().getText());
			action.setType(LeftType.NamedReference);
		}else if(parameterContext==null){
			action.setVariableCategory(context.variable().variableCategory().getText());
			action.setVariableLabel(context.variable().property().getText());			
		}else{
			action.setVariableCategory(VariableCategory.PARAM_CATEGORY);
			action.setVariableLabel(parameterContext.Identifier().getText());
		}
		action.setValue(BuildUtils.buildValue(context.complexValue()));
		return action;
	}
	
	private ConsolePrintAction buildConsolePrintAction(OutActionContext context){
		ConsolePrintAction action=new ConsolePrintAction();
		Value value=BuildUtils.buildValue(context.complexValue());
		action.setValue(value);
		return action;
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		functionDescriptors=applicationContext.getBeansOfType(FunctionDescriptor.class).values();
	}
	@Override
	public boolean support(ParserRuleContext context) {
		return context instanceof ActionContext;
	}
}
