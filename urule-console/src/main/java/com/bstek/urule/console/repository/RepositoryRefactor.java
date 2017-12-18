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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

import org.apache.tika.io.IOUtils;

import com.bstek.urule.RuleException;
import com.bstek.urule.console.repository.model.FileType;

/**
 * @author Jacky.gao
 * @since 2016年5月25日
 */
public class RepositoryRefactor {
	private RepositoryService repositoryService;
	public RepositoryRefactor(RepositoryService repositoryService) {
		this.repositoryService=repositoryService;
	}
	
	public List<String> getReferenceFiles(Node rootNode,String path,String searchText) throws Exception{
		List<String> referenceFiles=new ArrayList<String>();
		List<String> files=getFiles(rootNode, path);
		for(String nodePath:files){
			InputStream inputStream=repositoryService.readFile(nodePath,null);
			try {
				String content = IOUtils.toString(inputStream);
				inputStream.close();
				boolean containPath=content.contains(path);
				boolean containText=content.contains(searchText);
				if(containPath && containText){
					referenceFiles.add(nodePath);
				}
			} catch (IOException e) {
				throw new RuleException(e);
			}
		}
		return referenceFiles;
	}
		
	public List<String> getFiles(Node rootNode,String path){
		String project=getProject(path);
		try{
			List<String> list=new ArrayList<String>();
			Node projectNode=rootNode.getNode(project);		
			buildPath(list, projectNode);
			return list;
		}catch(Exception ex){
			throw new RuleException(ex);
		}
	}
	private void buildPath(List<String> list, Node parentNode) throws RepositoryException {
		NodeIterator nodeIterator=parentNode.getNodes();
		while(nodeIterator.hasNext()){
			Node node=nodeIterator.nextNode();
			String nodePath=node.getPath();
			if(nodePath.endsWith(FileType.Ruleset.toString())){
				list.add(nodePath);
			}else if(nodePath.endsWith(FileType.UL.toString())){
				list.add(nodePath);
			}else if(nodePath.endsWith(FileType.DecisionTable.toString())){
				list.add(nodePath);
			}else if(nodePath.endsWith(FileType.ScriptDecisionTable.toString())){
				list.add(nodePath);
			}else if(nodePath.endsWith(FileType.DecisionTree.toString())){
				list.add(nodePath);					
			}else if(nodePath.endsWith(FileType.RuleFlow.toString())){
				list.add(nodePath);					
			}
			buildPath(list,node);
		}
	}
	
	private String getProject(String path){
		if(path.startsWith("/")){
			path=path.substring(1);
		}
		int pos=path.indexOf("/");
		return path.substring(0,pos);
	}
}
