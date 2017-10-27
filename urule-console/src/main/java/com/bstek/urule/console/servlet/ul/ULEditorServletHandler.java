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
package com.bstek.urule.console.servlet.ul;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.bstek.urule.Utils;
import com.bstek.urule.builder.ResourceLibraryBuilder;
import com.bstek.urule.console.repository.RepositoryService;
import com.bstek.urule.console.servlet.RenderPageServletHandler;
import com.bstek.urule.dsl.DSLRuleSetBuilder;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.rule.RuleSet;

/**
 * @author Jacky.gao
 * @since 2016年8月1日
 */
public class ULEditorServletHandler extends RenderPageServletHandler{
	private DSLRuleSetBuilder dslRuleSetBuilder;
	private ResourceLibraryBuilder resourceLibraryBuilder;
	private RepositoryService repositoryService;
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method=retriveMethod(req);
		if(method!=null){
			invokeMethod(method, req, resp);
		}else{
			VelocityContext context = new VelocityContext();
			context.put("contextPath", req.getContextPath());
			String file=req.getParameter("file");
			String project = buildProjectNameFromFile(file);
			if(project!=null){
				context.put("project", project);
			}
			resp.setContentType("text/html");
			resp.setCharacterEncoding("utf-8");
			Template template=ve.getTemplate("html/ul-editor.html","utf-8");
			PrintWriter writer=resp.getWriter();
			template.merge(context, writer);
			writer.close();
		}
	}
	public void loadUL(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String file=req.getParameter("file");
		file=Utils.decodeURL(file);
		OutputStream outputStream=resp.getOutputStream();
		String version=req.getParameter("version");
		InputStream inputStream=null;
		if(StringUtils.isEmpty(version)){
			inputStream=repositoryService.readFile(file,null);
		}else{
			inputStream=repositoryService.readFile(file,version);			
		}
		try{
			IOUtils.copy(inputStream, outputStream);
		}finally{
			outputStream.close();
			inputStream.close();
		}
	}
	public void loadULLibs(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String content = req.getParameter("content");
		RuleSet ruleSet=dslRuleSetBuilder.build(content);
		ResourceLibrary library=resourceLibraryBuilder.buildResourceLibrary(ruleSet.getLibraries());
		writeObjectToJson(resp, library);
	}
	
	public void setDslRuleSetBuilder(DSLRuleSetBuilder dslRuleSetBuilder) {
		this.dslRuleSetBuilder = dslRuleSetBuilder;
	}
	public void setResourceLibraryBuilder(ResourceLibraryBuilder resourceLibraryBuilder) {
		this.resourceLibraryBuilder = resourceLibraryBuilder;
	}
	
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	@Override
	public String url() {
		return "/uleditor";
	}
	
}
