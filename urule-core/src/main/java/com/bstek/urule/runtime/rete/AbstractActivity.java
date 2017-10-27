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
