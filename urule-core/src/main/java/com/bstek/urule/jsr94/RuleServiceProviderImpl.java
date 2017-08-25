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
package com.bstek.urule.jsr94;

import javax.rules.ConfigurationException;
import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.admin.RuleAdministrator;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.urule.runtime.service.KnowledgePackageService;

/**
 * @author Jacky.gao
 * @since 2015年3月26日
 */
public class RuleServiceProviderImpl extends RuleServiceProvider implements ApplicationContextAware{
	public static final String BEAN_ID="urule.ruleServiceProvider";
	private KnowledgePackageService knowledgePackageService;
	private RuleRuntime ruleRuntime;
	@Override
	public RuleAdministrator getRuleAdministrator() throws ConfigurationException {
		return null;
	}

	@Override
	public RuleRuntime getRuleRuntime() throws ConfigurationException {
		if(ruleRuntime==null){
			throw new ConfigurationException("No repository so RuleRuntime can not init.");
		}
		return ruleRuntime;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if(applicationContext.containsBean(KnowledgePackageService.BEAN_ID)){
			knowledgePackageService=(KnowledgePackageService)applicationContext.getBean(KnowledgePackageService.BEAN_ID);
			ruleRuntime = new RuleRuntimeImpl(knowledgePackageService);
		}
	}
}
