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
public interface AgendaEventListener extends KnowledgeEventListener{
	/**
	 * 当规则条件满足时，创建一个包装规则的Activation对象时触发
	 * @param event ActivationCreatedEvent对象
	 */
	void activationCreated(ActivationCreatedEvent event);
	/**
	 * 当因某个Fact对象从WorkingMemory中删除导致某个规则条件不满足时触发
	 * @param event ActivationCancelledEvent对象
	 */
	void activationCancelled(ActivationCancelledEvent event);
	/**
	 * 在执行某个满足条件的规则动作之前触发
	 * @param event ActivationBeforeFiredEvent对象
	 */
	void beforeActivationFired(ActivationBeforeFiredEvent event);
	/**
	 * 在执行某个满足条件的规则动作之前触发
	 * @param event ActivationAfterFiredEvent对象
	 */
	void afterActivationFired(ActivationAfterFiredEvent event);
}
