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
package com.bstek.urule.runtime.cache;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Jacky.gao
 * @since 2016年12月29日
 */
public class CacheUtils implements ApplicationContextAware{
	private static KnowledgeCache knowledgeCache;
	public static KnowledgeCache getKnowledgeCache() {
		return knowledgeCache;
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Collection<KnowledgeCache> caches=applicationContext.getBeansOfType(KnowledgeCache.class).values();
		if(caches.size()>0){
			CacheUtils.knowledgeCache=caches.iterator().next();
		}else{
			CacheUtils.knowledgeCache=new MemoryKnowledgeCache();
		}
	}
}
