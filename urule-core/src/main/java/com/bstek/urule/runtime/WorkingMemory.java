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
package com.bstek.urule.runtime;

import java.util.List;
import java.util.Map;


/**
 * @author Jacky.gao
 * @since 2015年1月8日
 */
public interface WorkingMemory extends EventManager{
	/**
	 * 插入一个业务数据对象，对应到规则当中就是一个变量对象
	 * @param fact 目标业务数据对象
	 * @return 插入是否成功
	 */
	boolean insert(Object fact);
	/**
	 * 利用当前WorkingMemory中的规则信息来评估当前与业务对象，看其是否会满足相关规则的条件，同时将该对象插入到WorkingMemory
	 * @param fact 要评估的对象
	 */
	void assertFact(Object fact);
	/**
	 * 更新一个在当前WorkingMemory中已存在的业务对象，如果对象存在，那么WorkingMemory会重新评估这个对象
	 * @param fact 要更新的对象
	 * @return 更新是否成功，如果对象不在WorkingMemory中，则返回false
	 */
	boolean update(Object fact);
	/**
	 * 移除一个在WorkingMemory中的对象，如果对象存在，那么会尝试对已满足条件的规则进行重新评估，
	 * 看看当前对象的移除是否会影响已满足条件的规则，如果有影响，则同样移除已满足条件的规则
	 * @param obj 要移除的对象
	 * @return 是否移除成功，如果在WorkingMemory中存在，则返回true,否则返回false
	 */
	boolean retract(Object obj);
	/**
	 * 获取当前WorkingMemory中的某个参数值
	 * @param key 参数对应的key值
	 * @return 返回具体的值
	 */
	Object getParameter(String key);
	/**
	 * @return 返回所有的参数对象
	 */
	Map<String,Object> getParameters();
	/**
	 * @return 返回当前WorkingMemory中所有的业务数据对象
	 */
	List<Object> getAllFacts();
	/**
	 * @return 返回所有WorkingMemory中所有历史业务数据对象
	 */
	List<Object> getHistoryFacts();
	
}
