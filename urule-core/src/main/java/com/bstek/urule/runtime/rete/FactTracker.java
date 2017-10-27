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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.model.rule.lhs.BaseCriteria;
import com.bstek.urule.runtime.agenda.Activation;
import com.bstek.urule.runtime.agenda.ActivationImpl;

/**
 * @author Jacky.gao
 * @since 2015年8月2日
 */
public class FactTracker {
	private Activation activation;
	private Map<Object,List<BaseCriteria>> objectCriteriaMap=new HashMap<Object,List<BaseCriteria>>();
	public Activation getActivation() {
		return activation;
	}
	public void setActivation(Activation activation) {
		ActivationImpl ac=(ActivationImpl)activation;
		ac.setObjectCriteriaMap(objectCriteriaMap);
		this.activation = activation;
	}
	public Map<Object, List<BaseCriteria>> getObjectCriteriaMap() {
		return objectCriteriaMap;
	}
	public void addObjectCriteria(Object obj, BaseCriteria criteria) {
		if(obj instanceof HashMap && !(obj instanceof GeneralEntity)){
			obj=HashMap.class.getName();
		}
		if(objectCriteriaMap.containsKey(obj)){
			List<BaseCriteria> list=objectCriteriaMap.get(obj);
			if(!list.contains(criteria)){
				list.add(criteria);
			}
		}else{
			List<BaseCriteria> list=new ArrayList<BaseCriteria>();
			list.add(criteria);
			objectCriteriaMap.put(obj, list);
		}
	}
	public FactTracker newSubFactTracker(){
		FactTracker tracker=new FactTracker();
		tracker.getObjectCriteriaMap().putAll(objectCriteriaMap);
		return tracker;
	}
}
