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
	List<String> getReferenceFiles(String path);
	InputStream readFile(String path,String version);
	List<ResourcePackage> loadProjectResourcePackages(String project) throws Exception;
	List<VersionFile> getVersionFiles(String path);
	void exportXml(String projectPath,OutputStream outputStream);
	void importXml(InputStream inputStream,boolean overwrite);
	List<RepositoryFile> getDirectories(String project) throws Exception;
	List<ClientConfig> loadClientConfigs(String project);
	List<UserPermission> loadResourceSecurityConfigs(String companyId);
}
