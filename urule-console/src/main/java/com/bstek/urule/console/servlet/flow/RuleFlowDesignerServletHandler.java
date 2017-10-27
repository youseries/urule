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
		if(StringUtils.isEmpty(version)){
			inputStream=repositoryService.readFile(file,null);
		}else{
			inputStream=repositoryService.readFile(file,version);
		}
		Element root=parseXml(inputStream);
		FlowDefinition fd = flowDeserializer.deserialize(root);
		inputStream.close();
		writeObjectToJson(resp, new FlowDefinitionWrapper(fd));
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
