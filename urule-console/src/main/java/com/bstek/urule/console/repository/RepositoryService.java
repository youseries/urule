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
import com.bstek.urule.console.repository.model.ResourcePackage;
import com.bstek.urule.console.repository.model.VersionFile;
import com.bstek.urule.console.servlet.permission.UserPermission;

/**
 * @author Jacky.gao
 * @since 2015年3月24日
 */
public interface RepositoryService {
	public static final String BEAN_ID="urule.repositoryService";
	boolean fileExistCheck(String filePath);
	RepositoryFile createProject(String projectName,User user,boolean classify);
	void createDir(String path,User user);
	void createFile(String path,String content,String createUser);
	void saveFile(String path,String content,String createUser,boolean newVersion,String versionComment);
	void deleteFile(String path);
	Repository loadRepository(String project,String companyId,boolean classify,FileType[] types,String searchFileName);
	List<RepositoryFile> loadProject(String companyId);
	void fileRename(String path, String newPath);
	List<String> getReferenceFiles(String path,String searchText);
	InputStream readFile(String path,String version);
	List<ResourcePackage> loadProjectResourcePackages(String project) throws Exception;
	List<VersionFile> getVersionFiles(String path);
	void exportXml(String projectPath,OutputStream outputStream);
	void importXml(InputStream inputStream,boolean overwrite);
	List<RepositoryFile> getDirectories(String project) throws Exception;
	List<ClientConfig> loadClientConfigs(String project);
	List<UserPermission> loadResourceSecurityConfigs(String companyId);
}
