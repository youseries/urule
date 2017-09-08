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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bstek.urule.model.rule.lhs.BaseCriteria;

/**
 * @author Jacky.gao
 * @since 2015年1月8日
 */
public class AndActivity extends JoinActivity {
	private List<Path> fromPaths=new ArrayList<Path>();
	private boolean pass;
	private FactTracker currentTracker;
	private Map<Activity,List<Map<String,Object>>> toActivityMap=new HashMap<Activity,List<Map<String,Object>>>();

	public Collection<FactTracker> enter(EvaluationContext context, Object obj,FactTracker tracker,Map<String,Object> variableMap) {
		Activity prevActivity=context.getPrevActivity();
		if(prevActivity!=null){
			if(toActivityMap.containsKey(prevActivity)){
				List<Map<String,Object>> variableList=toActivityMap.get(prevActivity);
				if(pass){
					variableList.clear();
				}
				variableList.add(variableMap);
			}else{
				List<Map<String,Object>> variableList=new ArrayList<Map<String,Object>>();
				variableList.add(variableMap);
				toActivityMap.put(prevActivity, variableList);
			}
		}
		if(currentTracker!=null){
			Map<Object, List<BaseCriteria>> map=tracker.getObjectCriteriaMap();
			Map<Object, List<BaseCriteria>> currentMap=currentTracker.getObjectCriteriaMap();
			for(Object key:currentMap.keySet()){
				if(map.containsKey(key)){
					continue;
				}
				map.put(key, currentMap.get(key));
			}
		}
		currentTracker=tracker;
		if(isAllPassed()){
			context.setPrevActivity(this);
			pass=true;
			List<FactTracker> allTrackers=new ArrayList<FactTracker>();
			List<Map<String,Object>> list=buildPathVariableMaps();
			for(Map<String,Object> varMap:list){
				List<FactTracker> trackers=visitPahs(context,obj,tracker,varMap);
				if(trackers!=null && trackers.size()>0){
					allTrackers.addAll(trackers);
				}
			}
			return allTrackers;
		}
		return null;
	}
	
	private boolean isAllPassed(){
		boolean passed=true;
		for(Path path:fromPaths){
			if(!path.isPassed()){
				passed=false;
				break;
			}
		}
		return passed;
	}
	
	public void addFromPath(Path fromPath){
		fromPaths.add(fromPath);
	}
	
	private List<Map<String,Object>> buildPathVariableMaps(){
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		Iterator<List<Map<String,Object>>> iter=toActivityMap.values().iterator();
		while(iter.hasNext()){
			List<Map<String,Object>> list1=iter.next();
			result=buildVariableMaps(list1,result);
		}
		return result;
	}
	
	private static List<Map<String,Object>> buildVariableMaps(List<Map<String,Object>> list1,List<Map<String,Object>> list2){
		List<Map<String,Object>> resultList=new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map1:list1){
			if(list2.size()==0){
				resultList.add(map1);
			}else{
				for(Map<String,Object> map2:list2){
					Map<String,Object> newMap=new HashMap<String,Object>();
					newMap.putAll(map1);
					newMap.putAll(map2);
					resultList.add(newMap);
				}
			}
		}
		return resultList;
	}
	
	@Override
	public boolean orNodeIsPassed() {
		return false;
	}
	
	@Override
	public void reset() {
		toActivityMap.clear();
		pass=false;
		currentTracker=null;
	}
}
