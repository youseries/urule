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
package com.bstek.urule.runtime.rete;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.urule.model.rule.lhs.NamedCriteria;

/**
 * @author Jacky.gao
 * @since 2015年1月8日
 */
public class NamedCriteriaActivity  extends AbstractActivity{
	protected NamedCriteria criteria;
	private boolean pass;
	public NamedCriteriaActivity(NamedCriteria criteria){
		this.criteria=criteria;
	}
	public List<FactTracker> enter(EvaluationContext context, Object obj,FactTracker tracker,Map<String,Object> variableMap) {
		String referenceName=criteria.getReferenceName();
		if(pass){
			if(StringUtils.isBlank(referenceName) || variableMap.containsKey(referenceName)){
				return null;
			}
		}
		List<Object> allMatchedObjects=new ArrayList<Object>();
		boolean result=criteria.evaluate(context,obj,allMatchedObjects);
		if(result){
			if(StringUtils.isNotBlank(referenceName)){
				variableMap.put(referenceName,obj);				
			}
			context.setPrevActivity(this);
			pass=true;
			tracker.addObjectCriteria(obj, criteria);
			for(Object object:allMatchedObjects){
				tracker.addObjectCriteria(object, criteria);
			}
			return visitPahs(context,obj,tracker,variableMap);
		}
		return null;
	}
	@Override
	public boolean orNodeIsPassed() {
		return false;
	}
	@Override
	public void reset() {
		pass=false;
	}
}
