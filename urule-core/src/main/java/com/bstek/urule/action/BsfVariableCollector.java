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
package com.bstek.urule.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.urule.runtime.rete.Context;

/**
 * @author Jacky.gao
 * @since 2015年3月18日
 */
public class BsfVariableCollector implements ApplicationContextAware{
	public static final String BEAN_ID="urule.bsfVariableCollector";
	private Collection<BsfVariableProvider> providers;
	private ApplicationContext applicationContext;
	public Map<String,Object> getVariableMap(Context context){
		Map<String,Object> variableMap=new HashMap<String,Object>();
		variableMap.put("workingMemory", context.getWorkingMemory());
		variableMap.put("applicationContext", applicationContext);
		for(BsfVariableProvider provider:providers){
			Map<String,Object> map=provider.provide();
			if(map==null){
				continue;
			}
			variableMap.putAll(map);
		}
		return variableMap;
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		providers=applicationContext.getBeansOfType(BsfVariableProvider.class).values();
		this.applicationContext=applicationContext;
	}
}
