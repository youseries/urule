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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.bstek.urule.RuleException;

/**
 * @author Jacky.gao
 * @since 2015年9月29日
 */
public class BatchSessionImpl implements BatchSession {
	private ExecutorService executorService;
	private int batchSize;
	private List<Business> businessList=new ArrayList<Business>();
	private KnowledgePackage knowledgePackage;
	private KnowledgePackage[] knowledgePackages;
	
	public BatchSessionImpl(KnowledgePackage knowledgePackage,int threadSize,int batchSize) {
		this.executorService=Executors.newFixedThreadPool(threadSize);
		this.knowledgePackage=knowledgePackage;
		this.batchSize=batchSize;
	}
	public BatchSessionImpl(KnowledgePackage[] knowledgePackages,int threadSize,int batchSize) {
		this.executorService=Executors.newFixedThreadPool(threadSize);
		this.knowledgePackages=knowledgePackages;
		this.batchSize=batchSize;
	}
	@Override
	public void addBusiness(Business business) {
		if(businessList!=null){
			if(businessList.size()>=batchSize){
				doBusinesses();
				businessList=new ArrayList<Business>();
			}
		}else{
			businessList=new ArrayList<Business>();
		}
		businessList.add(business);
	}
	private void doBusinesses() {
		BatchThread thread=null;
		if(knowledgePackage!=null){
			thread=new BatchThread(knowledgePackage,businessList);
		}else if(knowledgePackages!=null){
			thread=new BatchThread(knowledgePackages,businessList);			
		}else{
			throw new RuleException("KnowledgePackage can not be null.");
		}
		executorService.execute(thread);
		businessList=null;
	}
	@Override
	public void waitForCompletion() {
		if(businessList!=null && businessList.size()>0){
			doBusinesses();
		}
		executorService.shutdown();
		try{
			while(!executorService.awaitTermination(300, TimeUnit.MILLISECONDS)){}
		}catch(InterruptedException ex){
			ex.printStackTrace();
			throw new RuleException(ex);
		}
	}
}
