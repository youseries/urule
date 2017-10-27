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
package com.bstek.urule.runtime.agenda;

import java.util.ArrayList;
import java.util.List;

import com.bstek.urule.action.ActionValue;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.RuleInfo;
import com.bstek.urule.runtime.rete.Context;
/**
 * @author Jacky.gao
 * @since 2015年1月2日
 */
public abstract class RuleGroup {
	private String name;
	protected List<RuleInfo> executedRules;
	protected List<Activation> activations=new ArrayList<Activation>();
	public RuleGroup(String name,List<RuleInfo> executedRules) {
		this.name=name;
		this.executedRules=executedRules;
	}
	public abstract List<RuleInfo> execute(Context context,AgendaFilter filter, int max,List<ActionValue> actionValues);
	
	public static Activation fetchNextExecutableActivation(List<Activation> activations){
		Activation targetActivation=null;
		for(Activation ac:activations){
			if(!ac.isProcessed()){
				targetActivation=ac;
				break;
			}
		}
		return targetActivation;
	}
	
	public List<Activation> getActivations() {
		return activations;
	}
	public String getName() {
		return name;
	}
	public boolean contains(Rule rule){
		for(Activation activation:activations){
			if(activation.getRule().equals(rule)){
				return true;
			}
		}
		return false;
	}
}
