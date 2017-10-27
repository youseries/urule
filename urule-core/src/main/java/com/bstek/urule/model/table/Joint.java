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
package com.bstek.urule.model.table;

import java.util.ArrayList;
import java.util.List;

import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.Or;

/**
 * @author Jacky.gao
 * @since 2015年1月19日
 */
public class Joint {
	private List<Condition> conditions;
	private List<Joint> joints;
	private JointType type;
	public Junction getJunction(){
		if(type.equals(JointType.and)){
			return new And();
		}else{
			return new Or();
		}
	}
	public List<Condition> getConditions() {
		return conditions;
	}
	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}
	public void addJoint(Joint joint){
		if(joints==null){
			joints=new ArrayList<Joint>();
		}
		joints.add(joint);
	}
	public void addCondition(Condition condition){
		if(conditions==null){
			conditions=new ArrayList<Condition>();
		}
		conditions.add(condition);
	}
	public List<Joint> getJoints() {
		return joints;
	}
	public void setJoints(List<Joint> joints) {
		this.joints = joints;
	}
	public JointType getType() {
		return type;
	}
	public void setType(JointType type) {
		this.type = type;
	}
}
