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
package com.bstek.urule.console.servlet.frame;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.bstek.urule.RuleException;
import com.bstek.urule.Utils;
import com.bstek.urule.console.EnvironmentUtils;
import com.bstek.urule.console.User;
import com.bstek.urule.console.repository.Repository;
import com.bstek.urule.console.repository.RepositoryService;
import com.bstek.urule.console.repository.model.FileType;
import com.bstek.urule.console.repository.model.RepositoryFile;
import com.bstek.urule.console.repository.model.Type;
import com.bstek.urule.console.repository.model.VersionFile;
import com.bstek.urule.console.servlet.RenderPageServletHandler;
import com.bstek.urule.console.servlet.RequestContext;

/**
 * @author Jacky.gao
 * @since 2016年6月3日
 */
public class FrameServletHandler extends RenderPageServletHandler{
	private RepositoryService repositoryService;
	private String welcomePage;
	private String title;
	private static final String CLASSIFY_COOKIE_NAME="_lib_classify";
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method=retriveMethod(req);
		if(method!=null){
			invokeMethod(method, req, resp);
		}else{
			VelocityContext context = new VelocityContext();
			context.put("contextPath", req.getContextPath());
			context.put("welcomePage", welcomePage);
			context.put("title", title);
			resp.setContentType("text/html");
			resp.setCharacterEncoding("utf-8");
			Template template=ve.getTemplate("html/frame.html","utf-8");
			PrintWriter writer=resp.getWriter();
			template.merge(context, writer);
			writer.close();
		}
	}
	
	public void fileVersions(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String path=req.getParameter("path");
		path=Utils.decodeURL(path);
		List<VersionFile> files=repositoryService.getVersionFiles(path);
		writeObjectToJson(resp, files);
	}
	
	public void fileSource(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String path=req.getParameter("path");
		path=Utils.decodeURL(path);
		InputStream inputStream=repositoryService.readFile(path,null);
		String content=IOUtils.toString(inputStream,"utf-8");
		inputStream.close();
		String xml=null;
		try{
			Document doc=DocumentHelper.parseText(content);
			OutputFormat format=OutputFormat.createPrettyPrint();
			StringWriter out=new StringWriter();
			XMLWriter writer=new XMLWriter(out, format);
			writer.write(doc);
			xml=out.toString();
		}catch(Exception ex){
			xml=content;
		}
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("content", xml);
		writeObjectToJson(resp, result);
	}
	
	public void importProject(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletContext servletContext = req.getSession().getServletContext();
		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);
		ServletFileUpload upload = new ServletFileUpload(factory);
		InputStream inputStream=null;
		boolean overwriteProject=true;
		List<FileItem> items = upload.parseRequest(req);
		if(items.size()==0){
			throw new ServletException("Upload file is invalid.");
		}
		for(FileItem item:items){
			String name=item.getFieldName();
			if(name.equals("overwriteProject")){
				String overwriteProjectStr=new String(item.get());
				overwriteProject=Boolean.valueOf(overwriteProjectStr);
			}else if(name.equals("file")){
				inputStream=item.getInputStream();
			}
		}
		repositoryService.importXml(inputStream,overwriteProject);
		IOUtils.closeQuietly(inputStream);
		resp.sendRedirect(req.getContextPath()+"/urule/frame");
	}
	
	public void loadFileVersions(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String file=req.getParameter("file");
		file=Utils.decodeURL(file);
		List<VersionFile> versions=repositoryService.getVersionFiles(file);
		writeObjectToJson(resp, versions);
	}
	public void createFolder(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String fullFolderName=req.getParameter("fullFolderName");
		fullFolderName=Utils.decodeURL(fullFolderName);
		User user=EnvironmentUtils.getLoginUser(new RequestContext(req,resp));
		repositoryService.createDir(fullFolderName, user);
		loadProjects(req, resp);
	}
	public void copyFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String newFullPath=req.getParameter("newFullPath");
		String oldFullPath=req.getParameter("oldFullPath");
		newFullPath=Utils.decodeURL(newFullPath);
		oldFullPath=Utils.decodeURL(oldFullPath);
		try{
			InputStream inputStream=repositoryService.readFile(oldFullPath, null);
			String content=IOUtils.toString(inputStream, "utf-8");
			inputStream.close();
			User user=EnvironmentUtils.getLoginUser(new RequestContext(req,resp));
			repositoryService.createFile(newFullPath, content,user);
			loadProjects(req, resp);
		}catch(Exception ex){
			throw new RuleException(ex);
		}
	}
	
	public void createFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path=req.getParameter("path");
		path=Utils.decodeURL(path);
		String type=req.getParameter("type");
		FileType fileType=FileType.parse(type);
		StringBuilder content=new StringBuilder();
		if(fileType.equals(FileType.UL)) {
			content.append("rule \"rule01\"");
			content.append("\n");
			content.append("if");
			content.append("\r\n");
			content.append("then");
			content.append("\r\n");
			content.append("end");
		}else if(fileType.equals(FileType.DecisionTable)) {
			content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			content.append("<decision-table>");
			content.append("<cell row=\"0\" col=\"2\" rowspan=\"1\"></cell>");
			content.append("<cell row=\"0\" col=\"1\" rowspan=\"1\">");
			content.append("<joint type=\"and\"/>");
			content.append("</cell>");
			content.append("<cell row=\"0\" col=\"0\" rowspan=\"1\">");
			content.append("<joint type=\"and\"/>");
			content.append("</cell>");
			content.append("<cell row=\"1\" col=\"2\" rowspan=\"1\">");
			content.append("</cell>");
			content.append("<cell row=\"1\" col=\"1\" rowspan=\"1\">");
			content.append("<joint type=\"and\"/>");
			content.append("</cell>");
			content.append("<cell row=\"1\" col=\"0\" rowspan=\"1\">");
			content.append("<joint type=\"and\"/>");
			content.append("</cell>");
			content.append("<row num=\"0\" height=\"40\"/>");
			content.append("<row num=\"1\" height=\"40\"/>");
			content.append("<col num=\"0\" width=\"120\" type=\"Criteria\"/>");
			content.append("<col num=\"1\" width=\"120\" type=\"Criteria\"/>");
			content.append("<col num=\"2\" width=\"200\" type=\"Assignment\"/>");
			content.append("</decision-table>");
		}else if(fileType.equals(FileType.DecisionTree)){
			content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			content.append("<decision-tree>");
			content.append("<variable-tree-node></variable-tree-node>");
			content.append("</decision-tree>");
		}else if(fileType.equals(FileType.ScriptDecisionTable)) {
			content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			content.append("<script-decision-table>");
			content.append("<script-cell row=\"0\" col=\"2\" rowspan=\"1\"></script-cell>");
			content.append("<script-cell row=\"0\" col=\"1\" rowspan=\"1\"></script-cell>");
			content.append("<script-cell row=\"0\" col=\"0\" rowspan=\"1\"></script-cell>");
			content.append("<script-cell row=\"1\" col=\"2\" rowspan=\"1\"></script-cell>");
			content.append("<script-cell row=\"1\" col=\"1\" rowspan=\"1\"></script-cell>");
			content.append("<script-cell row=\"1\" col=\"0\" rowspan=\"1\"></script-cell>");
			content.append("<row num=\"0\" height=\"40\"/>");
			content.append("<row num=\"1\" height=\"40\"/>");
			content.append("<col num=\"0\" width=\"120\" type=\"Criteria\"/>");
			content.append("<col num=\"1\" width=\"120\" type=\"Criteria\"/>");
			content.append("<col num=\"2\" width=\"200\" type=\"Assignment\"/>");
			content.append("</script-decision-table>");
		}else if(fileType.equals(FileType.Scorecard)){
			content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			content.append("<scorecard scoring-type=\"sum\" assign-target-type=\"none\">");
			content.append("</scorecard>");
		}else{
			String name = getRootTagName(fileType);
			content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			content.append("<" + name + ">");
			content.append("</" + name + ">");
		}
		User user=EnvironmentUtils.getLoginUser(new RequestContext(req,resp));
		try{
			repositoryService.createFile(path, content.toString(),user);			
		}catch(Exception ex){
			throw new RuleException(ex);
		}
		RepositoryFile newFileInfo=new RepositoryFile();
		newFileInfo.setFullPath(path);
		if(fileType.equals(FileType.VariableLibrary)){
			newFileInfo.setType(Type.variable);			
		}else if(fileType.equals(FileType.ActionLibrary)){
			newFileInfo.setType(Type.action);			
		}else if(fileType.equals(FileType.ConstantLibrary)){
			newFileInfo.setType(Type.constant);			
		}else if(fileType.equals(FileType.ParameterLibrary)){
			newFileInfo.setType(Type.parameter);			
		}else if(fileType.equals(FileType.DecisionTable)){
			newFileInfo.setType(Type.decisionTable);			
		}else if(fileType.equals(FileType.ScriptDecisionTable)){
			newFileInfo.setType(Type.scriptDecisionTable);			
		}else if(fileType.equals(FileType.Ruleset)){
			newFileInfo.setType(Type.rule);			
		}else if(fileType.equals(FileType.UL)){
			newFileInfo.setType(Type.ul);			
		}else if(fileType.equals(FileType.DecisionTree)){
			newFileInfo.setType(Type.decisionTree);			
		}else if(fileType.equals(FileType.RuleFlow)){
			newFileInfo.setType(Type.flow);			
		}else if(fileType.equals(FileType.Scorecard)){
			newFileInfo.setType(Type.scorecard);			
		}
		writeObjectToJson(resp, newFileInfo);
	}
	
	private String getRootTagName(FileType type) {
		String root = null;
		switch(type){
		case ActionLibrary:
			root="action-library";
			break;
		case ConstantLibrary:
			root="constant-library";
			break;
		case DecisionTable:
			root="decision-table";
			break;
		case DecisionTree:
			root="decision-tree";
			break;
		case ParameterLibrary:
			root="parameter-library";
			break;
		case RuleFlow:
			root="rule-flow";
			break;
		case Ruleset:
			root="rule-set";
			break;
		case ScriptDecisionTable:
			root="script-decision-table";
			break;
		case VariableLibrary:
			root="variable-library";
			break;
		case UL:
			root="script";
			break;
		case Scorecard:
			root="scorecard";
			break;
		case DIR:
			throw new IllegalArgumentException("Unsupport filetype : "+type);
		}
		return root;
	}
	
	public void projectExistCheck(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String projectName=req.getParameter("newProjectName");
		if(StringUtils.isEmpty(projectName)){
			return;
		}
		projectName=Utils.decodeURL(projectName);
		projectName=projectName.trim();
		Map<String,Object> result=new HashMap<String,Object>();
		try{
			result.put("valid", !repositoryService.fileExistCheck(projectName));
			writeObjectToJson(resp, result);
		}catch(Exception ex){
			throw new RuleException(ex);
		}
	}
	public void fileExistCheck(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fullFileName=req.getParameter("fullFileName");
		if(StringUtils.isEmpty(fullFileName)){
			return;
		}
		fullFileName=Utils.decodeURL(fullFileName);
		fullFileName=fullFileName.trim();
		Map<String,Object> result=new HashMap<String,Object>();
		try{
			result.put("valid", !repositoryService.fileExistCheck(fullFileName));
			writeObjectToJson(resp, result);			
		}catch(Exception ex){
			throw new RuleException(ex);
		}
	}
	
	public void deleteFile(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String path=req.getParameter("path");
		path=Utils.decodeURL(path);
		User user=EnvironmentUtils.getLoginUser(new RequestContext(req, resp));
		repositoryService.deleteFile(path,user);
		String isFolder=req.getParameter("isFolder");
		if(StringUtils.isNotBlank(isFolder) && isFolder.equals("true")){
			loadProjects(req, resp);
		}
	}
	
	public void lockFile(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String file=req.getParameter("file");
		User user=EnvironmentUtils.getLoginUser(new RequestContext(req, resp));
		repositoryService.lockPath(file, user);
		loadProjects(req, resp);
	}
	public void unlockFile(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String file=req.getParameter("file");
		User user=EnvironmentUtils.getLoginUser(new RequestContext(req, resp));
		repositoryService.unlockPath(file, user);
		loadProjects(req, resp);
	}
	
	public void exportProjectBackupFile(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String path=req.getParameter("path");
		String projectPath=Utils.decodeURL(path);
		if(StringUtils.isEmpty(projectPath)){
			throw new RuleException("Export project not be null.");
		}
		SimpleDateFormat sd=new SimpleDateFormat("yyyyMMddHHmmss");
		String projectName=projectPath.substring(1,projectPath.length());
		String filename=projectName+"-urule-repo-"+sd.format(new Date())+".bak";
		resp.setContentType("application/octet-stream");
		resp.setHeader("Content-Disposition", "attachment; filename=\"" + new String(filename.getBytes("utf-8"),"iso-8859-1") + "\"");
		resp.setHeader("content-type", "application/octet-stream");
		OutputStream outputStream=resp.getOutputStream();
		repositoryService.exportXml(projectPath,outputStream);
		outputStream.flush();
		outputStream.close();
	}
	
	public void createProject(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String projectName=req.getParameter("newProjectName");
		projectName=Utils.decodeURL(projectName);
		boolean classify = getClassify(req,resp);
		User user=EnvironmentUtils.getLoginUser(new RequestContext(req,resp));
		RepositoryFile projectFileInfo=repositoryService.createProject(projectName,user,classify);
		writeObjectToJson(resp, projectFileInfo);
	}

	
	public void loadProjects(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		User user=EnvironmentUtils.getLoginUser(new RequestContext(req,resp));
		boolean classify = getClassify(req,resp);
		String projectName=req.getParameter("projectName");
		String searchFileName=req.getParameter("searchFileName");
		projectName=Utils.decodeURL(projectName);
		String typesStr=req.getParameter("types");
		FileType[] types=null;
		if(StringUtils.isNotBlank(typesStr) && !typesStr.equals("all")){
			if(typesStr.equals("lib")){
				types=new FileType[]{FileType.VariableLibrary,FileType.ConstantLibrary,FileType.ParameterLibrary,FileType.ActionLibrary};
			}else if(typesStr.equals("rule")){
				types=new FileType[]{FileType.Ruleset,FileType.UL};
			}else if(typesStr.equals("table")){
				types=new FileType[]{FileType.DecisionTable,FileType.ScriptDecisionTable};
			}else if(typesStr.equals("tree")){
				types=new FileType[]{FileType.DecisionTree};
			}else if(typesStr.equals("flow")){
				types=new FileType[]{FileType.RuleFlow};
			}
		}
		Repository repo=repositoryService.loadRepository(projectName, user,classify,types,searchFileName);			
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("repo", repo);
		map.put("classify", classify);
		writeObjectToJson(resp, map);
	}

	private boolean getClassify(HttpServletRequest req,HttpServletResponse resp) {
		String classifyValue=req.getParameter("classify");
		if(StringUtils.isBlank(classifyValue)){
			Cookie[] cookies=req.getCookies();
			if(cookies!=null){				
				for(Cookie cookie:cookies){
					if(CLASSIFY_COOKIE_NAME.equals(cookie.getName())){
						classifyValue=cookie.getValue();
						break;
					}
				}
			}
		}else{
			Cookie classifyCookie=new Cookie(CLASSIFY_COOKIE_NAME,classifyValue);
			classifyCookie.setMaxAge(2100000000);
			resp.addCookie(classifyCookie);
		}
		boolean classify=true;
		if(StringUtils.isNotBlank(classifyValue)){
			classify=Boolean.valueOf(classifyValue);
		}
		return classify;
	}
	
	public void fileRename(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String path=req.getParameter("path");
		path=Utils.decodeURL(path);
		String newPath=req.getParameter("newPath");
		newPath=Utils.decodeURL(newPath);
		repositoryService.fileRename(path, newPath);
		loadProjects(req, resp);			
	}
	
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	public void setWelcomePage(String welcomePage) {
		this.welcomePage = welcomePage;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String url() {
		return "/frame";
	}
}
