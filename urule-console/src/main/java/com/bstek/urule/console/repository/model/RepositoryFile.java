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
package com.bstek.urule.console.repository.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author Jacky.gao
 * @since 2014年12月24日
 */
public class RepositoryFile {
	private String id;
	private String name;
	private String fullPath;
	private Type type;
	private Type folderType;
	private boolean lock;
	private String lockInfo;
	@JsonIgnore
	private LibType libType;
	@JsonIgnore
	private RepositoryFile parentFile;
	private List<RepositoryFile> children;
	public RepositoryFile() {
		this.id=UUID.randomUUID().toString();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public RepositoryFile getParentFile() {
		return parentFile;
	}
	public void setParentFile(RepositoryFile parentFile) {
		this.parentFile = parentFile;
	}
	public LibType getLibType() {
		return libType;
	}
	public void setLibType(LibType libType) {
		this.libType = libType;
	}
	public List<RepositoryFile> getChildren() {
		return children;
	}
	public void addChild(RepositoryFile fileInfo,boolean isdir) {
		if(this.children==null){
			this.children=new ArrayList<RepositoryFile>();
		}
		fileInfo.setParentFile(this);
		if(isdir){
			this.children.add(0,fileInfo);
		}else{
			this.children.add(fileInfo);
		}
	}
	public void setChildren(List<RepositoryFile> children) {
		this.children = children;
	}
	public String getFullPath(){
		if(fullPath==null){
			if(parentFile!=null){
				fullPath=parentFile.getFullPath();
			}else{
				fullPath="";
			}
			if(fullPath.equals("/")){
				fullPath="";
			}
			fullPath+="/"+name;
		}
		return fullPath;
	}
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}

	public Type getFolderType() {
		return folderType;
	}

	public void setFolderType(Type folderType) {
		this.folderType = folderType;
	}

	public boolean isLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	public String getLockInfo() {
		return lockInfo;
	}

	public void setLockInfo(String lockInfo) {
		this.lockInfo = lockInfo;
	}
}
