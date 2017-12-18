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
package com.bstek.urule.console;

import java.io.IOException;
import java.util.List;

import com.bstek.urule.RuleException;
import com.bstek.urule.builder.KnowledgeBase;
import com.bstek.urule.builder.KnowledgeBuilder;
import com.bstek.urule.builder.ResourceBase;
import com.bstek.urule.console.repository.RepositoryService;
import com.bstek.urule.console.repository.model.ResourceItem;
import com.bstek.urule.console.repository.model.ResourcePackage;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.service.KnowledgePackageService;

/**
 * @author Jacky.gao
 * @since 2016年6月22日
 */
public class DefaultKnowledgePackageService implements KnowledgePackageService{
	private KnowledgeBuilder knowledgeBuilder;
	private RepositoryService repositoryService;
	
	@Override
	public KnowledgePackage buildKnowledgePackage(String packageInfo) throws IOException{
		try{
			String[] info=packageInfo.split("/");
			if(info.length!=2){
				throw new RuleException("PackageInfo ["+packageInfo+"] is invalid. Correct such as \"projectName/packageId\".");
			}
			String project=info[0];
			String packageId=info[1];
			List<ResourcePackage> packages=repositoryService.loadProjectResourcePackages(project);
			List<ResourceItem> list=null;
			for(ResourcePackage p:packages){
				if(p.getId().equals(packageId)){
					list=p.getResourceItems();
					break;
				}
			}
			if(list==null){
				throw new RuleException("PackageId ["+packageId+"] was not found in project ["+project+"].");
			}
			ResourceBase resourceBase=knowledgeBuilder.newResourceBase();
			for(ResourceItem item:list){
				resourceBase.addResource(item.getPath(),item.getVersion());
			}
			KnowledgeBase knowledgeBase=knowledgeBuilder.buildKnowledgeBase(resourceBase);
			KnowledgePackage knowledgePackage=knowledgeBase.getKnowledgePackage();
			return knowledgePackage;
		}catch(Exception ex){
			throw new RuleException(ex);
		}
	}

	public void setKnowledgeBuilder(KnowledgeBuilder knowledgeBuilder) {
		this.knowledgeBuilder = knowledgeBuilder;
	}
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
}
