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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bstek.urule.RuleException;
import com.bstek.urule.builder.resource.Resource;
import com.bstek.urule.builder.resource.ResourceProvider;

/**
 * @author Jacky.gao
 * @since 2014年12月22日
 */
public class ResourceBase {
	private Collection<ResourceProvider> providers; 
	private List<Resource> resources=new ArrayList<Resource>();
	protected ResourceBase(Collection<ResourceProvider> providers) {
		this.providers=providers;
	}
	public ResourceBase addResource(String path,String version){
		boolean support=false;
		for(ResourceProvider provider:providers){
			if(provider.support(path)){
				support=true;
				resources.add(provider.provide(path,version));
				break;
			}
		}
		if(!support){
			throw new RuleException("Unsupport rule file source : "+path);
		}
		return this;
	}
	public List<Resource> getResources() {
		return resources;
	}
}
