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
}
