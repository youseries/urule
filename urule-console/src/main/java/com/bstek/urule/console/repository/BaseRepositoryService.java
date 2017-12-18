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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.lock.LockManager;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.jcr.version.VersionManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.core.RepositoryImpl;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.urule.RuleException;
import com.bstek.urule.console.DefaultRepositoryInteceptor;
import com.bstek.urule.console.RepositoryInteceptor;
import com.bstek.urule.console.repository.model.RepositoryFile;
import com.bstek.urule.console.repository.model.ResourceItem;
import com.bstek.urule.console.repository.model.ResourcePackage;
import com.bstek.urule.console.repository.model.Type;
import com.bstek.urule.console.repository.model.VersionFile;

/**
 * @author Jacky.gao
 * @since 2017年12月15日
 */
public abstract class BaseRepositoryService implements RepositoryReader,ApplicationContextAware {
	public static final String RES_PACKGE_FILE="___res__package__file__";
	public static final String CLIENT_CONFIG_FILE="___client_config__file__";
	public static final String RESOURCE_SECURITY_CONFIG_FILE="___resource_security_config__file__";
	protected final String DATA = "_data";
	protected final String DIR_TAG = "_dir";
	protected final String FILE = "_file";
	protected final String CRATE_USER = "_create_user";
	protected final String CRATE_DATE = "_create_date";
	protected final String VERSION_COMMENT="_version_comment";
	protected final String COMPANY_ID="_company_id";

	protected RepositoryBuilder repositoryBuilder;
	protected RepositoryImpl repository;
	protected Session session;
	protected VersionManager versionManager;
	protected LockManager lockManager;
	
	protected RepositoryInteceptor repositoryInteceptor;
	
	@Override
	public List<RepositoryFile> loadProjects(String companyId) throws Exception{
		List<RepositoryFile> projects=new ArrayList<RepositoryFile>();
		Node rootNode=getRootNode();
		NodeIterator nodeIterator = rootNode.getNodes();
		while (nodeIterator.hasNext()) {
			Node projectNode = nodeIterator.nextNode();
			if (!projectNode.hasProperty(FILE)) {
				continue;
			}
			if(StringUtils.isNotEmpty(companyId)){
				if(projectNode.hasProperty(COMPANY_ID)){
					String id=projectNode.getProperty(COMPANY_ID).getString();
					if(!companyId.equals(id)){
						continue;
					}
				}
			}
			if(projectNode.getName().indexOf(RESOURCE_SECURITY_CONFIG_FILE)>-1){
				continue;
			}
			RepositoryFile projectFile = new RepositoryFile();
			projectFile.setType(Type.project);
			projectFile.setName(projectNode.getName());
			projectFile.setFullPath("/" + projectNode.getName());
			projects.add(projectFile);
		}
		return projects;
	}
	
	@Override
	public List<VersionFile> getVersionFiles(String path) throws Exception{
		path = processPath(path);
		Node rootNode=getRootNode();
		if (!rootNode.hasNode(path)) {
			throw new RuleException("File [" + path + "] not exist.");
		}
		List<VersionFile> files = new ArrayList<VersionFile>();
		Node fileNode = rootNode.getNode(path);
		VersionHistory versionHistory = versionManager.getVersionHistory(fileNode.getPath());
		VersionIterator iterator = versionHistory.getAllVersions();
		while (iterator.hasNext()) {
			Version version = iterator.nextVersion();
			String versionName = version.getName();
			if (versionName.startsWith("jcr:")) {
				continue; // skip root version
			}
			Node fnode = version.getFrozenNode();
			VersionFile file = new VersionFile();
			file.setName(version.getName());
			file.setPath(fileNode.getPath());
			Property prop = fnode.getProperty(CRATE_USER);
			file.setCreateUser(prop.getString());
			prop = fnode.getProperty(CRATE_DATE);
			file.setCreateDate(prop.getDate().getTime());
			if(fnode.hasProperty(VERSION_COMMENT)){
				prop=fnode.getProperty(VERSION_COMMENT);
				file.setComment(prop.getString());
			}
			files.add(file);
		}
		return files;
	}
	
	@Override
	public InputStream readFile(String path) throws Exception{
		return readFile(path, null);
	}
	
	@Override
	public InputStream readFile(String path,String version) throws Exception{
		if(StringUtils.isNotBlank(version)){
			repositoryInteceptor.readFile(path+":"+version);
			return readVersionFile(path, version);
		}
		repositoryInteceptor.readFile(path);
		Node rootNode=getRootNode();
		int colonPos = path.lastIndexOf(":");
		if (colonPos > -1) {
			version = path.substring(colonPos + 1, path.length());
			path = path.substring(0, colonPos);
			return readFile(path, version);
		}
		path = processPath(path);
		if (!rootNode.hasNode(path)) {
			throw new RuleException("File [" + path + "] not exist.");
		}
		Node fileNode = rootNode.getNode(path);
		Property property = fileNode.getProperty(DATA);
		Binary fileBinary = property.getBinary();
		return fileBinary.getStream();
	}
	
	
	private InputStream readVersionFile(String path, String version) throws Exception{
		path = processPath(path);
		Node rootNode=getRootNode();
		if (!rootNode.hasNode(path)) {
			throw new RuleException("File [" + path + "] not exist.");
		}
		Node fileNode = rootNode.getNode(path);
		VersionHistory versionHistory = versionManager.getVersionHistory(fileNode.getPath());
		Version v = versionHistory.getVersion(version);
		Node fnode = v.getFrozenNode();
		Property property = fnode.getProperty(DATA);
		Binary fileBinary = property.getBinary();
		return fileBinary.getStream();
	}
	
	@Override
	public List<ResourcePackage> loadProjectResourcePackages(String project) throws Exception {
		Node rootNode=getRootNode();
		String filePath = processPath(project) + "/" + RES_PACKGE_FILE;
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Node fileNode = rootNode.getNode(filePath);
		Property property = fileNode.getProperty(DATA);
		Binary fileBinary = property.getBinary();
		InputStream inputStream = fileBinary.getStream();
		String content = IOUtils.toString(inputStream, "utf-8");
		inputStream.close();
		Document document = DocumentHelper.parseText(content);
		Element rootElement = document.getRootElement();
		List<ResourcePackage> packages = new ArrayList<ResourcePackage>();
		for (Object obj : rootElement.elements()) {
			if (!(obj instanceof Element)) {
				continue;
			}
			Element element = (Element) obj;
			if (!element.getName().equals("res-package")) {
				continue;
			}
			ResourcePackage p = new ResourcePackage();
			String dateStr = element.attributeValue("create_date");
			if (dateStr != null) {
				p.setCreateDate(sd.parse(dateStr));
			}
			p.setId(element.attributeValue("id"));
			p.setName(element.attributeValue("name"));
			p.setProject(project);
			List<ResourceItem> items = new ArrayList<ResourceItem>();
			for (Object o : element.elements()) {
				if (!(o instanceof Element)) {
					continue;
				}
				Element ele = (Element) o;
				if (!ele.getName().equals("res-package-item")) {
					continue;
				}
				ResourceItem item = new ResourceItem();
				item.setName(ele.attributeValue("name"));
				item.setPackageId(p.getId());
				item.setPath(ele.attributeValue("path"));
				item.setVersion(ele.attributeValue("version"));
				items.add(item);
			}
			p.setResourceItems(items);
			packages.add(p);
		}
		return packages;
	}
	
	protected String processPath(String path) {
		if (path.startsWith("/")) {
			return path.substring(1, path.length());
		}
		return path;
	}

	protected Node getRootNode() throws Exception{
		return session.getRootNode();
	}
	
	public void setRepositoryBuilder(RepositoryBuilder repositoryBuilder) {
		this.repositoryBuilder = repositoryBuilder;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		try {
			repository = repositoryBuilder.getRepository();
			SimpleCredentials cred = new SimpleCredentials("admin", "admin".toCharArray());
			cred.setAttribute("AutoRefresh", true);
			session = repository.login(cred, null);
			versionManager = session.getWorkspace().getVersionManager();
			lockManager=session.getWorkspace().getLockManager();
			Collection<RepositoryInteceptor> repositoryInteceptors=applicationContext.getBeansOfType(RepositoryInteceptor.class).values();
			if(repositoryInteceptors.size()==0){
				repositoryInteceptor=new DefaultRepositoryInteceptor();
			}else{
				repositoryInteceptor=repositoryInteceptors.iterator().next();
			}
		} catch (Exception ex) {
			throw new RuleException(ex);
		}
	}
}
