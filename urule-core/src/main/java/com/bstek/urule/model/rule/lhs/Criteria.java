/*******************************************************************************
 * Copyright (C) 2017 Bstek.com
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
import com.bstek.urule.runtime.assertor.AssertorEvaluator;
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
	public boolean evaluate(EvaluationContext context,Object obj,List<Object> allMatchedObjects){
		Object leftValue=null;
		Datatype datatype=null;
		LeftPart leftPart=left.getLeftPart();
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
			datatype=Utils.getDatatype(leftValue);
		}
		Object leftResult=leftValue;
		SimpleArithmetic arithmetic=left.getArithmetic();
		ValueCompute valueCompute=context.getValueCompute();
		if(arithmetic!=null){
			leftResult=valueCompute.simpleArithmeticCompute(context,leftValue, arithmetic);
		}
		Object right=null;
		if(value!=null){
			right=valueCompute.complexValueCompute(value,obj,context,allMatchedObjects,null);
			if(right==null){
				return false;
			}
		}
		AssertorEvaluator assertorEvaluator=context.getAssertorEvaluator();
		boolean result=assertorEvaluator.evaluate(leftResult, right, datatype,op);
		return result;
	}
	@Override
	public String getId() {
		if(id==null){
			id=left.getLeftPart().getId()+"【"+op.toString()+"】";
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
