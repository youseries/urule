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
