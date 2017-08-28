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
package com.bstek.urule.runtime.event;
/**
 * @author Jacky.gao
 * @since 2015年7月20日
 */
public interface ProcessEventListener extends KnowledgeEventListener{
	/**
	 * 规则流开始之前触发
	 * @param event ProcessBeforeStartedEvent对象
	 */
	void beforeProcessStarted(ProcessBeforeStartedEvent event);
	/**
	 * 规则流开始之后触发，执行完开始节点后触发
	 * @param event ProcessAfterStartedEvent对象
	 */
	void afterProcessStarted(ProcessAfterStartedEvent event);
	/**
	 * 规则流结束之前触发
	 * @param event ProcessBeforeCompletedEvent对象
	 */
	void beforeProcessCompleted(ProcessBeforeCompletedEvent event);
	/**
	 * 规则流结束之后触发
	 * @param event ProcessAfterCompletedEvent对象
	 */
	void afterProcessCompleted(ProcessAfterCompletedEvent event);
	/**
	 * 流经规则流中每个节点前触发
	 * @param event ProcessBeforeNodeTriggeredEvent对象
	 */
	void beforeNodeTriggered(ProcessBeforeNodeTriggeredEvent event);
	/**
	 * 流经规则流中每个节点后触发
	 * @param event ProcessBeforeNodeTriggeredEvent对象
	 */
	void afterNodeTriggered(ProcessAfterNodeTriggeredEvent event);
}
