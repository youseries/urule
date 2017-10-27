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
package com.bstek.urule.console.repository;

import java.io.IOException;
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
	public Resource provide(String path,String version) {
		String newpath=path.substring(4,path.length());
		InputStream inputStream=null;
		if(StringUtils.isEmpty(version) || version.equals("LATEST")){
			inputStream=repositoryService.readFile(newpath,null);			
		}else{
			inputStream=repositoryService.readFile(newpath,version);						
		}
		try {
			String content=IOUtils.toString(inputStream,"utf-8");
			IOUtils.closeQuietly(inputStream);
			return new Resource(content,path);
		} catch (IOException e) {
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
