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
package com.bstek.urule.console.servlet.client;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.bstek.urule.Utils;
import com.bstek.urule.console.EnvironmentUtils;
import com.bstek.urule.console.repository.RepositoryService;
import com.bstek.urule.console.repository.RepositoryServiceImpl;
import com.bstek.urule.console.servlet.RenderPageServletHandler;
import com.bstek.urule.console.servlet.RequestContext;

/**
 * @author Jacky.gao
 * @since 2016年8月11日
 */
public class ClientConfigServletHandler extends RenderPageServletHandler{
	private RepositoryService repositoryService;
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method=retriveMethod(req);
		if(method!=null){
			invokeMethod(method, req, resp);
		}else{
			VelocityContext context = new VelocityContext();
			context.put("contextPath", req.getContextPath());
			resp.setContentType("text/html");
			resp.setCharacterEncoding("utf-8");
			Template template=ve.getTemplate("html/client-config-editor.html","utf-8");
			PrintWriter writer=resp.getWriter();
			template.merge(context, writer);
			writer.close();
		}
	}
	public void loadData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String project=req.getParameter("project");
		project=Utils.decodeURL(project);
		writeObjectToJson(resp, repositoryService.loadClientConfigs(project));
	}
	public void save(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String project=req.getParameter("project");
		project=Utils.decodeURL(project);
		String file=project+"/"+RepositoryServiceImpl.CLIENT_CONFIG_FILE;
		String content=req.getParameter("content");
		String username=EnvironmentUtils.getLoginUser(new RequestContext(req, resp)).getUsername();
		repositoryService.saveFile(file, content, username, false,null);
	}
	
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	@Override
	public String url() {
		return "/clientconfig";
	}
	
}
