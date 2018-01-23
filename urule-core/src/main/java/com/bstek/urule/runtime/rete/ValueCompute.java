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
package com.bstek.urule.runtime.rete;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bstek.urule.RuleException;
import com.bstek.urule.Utils;
import com.bstek.urule.action.ActionValue;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rule.AbstractValue;
import com.bstek.urule.model.rule.ArithmeticType;
import com.bstek.urule.model.rule.CommonFunctionValue;
import com.bstek.urule.model.rule.ComplexArithmetic;
import com.bstek.urule.model.rule.ConstantValue;
import com.bstek.urule.model.rule.MethodValue;
import com.bstek.urule.model.rule.NamedReferenceValue;
import com.bstek.urule.model.rule.ParameterValue;
import com.bstek.urule.model.rule.ParenValue;
import com.bstek.urule.model.rule.SimpleArithmetic;
import com.bstek.urule.model.rule.SimpleArithmeticValue;
import com.bstek.urule.model.rule.SimpleValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.bstek.urule.model.rule.VariableCategoryValue;
import com.bstek.urule.model.rule.VariableValue;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;

/**
 * @author Jacky.gao
 * @since 2014年12月29日
 */
public class ValueCompute {
	public static final String BEAN_ID="urule.valueCompute";
	public Object complexValueCompute(Value value,Object matchedObject,Context context,List<Object> allMatchedObjects,Map<String,Object> variableMap){
		return compute(value,context,matchedObject,allMatchedObjects,variableMap);
	}
	public Object simpleArithmeticCompute(Context context,Object leftObj,SimpleArithmetic simpleArithmetic){
		String expr=null;
		if(!(leftObj instanceof Number)){
			expr="\""+leftObj+"\"";
		}else{
			expr=leftObj.toString();
		}
		while(simpleArithmetic!=null){
			ArithmeticType type=simpleArithmetic.getType();
			SimpleArithmeticValue nextValue=simpleArithmetic.getValue();
			expr+=type.toString()+"\""+nextValue.getContent()+"\"";
			simpleArithmetic=nextValue.getArithmetic();
		}
		return context.parseExpression(expr);
	}
	
	private Object compute(Value value,Context context,Object matchedFact,List<Object> allMatchedObjects,Map<String,Object> variableMap){
		Object leftObj=fetchValue(value,context,matchedFact,allMatchedObjects,variableMap);
		ComplexArithmetic arithmetic=value.getArithmetic();
		if(arithmetic==null){
			return leftObj;
		}
		StringBuffer expr=new StringBuffer();
		addToExpr(expr, leftObj);
		while(arithmetic!=null){
			ArithmeticType type=arithmetic.getType();
			addToExpr(expr, type);
			AbstractValue rightValue=(AbstractValue)arithmetic.getValue();
			if(rightValue instanceof ParenValue){
				ParenValue pv=(ParenValue)rightValue;
				Object obj=compute(pv.getValue(), context,matchedFact,allMatchedObjects,variableMap);
				addToExpr(expr, obj);
			}else{
				Object rightObj=fetchValue(rightValue, context,matchedFact,allMatchedObjects,variableMap);
				if(rightObj==null){
					rightObj="null";
				}
				addToExpr(expr, rightObj);
			}
			arithmetic=rightValue.getArithmetic();
		}
		return context.parseExpression(expr.toString());
	}
	
	private void addToExpr(StringBuffer expr,Object obj){
		expr.append(" ");
		if(obj instanceof ArithmeticType){
			expr.append(obj.toString());			
		}else if(obj instanceof Number){
			expr.append(obj.toString());
		}else{
			expr.append("\""+obj+"\"");
		}
		expr.append(" ");
	}
	
	private Object fetchValue(Value value,Context context,Object matchedFact,List<Object> allMatchedObjects,Map<String,Object> variableMap){
		Object left=null;
		ValueType type=value.getValueType();
		if(type.equals(ValueType.Input)){
			left=((SimpleValue)value).getContent();
		}else if(type.equals(ValueType.Constant)){
			ConstantValue cv=(ConstantValue)value;
			left=cv.getConstantName();
		}else if(type.equals(ValueType.VariableCategory)){
			VariableCategoryValue vc=(VariableCategoryValue)value;
			String categoryName=vc.getVariableCategory();
			return findObject(context, matchedFact, categoryName,allMatchedObjects);
		}else if(type.equals(ValueType.Parameter)){
			ParameterValue pv=(ParameterValue)value;
			String categoryName=VariableCategory.PARAM_CATEGORY;
			Object object = findObject(context, matchedFact, categoryName,allMatchedObjects);
			if(object==null){
				return null;
			}
			String property=pv.getVariableName();
			left=Utils.getObjectProperty(object, property);
		}else if(type.equals(ValueType.Method)){
			MethodValue mv=(MethodValue)value;
			ExecuteMethodAction action=new ExecuteMethodAction();
			action.setBeanId(mv.getBeanId());
			action.setBeanLabel(mv.getBeanLabel());
			action.setMethodName(mv.getMethodName());
			action.setMethodLabel(mv.getMethodLabel());
			action.setParameters(mv.getParameters());
			ActionValue actionValue=action.execute(context, matchedFact,allMatchedObjects,variableMap);
			if(actionValue!=null){
				left=actionValue.getValue();
			}else{
				left=null;
			}
		}else if(type.equals(ValueType.CommonFunction)){
			CommonFunctionValue v=(CommonFunctionValue)value;
			CommonFunctionParameter functionParameter=v.getParameter();
			Value propertyValue=functionParameter.getObjectParameter();
			Object object=this.complexValueCompute(propertyValue, matchedFact, context, allMatchedObjects,variableMap);
			FunctionDescriptor fun=Utils.findFunctionDescriptor(v.getName());
			Argument arg=fun.getArgument();
			String property=null;
			if(arg.isNeedProperty()){
				property=functionParameter.getProperty();
			}
			left=fun.doFunction(object, property,context.getWorkingMemory());
		}else if(type.equals(ValueType.Paren)){
			ParenValue parenValue=(ParenValue)value;
			left=compute(parenValue.getValue(), context,matchedFact,allMatchedObjects,variableMap);
		}else if(type.equals(ValueType.NamedReference)){
			NamedReferenceValue namedValue=(NamedReferenceValue)value;
			String prop=namedValue.getPropertyName();
			String refName=namedValue.getReferenceName();
			if(variableMap==null){
				throw new RuleException("Reference ["+refName+"] not define");
			}
			Object obj=variableMap.get(refName);
			if(obj==null){
				refName=refName.substring(1,refName.length());
				obj=variableMap.get(refName);
			}
			if(obj==null){
				throw new RuleException("Reference ["+refName+"] not define");
			}
			if(prop!=null){
				left=Utils.getObjectProperty(obj, prop);				
			}else{
				left=obj;			
			}
		}else{
			VariableValue vv=(VariableValue)value;
			String categoryName=vv.getVariableCategory();
			Object object = findObject(context, matchedFact, categoryName,allMatchedObjects);
			if(object==null){
				return null;
			}
			String property=vv.getVariableName();
			left=Utils.getObjectProperty(object, property);
		}
		return left;
	}
	
	
	private Object findObject(Context context,Object matchedFact, String categoryName,List<Object> allMatchedObjects) {
		String className=context.getVariableCategoryClass(categoryName);
		Object object = findObject(className, matchedFact,context);
		if(allMatchedObjects!=null && object!=null && !allMatchedObjects.contains(object)){
			allMatchedObjects.add(object);
		}
		return object;
	}
	@SuppressWarnings("rawtypes")
	public Object findObject(String className,Object matchedFact,Context context) {
		if(className.equals(HashMap.class.getName())){
			return context.getWorkingMemory().getParameters();
		}
		if(matchedFact instanceof Collection){
			Collection coll=(Collection)matchedFact;
			for(Object obj:coll){
				if(obj.getClass().getName().equals(className)){
					return obj;
				}else if(obj instanceof GeneralEntity){
					GeneralEntity entity=(GeneralEntity)obj;
					if(entity.getTargetClass().equals(className)){
						return obj;
					}
				}
			}
		}else{
			if(matchedFact.getClass().getName().equals(className)){
				return matchedFact;
			}else if(matchedFact instanceof GeneralEntity){
				GeneralEntity entity=(GeneralEntity)matchedFact;
				if(entity.getTargetClass().equals(className)){
					return matchedFact;
				}
			}			
		}
		Object object=null;
		List<Object> allFacts=context.getWorkingMemory().getAllFacts();
		for(Object obj:allFacts){
			if(obj.getClass().getName().equals(className)){
				object = obj;
				break;
			}else if(obj instanceof GeneralEntity){
				GeneralEntity entity=(GeneralEntity)obj;
				if(entity.getTargetClass().equals(className)){
					object = obj;
					break;
				}
			}
		}
		if(object==null){
			allFacts=context.getWorkingMemory().getHistoryFacts();
			for(Object obj:allFacts){
				if(obj.getClass().getName().equals(className)){
					object = obj;
					break;
				}else if(obj instanceof GeneralEntity){
					GeneralEntity entity=(GeneralEntity)obj;
					if(entity.getTargetClass().equals(className)){
						object = obj;
						break;
					}
				}
			}
		}
		return object;
	}
}
