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
import java.io.OutputStream;
import java.util.List;

import com.bstek.urule.console.User;
import com.bstek.urule.console.repository.model.FileType;
import com.bstek.urule.console.repository.model.RepositoryFile;
import com.bstek.urule.console.repository.model.VersionFile;
import com.bstek.urule.console.servlet.permission.UserPermission;

/**
 * @author Jacky.gao
 * @since 2015年3月24日
 */
public interface RepositoryService extends RepositoryReader{
	public static final String BEAN_ID="urule.repositoryService";
	boolean fileExistCheck(String filePath) throws Exception;
	RepositoryFile createProject(String projectName,User user,boolean classify) throws Exception;
	void createDir(String path,User user) throws Exception;
	void createFile(String path,String content,User user) throws Exception;
	void saveFile(String path,String content,boolean newVersion,String versionComment,User user) throws Exception;
	void deleteFile(String path,User user)throws Exception;
	void lockPath(String path,User user) throws Exception;
	void unlockPath(String path,User user) throws Exception;
	Repository loadRepository(String project,User user,boolean classify,FileType[] types,String searchFileName) throws Exception;
	void fileRename(String path, String newPath) throws Exception;
	List<String> getReferenceFiles(String path,String searchText) throws Exception;
	InputStream readFile(String path,String version) throws Exception;
	List<VersionFile> getVersionFiles(String path) throws Exception;
	void exportXml(String projectPath,OutputStream outputStream)throws Exception;
	void importXml(InputStream inputStream,boolean overwrite)throws Exception;
	List<RepositoryFile> getDirectories(String project) throws Exception;
	List<ClientConfig> loadClientConfigs(String project)throws Exception;
	List<UserPermission> loadResourceSecurityConfigs(String companyId) throws Exception;
}
