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
import java.util.List;
import java.util.Map;

/**
 * @author Jacky.gao
 * @since 2015年1月8日
 */
public abstract class AbstractActivity implements Activity {
	private List<Path> paths;
	public List<Path> getPaths() {
		return paths;
	}
	public void addPath(Path path){
		if(paths==null){
			paths=new ArrayList<Path>();
		}
		this.paths.add(path);
	}
	protected List<FactTracker> visitPahs(EvaluationContext context, Object obj,FactTracker tracker,Map<String,Object> variableMap){
		if(paths==null || paths.size()==0){
			return null;
		}
		List<FactTracker> trackers=null;
		int size=paths.size();
		for(Path path:paths){
			Collection<FactTracker> results=null;
			Activity activity=path.getTo();
			path.setPassed(true);
			if(size>0){
				Map<String,Object> newVariableMap=new HashMap<String,Object>();
				newVariableMap.putAll(variableMap);
				results=activity.enter(context, obj, tracker.newSubFactTracker(),newVariableMap);
			}else{
				results=activity.enter(context, obj, tracker,variableMap);				
			}
			if(results!=null){
				if(trackers==null){
					trackers=new ArrayList<FactTracker>();
				}
				trackers.addAll(results);
			}
		}
		return trackers;
	}
	public abstract boolean orNodeIsPassed();
	public abstract void reset();
}
