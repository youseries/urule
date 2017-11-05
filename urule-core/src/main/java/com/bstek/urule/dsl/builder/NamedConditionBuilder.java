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
import java.util.List;

import com.bstek.urule.RuleException;
import com.bstek.urule.dsl.DSLUtils;
import com.bstek.urule.dsl.RuleParserParser.JoinContext;
import com.bstek.urule.dsl.RuleParserParser.MultiNamedConditionsContext;
import com.bstek.urule.dsl.RuleParserParser.NamedConditionContext;
import com.bstek.urule.dsl.RuleParserParser.ParenNamedConditionsContext;
import com.bstek.urule.dsl.RuleParserParser.SingleNamedConditionsContext;
import com.bstek.urule.model.rule.Op;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.CriteriaUnit;
import com.bstek.urule.model.rule.lhs.JunctionType;
import com.bstek.urule.model.rule.lhs.Left;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;

/**
 * @author Jacky.gao
 * @since 2016年8月15日
 */
public class NamedConditionBuilder {
	public CriteriaUnit buildNamedCriteria(NamedConditionContext namedConditionContext,String variableCategory){
		CriteriaUnit unit=null;
		if(namedConditionContext instanceof MultiNamedConditionsContext){
			unit=visitMultiNamedConditions((MultiNamedConditionsContext)namedConditionContext, variableCategory);
		}else if(namedConditionContext instanceof SingleNamedConditionsContext){
			unit=visitSingleNamedConditions((SingleNamedConditionsContext)namedConditionContext, variableCategory);
		}else if(namedConditionContext instanceof ParenNamedConditionsContext){
			unit=visitParenNamedConditions((ParenNamedConditionsContext)namedConditionContext, variableCategory);
		}else{
			throw new RuleException("Unsupport context : +namedConditionContext+");
		}
		return unit;
	}
	
	private CriteriaUnit visitSingleNamedConditions(SingleNamedConditionsContext ctx,String variableCategory) {
		Criteria criteria=new Criteria();
		VariableLeftPart leftPart=new VariableLeftPart();
		Left left=new Left();
		left.setLeftPart(leftPart);
		left.setType(LeftType.NamedReference);
		criteria.setLeft(left);
		String variableName=ctx.property().getText();
		leftPart.setVariableLabel(variableName);
		leftPart.setVariableCategory(variableCategory);
		Op op=DSLUtils.parseOp(ctx.op());
		criteria.setOp(op);
		if(ctx.complexValue()==null){
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
		CriteriaUnit unit=new CriteriaUnit();
		unit.setCriteria(criteria);
		return unit;
	}
	private CriteriaUnit visitMultiNamedConditions(MultiNamedConditionsContext ctx,String variableCategory) {
		List<CriteriaUnit> nextUnits=new ArrayList<CriteriaUnit>();
		List<NamedConditionContext> namedConditions=ctx.namedCondition();
		if(namedConditions!=null){
			for(int i=0;i<namedConditions.size();i++){
				NamedConditionContext context=namedConditions.get(i);
				CriteriaUnit nextUnit=buildNamedCriteria(context, variableCategory);
				nextUnits.add(nextUnit);
				JoinContext joinContext=ctx.join(i);
				if(joinContext!=null){
					if(joinContext.AND()!=null){
						nextUnit.setJunctionType(JunctionType.and);
					}else{
						nextUnit.setJunctionType(JunctionType.or);				
					}
				}
			}
		}
		CriteriaUnit unit=new CriteriaUnit();
		unit.setNextUnits(nextUnits);
		return unit;
	}
	
	private CriteriaUnit visitParenNamedConditions(ParenNamedConditionsContext ctx,String variableCategory) {
		NamedConditionContext namedConditionContext=ctx.namedCondition();
		CriteriaUnit unit=buildNamedCriteria(namedConditionContext, variableCategory);
		return unit;
	}
}
