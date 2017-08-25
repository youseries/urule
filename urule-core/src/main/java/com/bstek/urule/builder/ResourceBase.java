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
