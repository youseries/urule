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
package com.bstek.urule.model.flow.ins;

import java.util.List;
import java.util.Map;

import com.bstek.urule.runtime.response.FlowExecutionResponse;
import com.bstek.urule.runtime.rete.Context;

/**
 * @author Jacky.gao
 * @since 2015年2月28日
 */
public interface FlowContext extends Context {
	Object getVariable(String key);
	Map<String,Object> getVariables();
	void addVariable(String key,Object object);
	void removeVariable(String key);
	List<FlowInstance> getFlowInstances();
	void addFlowInstance(FlowInstance instance);
	void setSessionValue(String key,Object value);
	Object getSessionValue(String key);
	FlowExecutionResponse getResponse();
}
