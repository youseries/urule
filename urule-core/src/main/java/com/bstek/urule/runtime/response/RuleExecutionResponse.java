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

import com.bstek.urule.action.ActionValue;
import com.bstek.urule.model.rule.RuleInfo;

/**
 * @author Jacky.gao
 * @since 2016年9月7日
 */
public interface RuleExecutionResponse extends ExecutionResponse {
	/**
	 * @return 返回所有触发的规则对象信息
	 */
	List<RuleInfo> getFiredRules();
	/**
	 * @return 返回所有匹配的规则对象信息
	 */
	List<RuleInfo> getMatchedRules();
	/**
	 * @return 返回匹配规则动作执行时返回的结果(如果有定义返回值的话)
	 */
	List<ActionValue> getActionValues();
	/**
	 * @return 返回决策流执行信息
	 */
	List<FlowExecutionResponse> getFlowExecutionResponses();
}
