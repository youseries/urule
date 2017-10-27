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
package com.bstek.urule.runtime;

import java.util.List;
import java.util.Map;

import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.runtime.rete.ReteInstance;

/**
 * @author Jacky.gao
 * @since 2015年1月20日
 */
public interface KnowledgePackage {
	Rete getRete();
	Map<String,String> getVariableCateogoryMap();
	Map<String, FlowDefinition> getFlowMap();
	Map<String, String> getParameters();
	ReteInstance newReteInstance();
	long getTimestamp();
	void resetTimestamp();
	List<Rule> getNoLhsRules();
	List<Rule> getWithElseRules();
	String getId();
}
