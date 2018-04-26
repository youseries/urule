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

import com.bstek.urule.Utils;
import com.bstek.urule.debug.MsgType;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.EvaluateResponse;

/**
 * @author Jacky.gao
 * @since 2015年1月8日
 */
public class CriteriaActivity  extends AbstractActivity {
	protected Criteria criteria;
	private boolean pass;
	private boolean debug;
	public CriteriaActivity(Criteria criteria,boolean debug){
		this.criteria=criteria;
		this.debug=debug;
	}
	public List<FactTracker> enter(EvaluationContext context, Object obj,FactTracker tracker,Map<String,Object> variableMap) {
		if(pass){
			return null;
		}
		if(orNodeIsPassed()){
			return null;
		}
		List<Object> allMatchedObjects=new ArrayList<Object>();
		EvaluateResponse response=null;
		String criteriaId=criteria.getId();
		Object storeValue=context.getCriteriaValue(criteriaId);
		if(storeValue!=null){
			response=(EvaluateResponse)storeValue;
		}else{
			response=criteria.evaluate(context,obj,allMatchedObjects);
			context.storeCriteriaValue(criteriaId, response);;
		}
		boolean result=response.getResult();
		doDebug(response,context);
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
	
	private void doDebug(EvaluateResponse response,Context context){
		if(!debug || !Utils.isDebug()){
			return;
		}
		String id=criteria.getId();
		StringBuffer sb=new StringBuffer();
		sb.append("^^^条件："+id);
		String result=response.getResult() ? "满足" : "不满足";
		sb.append(" =>"+result);
		sb.append(", 左值："+(response.getLeftResult()==null ? "null" : response.getLeftResult()));
		sb.append(", 右值："+(response.getRightResult()==null ? "null" : response.getRightResult()));
		context.debugMsg(sb.toString(), MsgType.Condition, debug);
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
