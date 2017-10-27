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
