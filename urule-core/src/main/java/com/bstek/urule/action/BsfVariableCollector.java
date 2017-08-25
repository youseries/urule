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
