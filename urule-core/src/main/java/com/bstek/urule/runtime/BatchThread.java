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

/**
 * @author Jacky.gao
 * @since 2015年9月29日
 */
public class BatchThread implements Runnable {
	private List<Business> businesses;
	private KnowledgeSession session;
	public BatchThread(KnowledgePackage knowledgePackage,List<Business> businesses) {
		session=KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);			
		this.businesses=businesses;
	}
	public BatchThread(KnowledgePackage[] knowledgePackages,List<Business> businesses) {
		session=KnowledgeSessionFactory.newKnowledgeSession(knowledgePackages);
		this.businesses=businesses;
	}
	@Override
	public void run() {
		Thread thread=Thread.currentThread();
		String oldThreadName=thread.getName();
		thread.setName("urule-"+oldThreadName);
		try{
			int size=businesses.size();
			for(int i=0;i<size;i++){
				Business business=businesses.get(i);
				business.execute(session);
			}
		}finally{
			thread.setName(oldThreadName);
		}
	}
}
