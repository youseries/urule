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
/**
 * @author Jacky.gao
 * @since 2015年9月29日
 */
public interface BatchSession {
	public static final int DEFAULT_BATCH_SIZE=100;
	public static final int DEFAULT_THREAD_SIZE=10;
	
	
	/**
	 * 添加一个具体要执行Business对象
	 * @param business Business对象实例
	 */
	void addBusiness(Business business);
	
	
	/**
	 * 等待线程池中所有业务线程执行完成，在进行批处理操作时一定要以此方法作为方法调用结尾
	 */
	void waitForCompletion();
}
