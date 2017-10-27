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
package com.bstek.urule.runtime.response;

import java.util.List;

/**
 * @author Jacky.gao
 * @since 2016年9月7日
 */
public interface FlowExecutionResponse extends ExecutionResponse{
	/**
	 * @return 返回执行的决策流ID
	 */
	String getFlowId();
	/**
	 * @return 返回当前决策流经过的节点名称列表
	 */
	List<String> getNodeNames();
	/**
	 * @return 返回规则集执行信息
	 */
	List<RuleExecutionResponse> getRuleExecutionResponses();
	/**
	 * @return 返回决策流执行信息
	 */
	List<FlowExecutionResponse> getFlowExecutionResponses();
}
