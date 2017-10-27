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
	private NodeType nodeType=NodeType.criteria;
	public CriteriaNode() {
		super(0);
	}
	public CriteriaNode(Criteria criteria,int id) {
		super(id);
		this.criteria = criteria;
		setCriteriaInfo(criteria.getId());
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
		CriteriaActivity activity=new CriteriaActivity(criteria);
		for(Line line:lines){
			activity.addPath(line.newPath(context));
		}
		context.put(this, activity);
		return activity;
	}
}
