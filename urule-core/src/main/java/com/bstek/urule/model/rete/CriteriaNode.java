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

import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.runtime.rete.Activity;
import com.bstek.urule.runtime.rete.CriteriaActivity;

/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public class CriteriaNode extends BaseReteNode implements ConditionNode{
	private String criteriaInfo;
	private Criteria criteria;
	private boolean debug;
	private NodeType nodeType=NodeType.criteria;
	public CriteriaNode() {
		super(0);
	}
	public CriteriaNode(Criteria criteria,int id,boolean debug) {
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
	public Criteria getCriteria() {
		return criteria;
	}
	public void setCriteria(Criteria criteria) {
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
		CriteriaActivity activity=new CriteriaActivity(criteria,debug);
		for(Line line:lines){
			activity.addPath(line.newPath(context));
		}
		context.put(this, activity);
		return activity;
	}
}
