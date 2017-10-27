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
		List<RepositoryFile> projects=repositoryService.loadProject(companyId);
		for(UserPermission p:result){
			buildProjectConfigs(projects, p);
		}
		writeObjectToJson(resp, result);
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
		String path=RepositoryServiceImpl.RESOURCE_SECURITY_CONFIG_FILE+(companyId==null ? "" : companyId);
		repositoryService.saveFile(path, content, user.getUsername(), false,null);
		permissionStore.refreshPermissionStore();
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
