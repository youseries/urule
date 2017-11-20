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
package com.bstek.urule.model.rete;

import java.util.Map;

import com.bstek.urule.model.rule.lhs.NamedCriteria;
import com.bstek.urule.runtime.rete.Activity;
import com.bstek.urule.runtime.rete.CriteriaActivity;
import com.bstek.urule.runtime.rete.NamedCriteriaActivity;

/**
 * @author Jacky.gao
 * @since 2016年8月15日
 */
public class NamedCriteriaNode extends BaseReteNode implements ConditionNode{
	private String criteriaInfo;
	private NamedCriteria criteria;
	private NodeType nodeType=NodeType.namedCriteria;
	private boolean debug;
	public NamedCriteriaNode() {
		super(0);
	}
	public NamedCriteriaNode(NamedCriteria criteria,int id,boolean debug) {
		super(id);
		this.criteria = criteria;
		setCriteriaInfo(criteria.getId());
		this.debug=debug;
	}
	@Override
	public NodeType getNodeType() {
		return nodeType;
	}
	@Override
	public NamedCriteria getCriteria() {
		return criteria;
	}
	public void setCriteria(NamedCriteria criteria) {
		this.criteria = criteria;
	}
	@Override
	public String getCriteriaInfo() {
		return criteriaInfo;
	}
	public void setCriteriaInfo(String criteriaInfo) {
		this.criteriaInfo = criteriaInfo;
	}
	@Override
	public Activity newActivity(Map<Object,Object> context) {
		if(context.containsKey(this)){
			return (CriteriaActivity)context.get(this);
		}
		NamedCriteriaActivity activity=new NamedCriteriaActivity(criteria,debug);
		for(Line line:lines){
			activity.addPath(line.newPath(context));
		}
		context.put(this, activity);
		return activity;
	}
}
