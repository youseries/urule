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
	 * 创建一个普通的KnowledgeSession对象
	 * @param knowledgePackages 创建KnowledgeSession对象所需要的KnowledgePackage集合对象
	 * @return 返回一个新的KnowledgeSession对象
	 */
	public static KnowledgeSession newKnowledgeSession(KnowledgePackage[] knowledgePackages){
		return new KnowledgeSessionImpl(knowledgePackages);
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
