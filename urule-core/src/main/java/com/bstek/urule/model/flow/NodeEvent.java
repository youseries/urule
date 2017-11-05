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
package com.bstek.urule.model.flow;

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.ProcessInstance;

/**
 * @author Jacky.gao
 * @since 2015年4月20日
 */
public interface NodeEvent {
	/**
	 * 规则流流入当前节点触发的方法
	 * @param node 当前节点对象
	 * @param instance 当前规则流实例对象
	 * @param context 规则流上下文件对象
	 */
	void enter(FlowNode node,ProcessInstance instance,FlowContext context);
	/**
	 * 规则流流出当前节点触发的方法
	 * @param node 当前节点对象
	 * @param instance 当前规则流实例对象
	 * @param context 规则流上下文件对象
	 */
	void leave(FlowNode node,ProcessInstance instance,FlowContext context);
}
