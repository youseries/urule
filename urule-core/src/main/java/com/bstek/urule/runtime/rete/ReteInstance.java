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
