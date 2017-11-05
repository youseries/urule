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

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.bstek.urule.RuleException;
import com.bstek.urule.Utils;
import com.bstek.urule.dsl.RuleParserParser.ActionParametersContext;
import com.bstek.urule.dsl.RuleParserParser.BeanMethodContext;
import com.bstek.urule.dsl.RuleParserParser.CommonFunctionContext;
import com.bstek.urule.dsl.RuleParserParser.ComplexValueContext;
import com.bstek.urule.dsl.RuleParserParser.ConstantContext;
import com.bstek.urule.dsl.RuleParserParser.MethodInvokeContext;
import com.bstek.urule.dsl.RuleParserParser.NamedVariableContext;
import com.bstek.urule.dsl.RuleParserParser.ParameterContext;
import com.bstek.urule.dsl.RuleParserParser.PropertyContext;
import com.bstek.urule.dsl.RuleParserParser.ValueContext;
import com.bstek.urule.dsl.RuleParserParser.VariableCategoryContext;
import com.bstek.urule.dsl.RuleParserParser.VariableContext;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.model.rule.AbstractValue;
import com.bstek.urule.model.rule.ArithmeticType;
import com.bstek.urule.model.rule.CommonFunctionValue;
import com.bstek.urule.model.rule.ComplexArithmetic;
import com.bstek.urule.model.rule.ConstantValue;
import com.bstek.urule.model.rule.MethodValue;
import com.bstek.urule.model.rule.NamedReferenceValue;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.ParameterValue;
import com.bstek.urule.model.rule.ParenValue;
import com.bstek.urule.model.rule.SimpleValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.VariableCategoryValue;
import com.bstek.urule.model.rule.VariableValue;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;

/**
 * @author Jacky.gao
 * @since 2016年6月1日
 */
public class BuildUtils {
	public static AbstractValue buildValue(ComplexValueContext context){
		AbstractValue value=null;
		if(context.leftParen()!=null){
			ParenValue pv=new ParenValue();
			List<ComplexValueContext> values=context.complexValue();
			Value v=buildValue(values.get(0));
			pv.setValue(v);
			value=pv;
		}else if(context.value()!=null){
			value=buildSimpleValue(context.value());
		}else if(context.variable()!=null){
			value=buildVariableValue(context.variable());
		}else if(context.constant()!=null){
			value=buildConstantValue(context.constant());
		}else if(context.variableCategory()!=null){
			VariableCategoryContext vcc=context.variableCategory();
			String name=vcc.Identifier().getText();
			value=new VariableCategoryValue(name);
		}else if(context.parameter()!=null){
			ParameterContext parameterContext=context.parameter();
			ParameterValue parameterValue=new ParameterValue();
			parameterValue.setVariableLabel(parameterContext.Identifier().getText());
			value=parameterValue;
		}else if(context.namedVariable()!=null){
			NamedVariableContext namedVariableContext=context.namedVariable();
			String refName=namedVariableContext.namedVariableCategory().getText();
			String property=namedVariableContext.property().getText();
			NamedReferenceValue refValue=new NamedReferenceValue();
			refValue.setReferenceName(refName);
			refValue.setPropertyLabel(property);
			value=refValue;
		}else if(context.methodInvoke()!=null){
			MethodInvokeContext actionContext=(MethodInvokeContext)context.methodInvoke();
			MethodValue mv=new MethodValue();
			BeanMethodContext beanMethodContext=actionContext.beanMethod();
			String beanLabel=beanMethodContext.Identifier(0).getText();
			String methodLabel=beanMethodContext.Identifier(1).getText();
			mv.setBeanLabel(beanLabel);
			mv.setMethodLabel(methodLabel);
			ActionParametersContext actionParametersContext=actionContext.actionParameters();
			if(actionParametersContext!=null && actionParametersContext.complexValue()!=null){
				List<ComplexValueContext> values=actionParametersContext.complexValue();
				List<Parameter> parameters=new ArrayList<Parameter>();
				for(ComplexValueContext cvx:values){
					Parameter parameter=new Parameter();
					parameter.setValue(buildValue(cvx));
					parameters.add(parameter);
				}
				mv.setParameters(parameters);
			}
			value=mv;
		}else if(context.commonFunction()!=null){
			CommonFunctionContext commonFunctionContext=context.commonFunction();
			Collection<FunctionDescriptor> functionDescriptors=Utils.getApplicationContext().getBeansOfType(FunctionDescriptor.class).values();
			CommonFunctionValue functionValue=new CommonFunctionValue();
			String nameorlabel=commonFunctionContext.Identifier().getText();
			for(FunctionDescriptor fun:functionDescriptors){
				if(nameorlabel.equals(fun.getName())){
					functionValue.setName(fun.getName());
					functionValue.setLabel(fun.getLabel());
					break;
				}else if(nameorlabel.equals(fun.getLabel())){
					functionValue.setName(fun.getName());;
					functionValue.setLabel(fun.getLabel());
					break;
				}
			}
			if(functionValue.getName()==null){
				throw new RuleException("Function["+nameorlabel+"] not exist.");
			}
			ComplexValueContext complexValue=commonFunctionContext.complexValue();
			CommonFunctionParameter param=new CommonFunctionParameter();
			param.setObjectParameter(buildValue(complexValue));
			PropertyContext propertyContext=commonFunctionContext.property();
			if(propertyContext!=null){
				param.setProperty(propertyContext.getText());
			}
			functionValue.setParameter(param);
			value=functionValue;
		}else if(context.complexValue()!=null){
			List<ComplexValueContext> values=context.complexValue();
			value=buildValue(values.get(0));
		}
		List<TerminalNode> arithList=context.ARITH();
		if(arithList!=null && arithList.size()>0){
			TerminalNode arithNode=arithList.get(0);
			ComplexArithmetic arith=new ComplexArithmetic();
			arith.setType(ArithmeticType.parse(arithNode.getText()));
			ParseTree nextContext=context.getChild(2);
			arith.setValue(buildValue((ComplexValueContext)nextContext));
			value.setArithmetic(arith);			
		}
		return value;
	}
	private static ConstantValue buildConstantValue(ConstantContext context){
		ConstantValue value=new ConstantValue();
		value.setConstantCategory(context.constantCategory().Identifier().getText());
		value.setConstantLabel(context.property().getText());
		return value;
	}
	private static VariableValue buildVariableValue(VariableContext context){
		VariableValue value=new VariableValue();
		value.setVariableCategory(context.variableCategory().getText());
		value.setVariableLabel(context.property().getText());
		return value;
	}
	private static SimpleValue buildSimpleValue(ValueContext context){
		SimpleValue value=new SimpleValue();
		if(context.STRING()!=null){
			value.setContent(getSTRINGContent(context.STRING()));
		}else if(context.Boolean()!=null){
			value.setContent(context.Boolean().getText());
		}else if(context.NUMBER()!=null){
			value.setContent(context.NUMBER().getText());
		}
		return value;
	}
	
	public static String getSTRINGContent(TerminalNode node){
		String text=node.getText();
		return text.substring(1,text.length()-1);
	}
}
