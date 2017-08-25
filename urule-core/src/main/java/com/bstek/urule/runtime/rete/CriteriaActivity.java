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
	public void reset() {
		pass=false;
	}
}
