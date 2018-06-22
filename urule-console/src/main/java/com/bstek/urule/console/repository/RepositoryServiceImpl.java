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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jcr.Binary;
import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.lock.Lock;
import javax.jcr.nodetype.NodeType;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.value.BinaryImpl;
import org.apache.jackrabbit.value.DateValue;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.context.ApplicationContextAware;

import com.bstek.urule.RuleException;
import com.bstek.urule.Utils;
import com.bstek.urule.console.User;
import com.bstek.urule.console.exception.NoPermissionException;
import com.bstek.urule.console.repository.model.FileType;
import com.bstek.urule.console.repository.model.LibType;
import com.bstek.urule.console.repository.model.RepositoryFile;
import com.bstek.urule.console.repository.model.Type;
import com.bstek.urule.console.repository.permission.PermissionService;
import com.bstek.urule.console.servlet.permission.ProjectConfig;
import com.bstek.urule.console.servlet.permission.UserPermission;

/**
 * @author Jacky.gao
 * @since 2016年5月24日
 */
public class RepositoryServiceImpl extends BaseRepositoryService implements RepositoryService, ApplicationContextAware {
	private PermissionService permissionService;
	
	@Override
	public List<UserPermission> loadResourceSecurityConfigs(String companyId) throws Exception{
		List<UserPermission> configs=new ArrayList<UserPermission>();
		String filePath=RESOURCE_SECURITY_CONFIG_FILE+(companyId == null ? "" : companyId);
		Node rootNode=getRootNode();
		Node fileNode = rootNode.getNode(filePath);
		Property property = fileNode.getProperty(DATA);
		Binary fileBinary = property.getBinary();
		InputStream inputStream = fileBinary.getStream();
		String content = IOUtils.toString(inputStream, "utf-8");
		inputStream.close();
		Document document = DocumentHelper.parseText(content);
		Element rootElement = document.getRootElement();
		for (Object obj : rootElement.elements()) {
			if (!(obj instanceof Element)) {
				continue;
			}
			Element element = (Element) obj;
			if (!element.getName().equals("user-permission")) {
				continue;
			}
			UserPermission userResource=new UserPermission();
			userResource.setUsername(element.attributeValue("username"));
			userResource.setProjectConfigs(parseProjectConfigs(element));
			configs.add(userResource);
		}
		return configs;
	}
	
	private List<ProjectConfig> parseProjectConfigs(Element element){
		List<ProjectConfig> list=new ArrayList<ProjectConfig>();
		for (Object obj : element.elements()) {
			if (!(obj instanceof Element)) {
				continue;
			}
			Element ele = (Element) obj;
			if (!ele.getName().equals("project-config")) {
				continue;
			}
			ProjectConfig config=new ProjectConfig();
			config.setProject(ele.attributeValue("project"));
			config.setReadProject(parseBooleanValue(ele, "read-project"));
			
			config.setReadPackage(parseBooleanValue(ele, "read-package"));
			config.setWritePackage(parseBooleanValue(ele, "write-package"));
			
			config.setReadVariableFile(parseBooleanValue(ele, "read-variable-file"));
			config.setWriteVariableFile(parseBooleanValue(ele, "write-variable-file"));
			
			config.setReadParameterFile(parseBooleanValue(ele, "read-parameter-file"));
			config.setWriteParameterFile(parseBooleanValue(ele, "write-parameter-file"));
			
			config.setReadConstantFile(parseBooleanValue(ele, "read-constant-file"));
			config.setWriteConstantFile(parseBooleanValue(ele, "write-constant-file"));
			
			config.setReadActionFile(parseBooleanValue(ele, "read-action-file"));
			config.setWriteActionFile(parseBooleanValue(ele, "write-action-file"));
			
			config.setReadRuleFile(parseBooleanValue(ele, "read-rule-file"));
			config.setWriteRuleFile(parseBooleanValue(ele, "write-rule-file"));
			
			config.setReadScorecardFile(parseBooleanValue(ele, "read-scorecard-file"));
			config.setWriteScorecardFile(parseBooleanValue(ele, "write-scorecard-file"));
			
			config.setReadDecisionTableFile(parseBooleanValue(ele, "read-decision-table-file"));
			config.setWriteDecisionTableFile(parseBooleanValue(ele, "write-decision-table-file"));
			
			config.setReadDecisionTreeFile(parseBooleanValue(ele, "read-decision-tree-file"));
			config.setWriteDecisionTreeFile(parseBooleanValue(ele, "write-decision-tree-file"));
			
			config.setReadFlowFile(parseBooleanValue(ele, "read-flow-file"));
			config.setWriteFlowFile(parseBooleanValue(ele, "write-flow-file"));
			list.add(config);
		}
		return list;
	}
	
	private boolean parseBooleanValue(Element element,String attributeName){
		if(element.attributeValue(attributeName)!=null){
			return Boolean.valueOf(element.attributeValue(attributeName));
		}
		return false;
	}
	
	@Override
	public List<ClientConfig> loadClientConfigs(String project) throws Exception{
		if(!permissionService.isAdmin()){
			throw new NoPermissionException();
		}
		List<ClientConfig> clients=new ArrayList<ClientConfig>();
		Node rootNode=getRootNode();
		String filePath = processPath(project) + "/" + CLIENT_CONFIG_FILE;
		Node fileNode = rootNode.getNode(filePath);
		Property property = fileNode.getProperty(DATA);
		Binary fileBinary = property.getBinary();
		InputStream inputStream = fileBinary.getStream();
		String content = IOUtils.toString(inputStream, "utf-8");
		inputStream.close();
		Document document = DocumentHelper.parseText(content);
		Element rootElement = document.getRootElement();
		for (Object obj : rootElement.elements()) {
			if (!(obj instanceof Element)) {
				continue;
			}
			Element element = (Element) obj;
			if (!element.getName().equals("item")) {
				continue;
			}
			ClientConfig client = new ClientConfig();
			client.setName(element.attributeValue("name"));
			client.setClient(element.attributeValue("client"));
			client.setProject(project);
			clients.add(client);
		}
		return clients;
	}

	@Override
	public List<RepositoryFile> getDirectories(String project) throws Exception {
		Node rootNode=getRootNode();
		NodeIterator nodeIterator = rootNode.getNodes();
		Node targetProjectNode = null;
		while (nodeIterator.hasNext()) {
			Node projectNode = nodeIterator.nextNode();
			if (!projectNode.hasProperty(FILE)) {
				continue;
			}
			String projectName = projectNode.getName();
			if (project != null && !project.equals(projectName)) {
				continue;
			}
			targetProjectNode = projectNode;
			break;
		}
		if (targetProjectNode == null) {
			throw new RuleException("Project [" + project + "] not exist.");
		}
		List<RepositoryFile> fileList = new ArrayList<RepositoryFile>();
		RepositoryFile root = new RepositoryFile();
		root.setName("根目录");
		String projectPath = targetProjectNode.getPath();
		root.setFullPath(projectPath);
		fileList.add(root);
		NodeIterator projectNodeIterator = targetProjectNode.getNodes();
		while (projectNodeIterator.hasNext()) {
			Node dirNode = projectNodeIterator.nextNode();
			if (!dirNode.hasProperty(DIR_TAG)) {
				continue;
			}
			RepositoryFile file = new RepositoryFile();
			file.setName(dirNode.getPath().substring(projectPath.length()));
			file.setFullPath(dirNode.getPath());
			fileList.add(file);
			buildDirectories(dirNode, fileList, projectPath);
		}
		return fileList;
	}

	private void buildDirectories(Node node, List<RepositoryFile> fileList, String projectPath) throws Exception {
		NodeIterator nodeIterator = node.getNodes();
		while (nodeIterator.hasNext()) {
			Node dirNode = nodeIterator.nextNode();
			if (!dirNode.hasProperty(FILE)) {
				continue;
			}
			if (!dirNode.hasProperty(DIR_TAG)) {
				continue;
			}
			RepositoryFile file = new RepositoryFile();
			file.setName(dirNode.getPath().substring(projectPath.length()));
			file.setFullPath(dirNode.getPath());
			buildDirectories(dirNode, fileList, projectPath);
			fileList.add(file);
		}
	}
	
	@Override
	public Repository loadRepository(String project,User user,boolean classify,FileType[] types,String searchFileName) throws Exception{
		String companyId=user.getCompanyId();
		createSecurityConfigFile(user);
		if(project!=null && project.startsWith("/")){
			project=project.substring(1,project.length());
		}
		Repository repo=new Repository();
		List<String> projectNames=new ArrayList<String>();
		repo.setProjectNames(projectNames);
		RepositoryFile rootFile = new RepositoryFile();
		rootFile.setFullPath("/");
		rootFile.setName("项目列表");
		rootFile.setType(Type.root);
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
			String projectName = projectNode.getName();
			if(projectName.indexOf(RESOURCE_SECURITY_CONFIG_FILE)>-1){
				continue;
			}
			if (StringUtils.isNotBlank(project) && !project.equals(projectName)) {
				continue;
			}
			if(!permissionService.projectHasPermission(projectNode.getPath())){
				continue;
			}
			if(StringUtils.isBlank(project)){
				projectNames.add(projectName);
			}
			RepositoryFile projectFile=buildProjectFile(projectNode,types,classify,searchFileName);
			rootFile.addChild(projectFile, false);
		}
		repo.setRootFile(rootFile);
		return repo;
	}
	
	private RepositoryFile buildProjectFile(Node projectNode,FileType[] types,boolean classify,String searchFileName) throws Exception{
		RepositoryFile projectFile = new RepositoryFile();
		projectFile.setType(Type.project);
		projectFile.setName(projectNode.getName());
		projectFile.setFullPath("/" + projectNode.getName());
		RepositoryFile resDir = new RepositoryFile();
		resDir.setFullPath(projectFile.getFullPath());
		resDir.setName("资源");
		if((types==null || types.length==0) && permissionService.projectPackageHasReadPermission(projectNode.getPath())){
			RepositoryFile packageFile = new RepositoryFile();
			packageFile.setName("知识包");
			packageFile.setType(Type.resourcePackage);
			packageFile.setFullPath(projectFile.getFullPath());
			projectFile.addChild(packageFile, false);
		}
		if(classify){
			resDir.setType(Type.resource);
			createResourceCategory(projectNode, resDir,types,searchFileName);			
		}else{
			resDir.setType(Type.all);
			buildResources(projectNode, resDir, types,searchFileName);
		}
		projectFile.addChild(resDir, false);
		return projectFile;
	}
	
	private void buildResources(Node projectNode, RepositoryFile libDir,FileType[] types,String searchFileName) throws Exception{
		FileType[] fileTypes=types;
		if(types==null || types.length==0){
			fileTypes = new FileType[] { FileType.VariableLibrary,
					FileType.ParameterLibrary, FileType.ConstantLibrary,
					FileType.ActionLibrary, FileType.Ruleset,
					FileType.RuleFlow, FileType.DecisionTable,
					FileType.DecisionTree, FileType.ScriptDecisionTable,
					FileType.UL,FileType.Scorecard };			
		}
		libDir.setLibType(LibType.all);
		buildNodes(projectNode.getNodes(), libDir, fileTypes,Type.all,searchFileName);
	}

	private void createResourceCategory(Node projectNode, RepositoryFile libDir,FileType[] types,String searchFileName) throws Exception{
		RepositoryFile subLib = buildLibFile(libDir,"库",LibType.res);
		subLib.setType(Type.lib);
		libDir.addChild(subLib, false);
		FileType[] librarySubTypes = types;
		if(types==null || types.length==0){
			librarySubTypes=new FileType[] { FileType.VariableLibrary, FileType.ParameterLibrary,FileType.ConstantLibrary, FileType.ActionLibrary };
		}
		buildNodes(projectNode.getNodes(), subLib, librarySubTypes,Type.lib,searchFileName);
		
		RepositoryFile rulesLib = buildLibFile(libDir,"决策集",LibType.ruleset);
		rulesLib.setFullPath(libDir.getFullPath());
		rulesLib.setType(Type.ruleLib);
		
		RepositoryFile decisionTableLib = buildLibFile(libDir,"决策表",LibType.decisiontable);
		decisionTableLib.setFullPath(libDir.getFullPath());
		decisionTableLib.setType(Type.decisionTableLib);
		
		RepositoryFile decisionTreeLib = buildLibFile(libDir,"决策树",LibType.decisiontree);
		decisionTreeLib.setFullPath(libDir.getFullPath());
		decisionTreeLib.setType(Type.decisionTreeLib);
		
		RepositoryFile scorecardLib = buildLibFile(libDir,"评分卡",LibType.scorecard);
		scorecardLib.setFullPath(libDir.getFullPath());
		scorecardLib.setType(Type.scorecardLib);
		
		RepositoryFile flowLib = buildLibFile(libDir,"决策流",LibType.ruleflow);
		flowLib.setFullPath(libDir.getFullPath());
		flowLib.setType(Type.flowLib);
		
		libDir.addChild(rulesLib, false);
		libDir.addChild(decisionTableLib, false);
		libDir.addChild(decisionTreeLib, false);
		libDir.addChild(scorecardLib, false);
		libDir.addChild(flowLib, false);
		
		FileType[] libraryRuleTypes = types;
		if(types==null || types.length==0){
			libraryRuleTypes=new FileType[] { FileType.Ruleset, FileType.UL };
		}
		
		FileType[] libraryDecisionTypes = types;
		if(types==null || types.length==0){
			libraryDecisionTypes = new FileType[] { FileType.DecisionTable, FileType.ScriptDecisionTable };
		}
		FileType[] libraryDecisionTreeTypes = types;
		if(types==null || types.length==0){
			libraryDecisionTreeTypes = new FileType[] { FileType.DecisionTree };
		}
		
		FileType[] libraryFlowTypes = types;
		if(types==null || types.length==0){
			libraryFlowTypes = new FileType[] { FileType.RuleFlow };
		}
		
		FileType[] libraryScorecardTypes = types;
		if(types==null || types.length==0){
			libraryScorecardTypes = new FileType[] { FileType.Scorecard };
		}
		
		buildNodes(projectNode.getNodes(), rulesLib, libraryRuleTypes,Type.ruleLib,searchFileName);
		buildNodes(projectNode.getNodes(), decisionTableLib, libraryDecisionTypes,Type.decisionTableLib,searchFileName);
		buildNodes(projectNode.getNodes(), decisionTreeLib, libraryDecisionTreeTypes,Type.decisionTreeLib,searchFileName);
		buildNodes(projectNode.getNodes(), scorecardLib, libraryScorecardTypes,Type.scorecardLib,searchFileName);
		buildNodes(projectNode.getNodes(), flowLib, libraryFlowTypes,Type.flowLib,searchFileName);
	}

	private RepositoryFile buildLibFile(RepositoryFile libraryDir,String name,LibType libType) {
		RepositoryFile subLib = new RepositoryFile();
		subLib.setFullPath(libraryDir.getFullPath());
		subLib.setName(name);
		subLib.setLibType(libType);
		return subLib;
	}

	private void buildNodes(NodeIterator nodeIterator, RepositoryFile parent, FileType[] types,Type folderType,String searchFileName) throws Exception{
		LibType libType=parent.getLibType();
		while (nodeIterator.hasNext()) {
			Node fileNode = nodeIterator.nextNode();
			if (!fileNode.hasProperty(FILE)) {
				continue;
			}
			RepositoryFile file = new RepositoryFile();
			file.setLibType(libType);
			String name = fileNode.getName();
			if (name.toLowerCase().indexOf(RES_PACKGE_FILE) > -1 || name.toLowerCase().indexOf(CLIENT_CONFIG_FILE) > -1 || name.toLowerCase().indexOf(RESOURCE_SECURITY_CONFIG_FILE) > -1) {
				continue;
			}
			if (!fileNode.hasProperty(DIR_TAG)) {
				if(!permissionService.fileHasReadPermission(fileNode.getPath())){
					continue;
				}
				FileType fileType=null;
				boolean add = false;
				for (FileType type : types) {
					if (name.toLowerCase().endsWith(type.toString())) {
						fileType=type;
						add = true;
						break;
					}
				}
				if (!add) {
					continue;
				}
				
				if(libType.equals(LibType.res)){
					if(!fileType.equals(FileType.ActionLibrary) && !fileType.equals(FileType.ParameterLibrary) && !fileType.equals(FileType.ConstantLibrary) && !fileType.equals(FileType.VariableLibrary)) {
						continue;
					}
				}
				
				if(libType.equals(LibType.decisiontable)){
					if(!fileType.equals(FileType.ScriptDecisionTable) && !fileType.equals(FileType.DecisionTable)) {
						continue;
					}
				}
				
				if(libType.equals(LibType.decisiontree)){
					if(!fileType.equals(FileType.DecisionTree)) {
						continue;
					}
				}
				
				if(libType.equals(LibType.ruleflow)){
					if(!fileType.equals(FileType.RuleFlow)) {
						continue;
					}
				}
				
				if(libType.equals(LibType.scorecard)){
					if(!fileType.equals(FileType.Scorecard)) {
						continue;
					}
				}
				
				if(libType.equals(LibType.ruleset)){
					if(!fileType.equals(FileType.Ruleset) && !fileType.equals(FileType.UL)) {
						continue;
					}
				}
				if(StringUtils.isNotBlank(searchFileName)){
					if(name.toLowerCase().indexOf(searchFileName.toLowerCase())==-1){
						continue;
					}
				}
				if (name.toLowerCase().endsWith(FileType.ActionLibrary.toString())) {
					file.setType(Type.action);
				} else if (name.toLowerCase().endsWith(FileType.VariableLibrary.toString())) {
					file.setType(Type.variable);
				} else if (name.toLowerCase().endsWith(FileType.ConstantLibrary.toString())) {
					file.setType(Type.constant);
				} else if (name.toLowerCase().endsWith(FileType.Ruleset.toString())) {
					file.setType(Type.rule);
				} else if (name.toLowerCase().endsWith(FileType.DecisionTable.toString())) {
					file.setType(Type.decisionTable);
				} else if (name.toLowerCase().endsWith(FileType.UL.toString())) {
					file.setType(Type.ul);
				} else if (name.toLowerCase().endsWith(FileType.ParameterLibrary.toString())) {
					file.setType(Type.parameter);
				} else if (name.toLowerCase().endsWith(FileType.RuleFlow.toString())) {
					file.setType(Type.flow);
				} else if (name.toLowerCase().endsWith(FileType.ScriptDecisionTable.toString())) {
					file.setType(Type.scriptDecisionTable);
				} else if (name.toLowerCase().endsWith(FileType.DecisionTree.toString())) {
					file.setType(Type.decisionTree);
				} else if (name.toLowerCase().endsWith(FileType.Scorecard.toString())) {
					file.setType(Type.scorecard);
				}
				file.setFullPath(fileNode.getPath());
				file.setName(name);
				buildNodeLockInfo(fileNode,file);
				parent.addChild(file, false);
				buildNodes(fileNode.getNodes(), file, types,folderType,searchFileName);
			}else{
				file.setFullPath(fileNode.getPath());
				file.setName(name);
				file.setType(Type.folder);
				buildNodeLockInfo(fileNode,file);
				file.setFolderType(folderType);
				parent.addChild(file, true);
				buildNodes(fileNode.getNodes(), file, types,folderType,searchFileName);
			}
		}
	}
	
	private void buildNodeLockInfo(Node node,RepositoryFile file) throws Exception{
		String absPath=node.getPath();
		if(!lockManager.isLocked(absPath)){
			return;
		}
		String owner=lockManager.getLock(absPath).getLockOwner();
		file.setLock(true);
		file.setLockInfo("被"+owner+"锁定");
	}
	
	@Override
	public void lockPath(String path,User user) throws Exception{
		path = processPath(path);
		int pos=path.indexOf(":");
		if(pos!=-1){
			path=path.substring(0,pos);
		}
		Node rootNode=getRootNode();
		if (!rootNode.hasNode(path)) {
			throw new RuleException("File [" + path + "] not exist.");
		}
		Node fileNode = rootNode.getNode(path);
		String topAbsPath=fileNode.getPath();
		if(lockManager.isLocked(topAbsPath)){
			String owner=lockManager.getLock(topAbsPath).getLockOwner();
			throw new NodeLockException("【"+path+"】已被"+owner+"锁定，您不能进行再次锁定!");
		}
		List<Node> nodeList=new ArrayList<Node>();
		unlockAllChildNodes(fileNode, user, nodeList, path);
		for(Node node:nodeList){
			if(!lockManager.isLocked(node.getPath())){
				continue;
			}
			Lock lock=lockManager.getLock(node.getPath());
			lockManager.unlock(lock.getNode().getPath());
		}
		if(!fileNode.isNodeType(NodeType.MIX_LOCKABLE)){
			if (!fileNode.isCheckedOut()) {
				versionManager.checkout(fileNode.getPath());
			}
			fileNode.addMixin("mix:lockable");
			session.save();
		}
		lockManager.lock(topAbsPath, true, true, Long.MAX_VALUE, user.getUsername());				
	}
	
	private void unlockAllChildNodes(Node node,User user,List<Node> nodeList,String rootPath) throws Exception{
		NodeIterator iter=node.getNodes();
		while(iter.hasNext()){
			Node nextNode=iter.nextNode();
			String absPath=nextNode.getPath();
			if(!lockManager.isLocked(absPath)){
				continue;
			}
			Lock lock=lockManager.getLock(absPath);
			String owner=lock.getLockOwner();
			if(!user.getUsername().equals(owner)){
				throw new NodeLockException("当前目录下有子目录被其它人锁定，您不能执行锁定"+rootPath+"目录");
			}
			nodeList.add(nextNode);
			unlockAllChildNodes(nextNode, user, nodeList, rootPath);
		}
	}
	
	@Override
	public void unlockPath(String path,User user) throws Exception{
		path = processPath(path);
		int pos=path.indexOf(":");
		if(pos!=-1){
			path=path.substring(0,pos);
		}
		Node rootNode=getRootNode();
		if (!rootNode.hasNode(path)) {
			throw new RuleException("File [" + path + "] not exist.");
		}
		Node fileNode = rootNode.getNode(path);
		String absPath=fileNode.getPath();
		if(!lockManager.isLocked(absPath)){
			throw new NodeLockException("当前文件未锁定，不需要解锁!");
		}
		Lock lock=lockManager.getLock(absPath);
		String owner=lock.getLockOwner();
		if(!owner.equals(user.getUsername())){
			throw new NodeLockException("当前文件由【"+owner+"】锁定，您无权解锁!");
		}
		lockManager.unlock(lock.getNode().getPath());
	}

	public void deleteFile(String path,User user) throws Exception{
		if(!permissionService.fileHasWritePermission(path)){
			throw new NoPermissionException();
		}
		repositoryInteceptor.deleteFile(path);
		path = processPath(path);
		Node rootNode=getRootNode();
		if (!rootNode.hasNode(path)) {
			throw new RuleException("File [" + path + "] not exist.");
		}
		String[] subpaths = path.split("/");
		Node fileNode = rootNode;
		for (String subpath : subpaths) {
			if (StringUtils.isEmpty(subpath)) {
				continue;
			}
			String subDirs[] = subpath.split("\\.");
			for (String dir : subDirs) {
				if (StringUtils.isEmpty(dir)) {
					continue;
				}
				if (!fileNode.hasNode(dir)) {
					continue;
				}
				fileNode = fileNode.getNode(dir);
				lockCheck(fileNode,user);
				if (!fileNode.isCheckedOut()) {
					versionManager.checkout(fileNode.getPath());
				}
			}
		}
		fileNode = rootNode.getNode(path);
		lockCheck(fileNode,user);
		if (!fileNode.isCheckedOut()) {
			versionManager.checkout(fileNode.getPath());
		}
		fileNode.remove();
		session.save();
	}

	@Override
	public void saveFile(String path, String content,boolean newVersion,String versionComment,User user) throws Exception{
		path=Utils.decodeURL(path); 
		if(path.indexOf(RES_PACKGE_FILE)>-1){
			if(!permissionService.projectPackageHasWritePermission(path)){
				throw new NoPermissionException();
			}
		}
		if(!permissionService.fileHasWritePermission(path)){
			throw new NoPermissionException();
		}
		
		repositoryInteceptor.saveFile(path, content);
		path = processPath(path);
		int pos=path.indexOf(":");
		if(pos!=-1){
			path=path.substring(0,pos);
		}
		Node rootNode=getRootNode();
		if (!rootNode.hasNode(path)) {
			throw new RuleException("File [" + path + "] not exist.");
		}
		Node fileNode = rootNode.getNode(path);
		lockCheck(fileNode,user);
		versionManager.checkout(fileNode.getPath());
		Binary fileBinary = new BinaryImpl(content.getBytes("utf-8"));
		fileNode.setProperty(DATA, fileBinary);
		fileNode.setProperty(FILE, true);
		fileNode.setProperty(CRATE_USER, user.getUsername());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		DateValue dateValue = new DateValue(calendar);
		fileNode.setProperty(CRATE_DATE, dateValue);
		if (newVersion && StringUtils.isNotBlank(versionComment)) {
			fileNode.setProperty(VERSION_COMMENT, versionComment);
		}
		session.save();
		if (newVersion) {
			versionManager.checkin(fileNode.getPath());
		}
	}
	
	@Override
	public List<String> getReferenceFiles(String path,String searchText) throws Exception{
		Node rootNode=getRootNode();
		List<String> referenceFiles=new ArrayList<String>();
		List<String> files=getFiles(rootNode, path);
		for(String nodePath:files){
			InputStream inputStream=readFile(nodePath,null);
			try {
				String content = IOUtils.toString(inputStream,"UTF-8");
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
	
	private List<String> getFiles(Node rootNode,String path) throws Exception{
		String project=getProject(path);
		List<String> list=new ArrayList<String>();
		Node projectNode=rootNode.getNode(project);		
		buildPath(list, projectNode);
		return list;
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
	
	@Override
	public boolean fileExistCheck(String filePath) throws Exception{
		Node rootNode=getRootNode();
		filePath=processPath(filePath);
		if(filePath.contains(" ") || filePath.equals("")){
			return true;
		}
		if(rootNode.hasNode(filePath)){
			return true;
		}
		return false;
	}
	
	@Override
	public RepositoryFile createProject(String projectName, User user,boolean classify) throws Exception{
		if(!permissionService.isAdmin()){
			throw new NoPermissionException();
		}
		repositoryInteceptor.createProject(projectName);
		Node rootNode=getRootNode();
		if(rootNode.hasNode(projectName)){
			throw new RuleException("Project ["+projectName+"] already exist.");
		}
		Node projectNode=rootNode.addNode(projectName);
		projectNode.addMixin("mix:versionable");
		projectNode.setProperty(FILE, true);
		projectNode.setProperty(CRATE_USER,user.getUsername());
		projectNode.setProperty(COMPANY_ID, user.getCompanyId());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		DateValue dateValue = new DateValue(calendar);
		projectNode.setProperty(CRATE_DATE, dateValue);
		session.save();
		createResourcePackageFile(projectName,user);
		createClientConfigFile(projectName, user);
		RepositoryFile projectFileInfo=buildProjectFile(projectNode, null ,classify,null);
		return projectFileInfo;
	}
	
	public void createDir(String path,User user) throws Exception{
		if(!permissionService.isAdmin()){
			throw new NoPermissionException();
		}
		repositoryInteceptor.createDir(path);
		Node rootNode=getRootNode();
		path = processPath(path);
		if (rootNode.hasNode(path)) {
			throw new RuleException("Dir [" + path + "] already exist.");
		}
		boolean add = false;
		String[] subpaths = path.split("/");
		Node parentNode = rootNode;
		for (String subpath : subpaths) {
			if (StringUtils.isEmpty(subpath)) {
				continue;
			}
			String subDirs[] = subpath.split("\\.");
			for (String dir : subDirs) {
				if (StringUtils.isEmpty(dir)) {
					continue;
				}
				if (parentNode.hasNode(dir)) {
					parentNode = parentNode.getNode(dir);
				} else {
					parentNode = parentNode.addNode(dir);
					parentNode.addMixin("mix:versionable");
					parentNode.addMixin("mix:lockable");
					parentNode.setProperty(DIR_TAG, true);
					parentNode.setProperty(FILE, true);
					parentNode.setProperty(CRATE_USER,user.getUsername());
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					DateValue dateValue = new DateValue(calendar);
					parentNode.setProperty(CRATE_DATE, dateValue);
					add = true;
				}
			}
		}
		if (add) {
			session.save();
		}
	}
	
	@Override
	public void createFile(String path, String content,User user) throws Exception{
		if(!permissionService.isAdmin()){
			throw new NoPermissionException();
		}
		createFileNode(path, content, user, true);
	}
	
	public void fileRename(String path, String newPath) throws Exception{
		if(!permissionService.isAdmin()){
			throw new NoPermissionException();
		}
		
		repositoryInteceptor.renameFile(path, newPath);
		path = processPath(path);
		newPath = processPath(newPath);
		Node rootNode=getRootNode();
		if (!rootNode.hasNode(path)) {
			throw new RuleException("File [" + path + "] not exist.");
		}
		session.getWorkspace().move("/" + path, "/" + newPath);
		session.save();
	}
	
	public void exportXml(String projectPath, OutputStream outputStream) throws Exception{
		if(!permissionService.isAdmin()){
			throw new NoPermissionException();
		}
		session.exportSystemView(projectPath, outputStream, false, false);
	}

	public void importXml(InputStream inputStream,boolean overwrite) throws Exception{
		if(!permissionService.isAdmin()){
			throw new NoPermissionException();
		}
		String rootNodePath=getRootNode().getPath();
		if(overwrite){
			session.importXML(rootNodePath, inputStream,ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING);				
		}else{
			session.importXML(rootNodePath, inputStream,ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);				
		}
		session.save();
	}
	
	private void createResourcePackageFile(String project,User user) throws Exception{
		String filePath = processPath(project) + "/" + RES_PACKGE_FILE;
		Node rootNode=getRootNode();
		if (!rootNode.hasNode(filePath)) {
			createFile(filePath, "<?xml version=\"1.0\" encoding=\"utf-8\"?><res-packages></res-packages>",user);
		}
	}
	
	private void createClientConfigFile(String project,User user) throws Exception{
		Node rootNode=getRootNode();
		String filePath = processPath(project) + "/" + CLIENT_CONFIG_FILE;
		if (!rootNode.hasNode(filePath)) {
			createFile(filePath, "<?xml version=\"1.0\" encoding=\"utf-8\"?><client-config></client-config>",user);
		}
	}
	
	private void createSecurityConfigFile(User user) throws Exception{
		String companyId=user.getCompanyId();
		String filePath=RESOURCE_SECURITY_CONFIG_FILE+(companyId == null ? "" : companyId);
		Node rootNode=getRootNode();
		if (!rootNode.hasNode(filePath)) {
			createFileNode(filePath, "<?xml version=\"1.0\" encoding=\"utf-8\"?><user-permission></user-permission>",user,false);
		}
	}
	
	private void createFileNode(String path, String content,User user,boolean isFile) throws Exception{
		String createUser=user.getUsername();
		repositoryInteceptor.createFile(path,content);
		Node rootNode=getRootNode();
		path = processPath(path);
		try {
			if (rootNode.hasNode(path)) {
				throw new RuleException("File [" + path + "] already exist.");
			}
			Node fileNode = rootNode.addNode(path);
			fileNode.addMixin("mix:versionable");
			fileNode.addMixin("mix:lockable");
			Binary fileBinary = new BinaryImpl(content.getBytes());
			fileNode.setProperty(DATA, fileBinary);
			if(isFile){
				fileNode.setProperty(FILE, true);				
			}
			fileNode.setProperty(CRATE_USER, createUser);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			DateValue dateValue = new DateValue(calendar);
			fileNode.setProperty(CRATE_DATE, dateValue);
			session.save();
		} catch (Exception ex) {
			throw new RuleException(ex);
		}
	}

	private void lockCheck(Node node,User user) throws Exception{
		if(lockManager.isLocked(node.getPath())){
			String lockOwner=lockManager.getLock(node.getPath()).getLockOwner();
			if(lockOwner.equals(user.getUsername())){
				return;
			}
			throw new NodeLockException("【"+node.getName()+"】已被"+lockOwner+"锁定!");
		}
	}
	
	public void setPermissionService(PermissionService permissionService) {
		this.permissionService = permissionService;
	}
}
