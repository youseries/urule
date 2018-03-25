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
package com.bstek.urule.console.servlet.permission;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.bstek.urule.RuleException;
import com.bstek.urule.Utils;
import com.bstek.urule.console.EnvironmentUtils;
import com.bstek.urule.console.User;
import com.bstek.urule.console.exception.NoPermissionException;
import com.bstek.urule.console.repository.RepositoryService;
import com.bstek.urule.console.repository.RepositoryServiceImpl;
import com.bstek.urule.console.repository.model.RepositoryFile;
import com.bstek.urule.console.repository.permission.PermissionService;
import com.bstek.urule.console.repository.permission.PermissionStore;
import com.bstek.urule.console.servlet.RenderPageServletHandler;
import com.bstek.urule.console.servlet.RequestContext;

/**
 * @author Jacky.gao
 * @since 2016年8月30日
 */
public class PermissionConfigServletHandler extends RenderPageServletHandler{
	private RepositoryService repositoryService;
	private PermissionStore permissionStore;
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(!((PermissionService)permissionStore).isAdmin()){
			throw new NoPermissionException();
		}
		String method=retriveMethod(req);
		if(method!=null){
			invokeMethod(method, req, resp);
		}else{
			VelocityContext context = new VelocityContext();
			context.put("contextPath", req.getContextPath());
			resp.setContentType("text/html");
			resp.setCharacterEncoding("utf-8");
			Template template=ve.getTemplate("html/permission-config-editor.html","utf-8");
			PrintWriter writer=resp.getWriter();
			template.merge(context, writer);
			writer.close();
		}
	}
	
	public void loadResourceSecurityConfigs(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String companyId=EnvironmentUtils.getLoginUser(new RequestContext(req, resp)).getCompanyId();
		try{
			List<UserPermission> permissions=repositoryService.loadResourceSecurityConfigs(companyId);
			List<User> users=EnvironmentUtils.getEnvironmentProvider().getUsers();
			if(users==null)users=new ArrayList<User>();
			List<UserPermission> result=new ArrayList<UserPermission>();
			for(User user:users){
				if(user.isAdmin()){
					continue;
				}
				if(companyId!=null){
					if(user.getCompanyId()==null){
						continue;
					}
					if(!user.getCompanyId().equals(companyId)){
						continue;
					}
				}
				boolean exist=false;
				for(UserPermission p:permissions){
					if(p.getUsername().equals(user.getUsername())){
						exist=true;
						break;
					}
				}
				if(exist){
					continue;
				}
				UserPermission up=new UserPermission();
				up.setProjectConfigs(new ArrayList<ProjectConfig>());
				up.setUsername(user.getUsername());
				result.add(up);
			}
			result.addAll(permissions);
			List<RepositoryFile> projects=repositoryService.loadProjects(companyId);
			for(UserPermission p:result){
				buildProjectConfigs(projects, p);
			}
			writeObjectToJson(resp, result);
		}catch(Exception ex){
			throw new RuleException(ex);
		}
	}
	
	private void buildProjectConfigs(List<RepositoryFile> projects,UserPermission p){
		List<ProjectConfig> configs=p.getProjectConfigs();
		if(configs==null){
			configs=new ArrayList<ProjectConfig>();
			p.setProjectConfigs(configs);
		}
		for(RepositoryFile project:projects){
			boolean exist=false;
			for(ProjectConfig c:p.getProjectConfigs()){
				if(project.getName().equals(c.getProject())){
					exist=true;
					break;
				}
			}
			if(exist){
				continue;
			}
			ProjectConfig config=new ProjectConfig();
			config.setProject(project.getName());
			configs.add(config);
		}
	}
	
	public void saveResourceSecurityConfigs(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user=EnvironmentUtils.getLoginUser(new RequestContext(req, resp));
		String companyId=user.getCompanyId();
		String content=req.getParameter("content");
		content=Utils.decodeURL(content);
		String path=RepositoryServiceImpl.RESOURCE_SECURITY_CONFIG_FILE+(companyId==null ? "" : companyId);
		try{
			repositoryService.saveFile(path, content, false,null,user);
			permissionStore.refreshPermissionStore();			
		}catch(Exception ex){
			throw new RuleException(ex);
		}
	}
	
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	public void setPermissionStore(PermissionStore permissionStore) {
		this.permissionStore = permissionStore;
	}
	
	@Override
	public String url() {
		return "/permission";
	}
}
