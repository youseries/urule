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
package com.bstek.urule.runtime;

import java.util.Map;

import com.bstek.urule.runtime.agenda.AgendaFilter;
import com.bstek.urule.runtime.response.FlowExecutionResponse;
import com.bstek.urule.runtime.response.RuleExecutionResponse;

/**
 * @author Jacky.gao
 * @since 2015年1月8日
 */
public interface KnowledgeSession extends WorkingMemory{
	/**
	 * 执行当前WorkMemory中所有满足条件的规则
	 * @return 返回一个ExecutionResponse对象，其中包含规则执行耗时，满足条件的规则，执行的规则等信息
	 */
	RuleExecutionResponse fireRules();
	/**
	 * 对当前WorkMemory中所有满足条件的规则进行过滤执行
	 * @param filter 对满足条件的规则进行过滤
	 * @return 返回一个ExecutionResponse对象，其中包含规则执行耗时，满足条件的规则，执行的规则等信息
	 */
	RuleExecutionResponse fireRules(AgendaFilter filter);
	/**
	 * 对当前WorkMemory中所有满足条件的规则进行过滤执行,并向WorkingMemory中设置一个Map的参数对象
	 * @param parameters 向WorkingMemory中设置一个Map的参数对象
	 * @param filter 对满足条件的规则进行过滤
	 * @return 返回一个ExecutionResponse对象，其中包含规则执行耗时，满足条件的规则，执行的规则等信息
	 */
	RuleExecutionResponse fireRules(Map<String,Object> parameters,AgendaFilter filter);
	/**
	 * 对当前WorkMemory中所有满足条件的规则进行执行，并定义执行的最大数目，超出后就不再执行
	 * @param max 执行规则的最大数目
	 * @return 返回一个ExecutionResponse对象，其中包含规则执行耗时，满足条件的规则，执行的规则等信息
	 */
	RuleExecutionResponse fireRules(int max);
	/**
	 * 对当前WorkMemory中所有满足条件的规则进行执行，并定义执行的最大数目，超出后就不再执行，<br>
	 * 并向WorkingMemory中设置一个Map的参数对象
	 * @param parameters 向WorkingMemory中设置一个Map的参数对象
	 * @param max 执行规则的最大数目
	 * @return 返回一个ExecutionResponse对象，其中包含规则执行耗时，满足条件的规则，执行的规则等信息
	 */
	RuleExecutionResponse fireRules(Map<String,Object> parameters,int max);
	/**
	 * 对当前WorkMemory中所有满足条件的规则进行过滤执行，并定义执行数目的最大值
	 * @param filter 对满足条件的规则进行过滤
	 * @param max 执行规则的最大数目
	 * @return 返回一个ExecutionResponse对象，其中包含规则执行耗时，满足条件的规则，执行的规则等信息
	 */
	RuleExecutionResponse fireRules(AgendaFilter filter,int max);
	/**
	 * 对当前WorkMemory中所有满足条件的规则进行过滤执行，并定义执行数目的最大值，<br>
	 * 并向WorkingMemory中设置一个Map的参数对象
	 * @param parameters 向WorkingMemory中设置一个Map的参数对象
	 * @param filter 对满足条件的规则进行过滤
	 * @param max 执行规则的最大数目
	 * @return 返回一个ExecutionResponse对象，其中包含规则执行耗时，满足条件的规则，执行的规则等信息
	 */
	RuleExecutionResponse fireRules(Map<String,Object> parameters,AgendaFilter filter,int max);
	/**
	 * 对当前WorkMemory中所有满足条件的规则进行执行,并向WorkingMemory中设置一个Map的参数对象
	 * @param parameters 向WorkingMemory中设置一个Map的参数对象
	 * @return 返回一个ExecutionResponse对象，其中包含规则执行耗时，满足条件的规则，执行的规则等信息
	 */
	RuleExecutionResponse fireRules(Map<String,Object> parameters);
	/**
	 * 根据规则流ID，执行目标规则流
	 * @param processId 要执行的规则流ID
	 * @return 返回一个ExecutionResponse对象，其中包含规则流执行耗时信息
	 */
	FlowExecutionResponse startProcess(String processId);
	/**
	 * 根据规则流ID，执行目标规则流,并向WorkingMemory中设置一个Map的参数对象
	 * @param processId 要执行的规则流ID
	 * @param parameters 向WorkingMemory中设置一个Map的参数对象
	 * @return 返回一个ExecutionResponse对象，其中包含规则流执行耗时信息
	 */
	FlowExecutionResponse startProcess(String processId,Map<String,Object> parameters);
}
