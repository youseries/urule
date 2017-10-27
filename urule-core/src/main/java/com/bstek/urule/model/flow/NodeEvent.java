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
