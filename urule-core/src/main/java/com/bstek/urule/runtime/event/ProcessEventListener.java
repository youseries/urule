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
