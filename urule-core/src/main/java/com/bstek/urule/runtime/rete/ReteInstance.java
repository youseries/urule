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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2015年1月8日
 */
public class ReteInstance{
	private List<ObjectTypeActivity> objectTypeActivities;
	public ReteInstance(List<ObjectTypeActivity> objectTypeActivities) {
		this.objectTypeActivities=objectTypeActivities;
	}
	public Collection<FactTracker> enter(EvaluationContext context, Object obj) {
		Collection<FactTracker> trackers=null;
		for(ObjectTypeActivity objectTypeActivity:objectTypeActivities){
			if(objectTypeActivity.support(obj)){
				Collection<FactTracker> result=objectTypeActivity.enter(context,obj,new FactTracker(),new HashMap<String,Object>());
				if(result!=null){
					if(trackers==null){
						trackers=result;
					}else{
						trackers.addAll(result);											
					}
				}
			}
		}
		return trackers;
	}
	public List<ObjectTypeActivity> getObjectTypeActivities() {
		return objectTypeActivities;
	}
	public void reset(){
		for(ObjectTypeActivity objectTypeActivity:objectTypeActivities){
			List<Path> paths=objectTypeActivity.getPaths();
			resetActivities(paths,false);
		}
	}
	public void resetForReevaluate(Object valuateObj){
		for(ObjectTypeActivity objectTypeActivity:objectTypeActivities){
			if(objectTypeActivity.support(valuateObj)){				
				List<Path> paths=objectTypeActivity.getPaths();
				resetActivities(paths,true);
			}
		}
	}
	private void resetActivities(List<Path> paths,boolean forReevaluate){
		if(paths==null)return;
		for(Path path:paths){
			path.setPassed(false);
			Activity activity=path.getTo();
			if(forReevaluate){
				if(activity instanceof OrActivity){
					OrActivity ac=(OrActivity)activity;
					ac.reset();
				}
				if(activity instanceof CriteriaActivity){
					CriteriaActivity ac=(CriteriaActivity)activity;
					ac.reset();
				}
			}else{
				if(activity instanceof AbstractActivity){
					AbstractActivity ac=(AbstractActivity)activity;
					ac.reset();
				}
			}
			resetActivities(activity.getPaths(),forReevaluate);
		}
	}
}
