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
package com.bstek.urule.console.repository;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.bstek.urule.RuleException;
import com.bstek.urule.builder.resource.Resource;
import com.bstek.urule.builder.resource.ResourceProvider;

/**
 * @author Jacky.gao
 * @since 2015年3月25日
 */
public class RepositoryResourceProvider implements ResourceProvider {
	public static final String JCR="jcr:";
	private RepositoryService repositoryService;
	
	@Override
	public Resource provide(String path,String version) {
		String newpath=path.substring(4,path.length());
		InputStream inputStream=null;
		try {
			if(StringUtils.isEmpty(version) || version.equals("LATEST")){
				inputStream=repositoryService.readFile(newpath,null);											
			}else{
				inputStream=repositoryService.readFile(newpath,version);														
			}
			String content=IOUtils.toString(inputStream,"utf-8");
			IOUtils.closeQuietly(inputStream);
			return new Resource(content,path);
		} catch (Exception e) {
			throw new RuleException(e);
		}
	}

	public boolean support(String path) {
		return path.startsWith(JCR);
	}
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
}
