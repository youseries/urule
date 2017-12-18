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
package com.bstek.urule.console.servlet.flow;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.bstek.urule.RuleException;
import com.bstek.urule.Utils;
import com.bstek.urule.console.repository.RepositoryService;
import com.bstek.urule.console.repository.model.ResourcePackage;
import com.bstek.urule.console.servlet.RenderPageServletHandler;
import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.parse.deserializer.FlowDeserializer;

/**
 * @author Jacky.gao
 * @since 2016年6月3日
 */
public class RuleFlowDesignerServletHandler extends RenderPageServletHandler {
	private RepositoryService repositoryService;
	private FlowDeserializer flowDeserializer;
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
			Template template=ve.getTemplate("html/rule-flow-designer.html","utf-8");
			PrintWriter writer=resp.getWriter();
			template.merge(context, writer);
			writer.close();
		}
	}

	public void loadFlowDefinition(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		InputStream inputStream;
		String file=req.getParameter("file");
		String version=req.getParameter("version");
		file=Utils.decodeURL(file);
		try{
			if(StringUtils.isEmpty(version)){
				inputStream=repositoryService.readFile(file,null);
			}else{
				inputStream=repositoryService.readFile(file,version);
			}
			Element root=parseXml(inputStream);
			FlowDefinition fd = flowDeserializer.deserialize(root);
			inputStream.close();
			writeObjectToJson(resp, new FlowDefinitionWrapper(fd));
		}catch(Exception ex){
			throw new RuleException(ex);
		}
	}
	
	protected Element parseXml(InputStream stream){
		SAXReader reader=new SAXReader();
		Document document;
		try {
			document = reader.read(stream);
			Element root=document.getRootElement();
			return root;
		} catch (DocumentException e) {
			throw new RuleException(e);
		}
	}
	
	public void loadPackages(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String project=req.getParameter("project");
		try{
			List<ResourcePackage> packages=repositoryService.loadProjectResourcePackages(project);		
			writeObjectToJson(resp, packages);
		}catch(Exception ex){
			throw new RuleException(ex);
		}
	}
	
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public void setFlowDeserializer(FlowDeserializer flowDeserializer) {
		this.flowDeserializer = flowDeserializer;
	}
	
	@Override
	public String url() {
		return "/ruleflowdesigner";
	}
}
