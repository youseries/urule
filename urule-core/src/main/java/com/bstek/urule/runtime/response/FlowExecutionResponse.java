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
