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

import java.util.List;

import com.bstek.urule.debug.MessageItem;

/**
 * @author Jacky.gao
 * @since 2015年1月28日
 */
public class KnowledgeSessionFactory {
	/**
	 * 创建一个普通的KnowledgeSession对象
	 * @param knowledgePackage 创建KnowledgeSession对象所需要的KnowledgePackage对象
	 * @return 返回一个新的KnowledgeSession对象
	 */
	public static KnowledgeSession newKnowledgeSession(KnowledgePackage knowledgePackage){
		return new KnowledgeSessionImpl(knowledgePackage);
	}
	
	/**
	 * 创建一个普通的KnowledgeSession对象，同时将上级调试信息集合传入，以便于后续调试输出
	 * @param knowledgePackage 创建KnowledgeSession对象所需要的KnowledgePackage对象
	 * @param debugMessageItems 上级调试信息集合
	 * @return 返回一个新的KnowledgeSession对象
	 */
	public static KnowledgeSession newKnowledgeSession(KnowledgePackage knowledgePackage,List<MessageItem> debugMessageItems){
		return new KnowledgeSessionImpl(knowledgePackage,debugMessageItems);
	}	
	
	
	/**
	 * 创建一个普通的KnowledgeSession对象
	 * @param knowledgePackages 创建KnowledgeSession对象所需要的KnowledgePackage集合对象
	 * @return 返回一个新的KnowledgeSession对象
	 */
	public static KnowledgeSession newKnowledgeSession(KnowledgePackage[] knowledgePackages){
		return new KnowledgeSessionImpl(knowledgePackages,null);
	}
	
	/**
	 * 创建一个用于批处理的BatchSession对象，这里默认将开启10个普通的线程池来运行提交的批处理任务，默认将每100个任务放在一个线程里处理
	 * @param knowledgePackage 创建BatchSession对象所需要的KnowledgePackage对象
	 * @return 返回一个新的BatchSession对象
	 */
	public static BatchSession newBatchSession(KnowledgePackage knowledgePackage){
		return new BatchSessionImpl(knowledgePackage,BatchSession.DEFAULT_THREAD_SIZE,BatchSession.DEFAULT_BATCH_SIZE);
	}
	
	/**
	 * 创建一个用于批处理的BatchSession对象，第二个参数来指定线程池中可用线程个数，默认将每100个任务放在一个线程里处理
	 * @param knowledgePackage 创建BatchSession对象所需要的KnowledgePackage对象
	 * @param threadSize 线程池中可用的线程个数
	 * @return 返回一个新的BatchSession对象
	 */
	public static BatchSession newBatchSessionByThreadSize(KnowledgePackage knowledgePackage,int threadSize){
		return new BatchSessionImpl(knowledgePackage,threadSize,BatchSession.DEFAULT_BATCH_SIZE);
	}
	
	/**
	 * 创建一个用于批处理的BatchSession对象，这里默认将开启10个普通的线程池来运行提交的批处理任务，第二个参数用来决定单个线程处理的任务数
	 * @param knowledgePackage 创建BatchSession对象所需要的KnowledgePackage对象
	 * @param batchSize 单个线程处理的任务数
	 * @return 返回一个新的BatchSession对象
	 */
	public static BatchSession newBatchSessionByBatchSize(KnowledgePackage knowledgePackage,int batchSize){
		return new BatchSessionImpl(knowledgePackage,BatchSession.DEFAULT_THREAD_SIZE,batchSize);
	}
	
	/**
	 * 创建一个用于批处理的BatchSession对象，第二个参数来指定线程池中可用线程个数，第三个参数用来决定单个线程处理的任务数
	 * @param knowledgePackage 创建BatchSession对象所需要的KnowledgePackage对象
	 * @param threadSize 线程池中可用的线程个数
	 * @param batchSize 单个线程处理的任务数
	 * @return 返回一个新的BatchSession对象
	 */
	public static BatchSession newBatchSession(KnowledgePackage knowledgePackage,int threadSize,int batchSize){
		return new BatchSessionImpl(knowledgePackage,threadSize,batchSize);
	}
	
	/**
	 * 创建一个用于批处理的BatchSession对象，这里默认将开启10个普通的线程池来运行提交的批处理任务，默认将每100个任务放在一个线程里处理
	 * @param knowledgePackages 创建BatchSession对象所需要的KnowledgePackage集合对象
	 * @return 返回一个新的BatchSession对象
	 */
	public static BatchSession newBatchSession(KnowledgePackage[] knowledgePackages){
		return new BatchSessionImpl(knowledgePackages,BatchSession.DEFAULT_THREAD_SIZE,BatchSession.DEFAULT_BATCH_SIZE);
	}
	
	/**
	 * 创建一个用于批处理的BatchSession对象，第二个参数来指定线程池中可用线程个数，默认将每100个任务放在一个线程里处理
	 * @param knowledgePackages 创建BatchSession对象所需要的KnowledgePackage集合对象
	 * @param threadSize 线程池中可用的线程个数
	 * @return 返回一个新的BatchSession对象
	 */
	public static BatchSession newBatchSessionByThreadSize(KnowledgePackage[] knowledgePackages,int threadSize){
		return new BatchSessionImpl(knowledgePackages,threadSize,BatchSession.DEFAULT_BATCH_SIZE);
	}
	
	/**
	 * 创建一个用于批处理的BatchSession对象，这里默认将开启10个普通的线程池来运行提交的批处理任务，第二个参数用来决定单个线程处理的任务数
	 * @param knowledgePackages 创建BatchSession对象所需要的KnowledgePackage集合对象
	 * @param batchSize 单个线程处理的任务数
	 * @return 返回一个新的BatchSession对象
	 */
	public static BatchSession newBatchSessionByBatchSize(KnowledgePackage[] knowledgePackages,int batchSize){
		return new BatchSessionImpl(knowledgePackages,BatchSession.DEFAULT_THREAD_SIZE,batchSize);
	}
	
	/**
	 * 创建一个用于批处理的BatchSession对象，第二个参数来指定线程池中可用线程个数，第三个参数用来决定单个线程处理的任务数
	 * @param knowledgePackages 创建BatchSession对象所需要的KnowledgePackage集合对象
	 * @param threadSize 线程池中可用的线程个数
	 * @param batchSize 单个线程处理的任务数
	 * @return 返回一个新的BatchSession对象
	 */
	public static BatchSession newBatchSession(KnowledgePackage[] knowledgePackages,int threadSize,int batchSize){
		return new BatchSessionImpl(knowledgePackages,threadSize,batchSize);
	}
}
