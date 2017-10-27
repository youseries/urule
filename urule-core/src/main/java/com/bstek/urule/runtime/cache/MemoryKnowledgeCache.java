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

import java.util.Hashtable;
import java.util.Map;

import com.bstek.urule.runtime.KnowledgePackage;

/**
 * @author Jacky.gao
 * @since 2015年1月28日
 */
public class MemoryKnowledgeCache implements KnowledgeCache{
	private Map<String,Object> map=new Hashtable<String,Object>();
	public KnowledgePackage getKnowledge(String packageId) {
		if(packageId.startsWith("/")){
			packageId=packageId.substring(1,packageId.length());
		}
		return (KnowledgePackage)map.get(packageId);
	}

	public void putKnowledge(String packageId,KnowledgePackage knowledgePackage) {
		if(packageId.startsWith("/")){
			packageId=packageId.substring(1,packageId.length());
		}
		map.put(packageId, knowledgePackage);
	}

	public void removeKnowledge(String packageId) {
		map.remove(packageId);
	}
}
