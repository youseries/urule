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
package com.bstek.urule.builder.table;

import java.util.List;

import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.Left;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.model.rule.lhs.Or;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;
import com.bstek.urule.model.table.Cell;
import com.bstek.urule.model.table.Column;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.model.table.Joint;
import com.bstek.urule.model.table.JointType;

/**
 * @author Jacky.gao
 * @since 2015年1月20日
 */
public class CellContentBuilder {
	public Criterion buildCriterion(Cell cell,Column col){
		Joint joint=cell.getJoint();
		if(joint==null){
			return null;
		}
		List<Condition> conditions=joint.getConditions();
		List<Joint> joints=joint.getJoints();
		if((conditions==null || conditions.size()==0) && (joints==null || joints.size()==0)){
			return null;
		}
		Junction topJunction=null;
		if(conditions.size()==1){
			return newCriteria(col, conditions.get(0));
		}else{
			if(joint.getType().equals(JointType.and)){
				topJunction=new And();
			}else{
				topJunction=new Or();
			}
			buildConditionsCriterion(conditions, topJunction, col);
			buildJointsCriterion(joints, col, topJunction);
			return topJunction;
		}
	}
	private void buildJointsCriterion(List<Joint> joints,Column col,Junction parentJunction){
		if(joints==null || joints.size()==0){
			return;
		}
		for(Joint joint:joints){
			Junction junction=joint.getJunction();
			List<Condition> conditions=joint.getConditions();
			buildConditionsCriterion(conditions,junction,col);
			List<Joint> children=joint.getJoints();
			buildJointsCriterion(children,col,junction);
			parentJunction.addCriterion(junction);
		}
	}
	private void buildConditionsCriterion(List<Condition> conditions,Junction junction,Column col){
		if(conditions==null || conditions.size()==0){
			return;
		}
		for(Condition condition:conditions){
			Criteria criteria = newCriteria(col, condition);
			junction.addCriterion(criteria);
		}
	}
	private Criteria newCriteria(Column col, Condition condition) {
		Criteria criteria=new Criteria();
		Left left=new Left();
		VariableLeftPart part=new VariableLeftPart();
		part.setVariableCategory(col.getVariableCategory());
		part.setVariableName(col.getVariableName());
		part.setVariableLabel(col.getVariableLabel());
		part.setDatatype(col.getDatatype());
		left.setLeftPart(part);
		left.setType(LeftType.variable);
		criteria.setLeft(left);
		criteria.setOp(condition.getOp());
		criteria.setValue(condition.getValue());
		return criteria;
	}
}
