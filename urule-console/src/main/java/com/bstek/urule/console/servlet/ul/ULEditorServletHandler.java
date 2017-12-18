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

import com.bstek.urule.RuleException;
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
		try{
			if(StringUtils.isEmpty(version)){
				inputStream=repositoryService.readFile(file,null);
			}else{
				inputStream=repositoryService.readFile(file,version);			
			}
			IOUtils.copy(inputStream, outputStream);
		}catch(Exception ex){
			throw new RuleException(ex);
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
