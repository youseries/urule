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
