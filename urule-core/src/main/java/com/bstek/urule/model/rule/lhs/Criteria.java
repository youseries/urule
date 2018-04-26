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
package com.bstek.urule.model.rule.lhs;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.bstek.urule.RuleException;
import com.bstek.urule.Utils;
import com.bstek.urule.action.ActionValue;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;
import com.bstek.urule.model.rule.SimpleArithmetic;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.EvaluationContext;
import com.bstek.urule.runtime.rete.ValueCompute;

/**
 * @author Jacky.gao
 * @since 2014年12月29日
 */
public class Criteria extends BaseCriterion implements BaseCriteria{
	@JsonIgnore
	private String id;
	private Op op;
	private Left left;
	private Value value;
	
	@Override
	public EvaluateResponse evaluate(EvaluationContext context,Object obj,List<Object> allMatchedObjects){
		Datatype datatype=null;
		Object leftResult=null;
		LeftPart leftPart=left.getLeftPart();
		String leftId=left.getId();
		ValueCompute valueCompute=context.getValueCompute();
		if(context.partValueExist(leftId)){
			leftResult=context.getPartValue(leftId);
			if(leftPart instanceof VariableLeftPart){
				datatype=((VariableLeftPart)leftPart).getDatatype();
			}
		}else{
			Object leftValue=null;
			if(leftPart instanceof VariableLeftPart){
				VariableLeftPart varPart=(VariableLeftPart)leftPart;
				datatype=varPart.getDatatype();
				if(varPart.getVariableName()==null){
					throw new RuleException("Criteria left[variableName] can not be null.");
				}
				leftValue=Utils.getObjectProperty(obj, varPart.getVariableName());
			}else{
				if(leftPart instanceof MethodLeftPart){
					MethodLeftPart methodPart=(MethodLeftPart)leftPart;
					ExecuteMethodAction methodAction=new ExecuteMethodAction();
					methodAction.setBeanId(methodPart.getBeanId());
					methodAction.setBeanLabel(methodPart.getBeanLabel());
					methodAction.setMethodLabel(methodPart.getMethodLabel());
					methodAction.setMethodName(methodPart.getMethodName());
					methodAction.setParameters(methodPart.getParameters());
					ActionValue actionValue=methodAction.execute(context, obj,allMatchedObjects,null);
					if(actionValue==null){
						leftValue=null;
					}else{
						leftValue=actionValue.getValue();
					}
				}else if(leftPart instanceof ExistLeftPart){
					ExistLeftPart existPart=(ExistLeftPart)leftPart;
					leftValue=existPart.evaluate(context, obj, allMatchedObjects);
				}else if(leftPart instanceof AllLeftPart){
					AllLeftPart allPart=(AllLeftPart)leftPart;
					leftValue=allPart.evaluate(context, obj, allMatchedObjects);
				}else if(leftPart instanceof CollectLeftPart){
					CollectLeftPart collectPart=(CollectLeftPart)leftPart;
					leftValue=collectPart.evaluate(context, obj, allMatchedObjects);
				}else if(leftPart instanceof CommonFunctionLeftPart){
					CommonFunctionLeftPart part=(CommonFunctionLeftPart)leftPart;
					leftValue=part.evaluate(context, obj, allMatchedObjects);
				}
			}
			leftResult=leftValue;
			SimpleArithmetic arithmetic=left.getArithmetic();
			if(arithmetic!=null){
				leftResult=valueCompute.simpleArithmeticCompute(context,leftValue, arithmetic);
			}
			context.storePartValue(leftId, leftResult);
		}
		EvaluateResponse response=new EvaluateResponse();
		response.setLeftResult(leftResult);
		Object right=null;
		if(value!=null){
			String valueId=value.getId();
			if(context.partValueExist(valueId)){
				right=context.getPartValue(valueId);
			}else{				
				right=valueCompute.complexValueCompute(value,obj,context,allMatchedObjects,null);
				response.setRightResult(right);
				context.storePartValue(valueId, right);
			}
		}
		if(datatype==null){
			datatype=Utils.getDatatype(leftResult);
		}
		boolean result=context.getAssertorEvaluator().evaluate(leftResult, right, datatype,op);
		response.setResult(result);
		return response;
	}
	@Override
	public String getId() {
		if(id==null){
			id=left.getId()+"【"+op.toString()+"】";
			if(value!=null)id+=value.getId();
		}
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Op getOp() {
		return op;
	}
	public void setOp(Op op) {
		this.op = op;
	}
	public Left getLeft() {
		return left;
	}
	public void setLeft(Left left) {
		this.left = left;
	}
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
}
