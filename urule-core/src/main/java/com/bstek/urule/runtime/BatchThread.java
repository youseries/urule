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
