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
