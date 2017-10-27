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
package com.bstek.urule.runtime.service;


import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.urule.RuleException;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.cache.CacheUtils;

/**
 * @author Jacky.gao
 * @since 2015年1月28日
 */
public class KnowledgeServiceImpl implements KnowledgeService,ApplicationContextAware {
	private long knowledgeUpdateCycle;
	private RemoteService remoteService;
	private KnowledgePackageService knowledgePackageService;
	private Logger log=Logger.getLogger(KnowledgeServiceImpl.class.getName());
	@Override
	public KnowledgePackage[] getKnowledges(String[] packageIds)  throws IOException{
		KnowledgePackage[] packages=new KnowledgePackage[packageIds.length];
		for(int i=0;i<packageIds.length;i++){
			String packageId=packageIds[i];
			packages[i]=getKnowledge(packageId);
		}
		return packages;
	}
	
	public KnowledgePackage getKnowledge(String packageId) throws IOException{
		if(knowledgeUpdateCycle==0){
			return fetchKnowledgePackage(packageId);
		}
		KnowledgePackage knowledgePackage=CacheUtils.getKnowledgeCache().getKnowledge(packageId);
		if(knowledgeUpdateCycle==1){
			if(knowledgePackage==null){
				knowledgePackage=fetchKnowledgePackage(packageId);
				knowledgePackage.resetTimestamp();
				CacheUtils.getKnowledgeCache().putKnowledge(packageId, knowledgePackage);
			}
			return knowledgePackage;
		}
		if(knowledgePackage==null){
			knowledgePackage=fetchKnowledgePackage(packageId);
			CacheUtils.getKnowledgeCache().putKnowledge(packageId, knowledgePackage);
		}else{
			long timestamp=knowledgePackage.getTimestamp();
			long now=System.currentTimeMillis();
			long mm=now-timestamp;
			if(mm>=knowledgeUpdateCycle){
				KnowledgePackage remoteKnowledgePackage=remoteService.getKnowledge(packageId, String.valueOf(knowledgePackage.getTimestamp()));
				if(remoteKnowledgePackage==null){
					//表示repository中knowledgepackage与本地的比较无更新
					log.info("Not need update remote knowledgepackage.");
					knowledgePackage.resetTimestamp();
					CacheUtils.getKnowledgeCache().putKnowledge(packageId, knowledgePackage);
				}else{
					log.info("Need update remote knowledgepackage.");
					remoteKnowledgePackage.resetTimestamp();
					CacheUtils.getKnowledgeCache().putKnowledge(packageId, remoteKnowledgePackage);
				}
			}
		}
		return knowledgePackage;
	}

	private KnowledgePackage fetchKnowledgePackage(String packageId) throws IOException{
		KnowledgePackage knowledgePackage=remoteService.getKnowledge(packageId, null);
		if(knowledgePackage==null){
			//表示无法连接远程server.
			if(knowledgePackageService==null){
				throw new RuleException("Remote service and local KnowledgePackageService all unavailable");
			}else{
				knowledgePackage=knowledgePackageService.buildKnowledgePackage(packageId);
			}
		}
		return knowledgePackage;
	}
	public void setRemoteService(RemoteService remoteService) {
		this.remoteService = remoteService;
	}
	public void setKnowledgeUpdateCycle(String knowledgeUpdateCycle) {
		if(StringUtils.isEmpty(knowledgeUpdateCycle) || knowledgeUpdateCycle.equals("${urule.knowledgeUpdateCycle}")){
			return;
		}
		this.knowledgeUpdateCycle = Long.valueOf(knowledgeUpdateCycle);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		boolean hasBean=applicationContext.containsBean(KnowledgePackageService.BEAN_ID);
		if(hasBean){
			knowledgePackageService=(KnowledgePackageService)applicationContext.getBean(KnowledgePackageService.BEAN_ID);		
		}
	}
}
