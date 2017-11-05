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
