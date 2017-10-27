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

import com.bstek.urule.model.rule.lhs.Criteria;

/**
 * @author Jacky.gao
 * @since 2015年1月8日
 */
public class CriteriaActivity  extends AbstractActivity {
	protected Criteria criteria;
	private boolean pass;
	public CriteriaActivity(Criteria criteria){
		this.criteria=criteria;
	}
	public List<FactTracker> enter(EvaluationContext context, Object obj,FactTracker tracker,Map<String,Object> variableMap) {
		if(pass){
			return null;
		}
		if(orNodeIsPassed()){
			return null;
		}
		List<Object> allMatchedObjects=new ArrayList<Object>();
		boolean result=criteria.evaluate(context, obj,allMatchedObjects);
		if(result){
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
		List<Path> paths=getPaths();
		if(paths!=null){
			if(paths.size()>1){
				return false;
			}else if(paths.size()==1){
				Path path=paths.get(0);
				AbstractActivity activity=(AbstractActivity)path.getTo();
				return activity.orNodeIsPassed();
			}
		}
		return false;
	}
	@Override
	public void reset() {
		pass=false;
	}
}
