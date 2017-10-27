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
import java.util.List;
import java.util.Map;

import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.agenda.ActivationImpl;
import com.bstek.urule.runtime.event.impl.ActivationCreatedEventImpl;

/**
 * @author Jacky.gao
 * @since 2015年1月8日
 */
public class TerminalActivity extends AbstractActivity {
	private Rule rule;
	public TerminalActivity(Rule rule) {
		this.rule = rule;
	}
	public Collection<FactTracker> enter(EvaluationContext context, Object obj,FactTracker tracker,Map<String,Object> variableMap) {
		List<FactTracker> result=new ArrayList<FactTracker>();
		ActivationImpl ac=new ActivationImpl(rule,variableMap);
		tracker.setActivation(ac);
		result.add(tracker);
		KnowledgeSession session = (KnowledgeSession)context.getWorkingMemory();
		session.fireEvent(new ActivationCreatedEventImpl(ac,session));
		return result;
	}
	@Override
	public boolean orNodeIsPassed() {
		return false;
	}
	@Override
	public void reset() {
	}
}
