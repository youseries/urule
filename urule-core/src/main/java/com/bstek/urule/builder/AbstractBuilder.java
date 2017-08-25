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
package com.bstek.urule.builder;

import java.util.Collection;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.urule.RuleException;
import com.bstek.urule.builder.resource.ResourceBuilder;
import com.bstek.urule.builder.resource.ResourceProvider;

/**
 * @author Jacky.gao
 * @since 2015年2月16日
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractBuilder implements ApplicationContextAware{
	protected Collection<ResourceProvider> providers;
	protected ApplicationContext applicationContext;
	protected Collection<ResourceBuilder> resourceBuilders;
	public ResourceBase newResourceBase(){
		return new ResourceBase(providers);
	}
	protected Element parseResource(String content){
		try {
			Document document = DocumentHelper.parseText(content);
			Element root=document.getRootElement();
			return root;
		} catch (DocumentException e) {
			throw new RuleException(e);
		}
	}
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		resourceBuilders=applicationContext.getBeansOfType(ResourceBuilder.class).values();
		providers=applicationContext.getBeansOfType(ResourceProvider.class).values();
		this.applicationContext=applicationContext;
		applicationContext.getBeansWithAnnotation(SuppressWarnings.class);
	}
}
