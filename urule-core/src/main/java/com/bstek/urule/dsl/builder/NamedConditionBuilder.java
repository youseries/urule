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
