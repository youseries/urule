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
package com.bstek.urule.console.servlet.variable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.bstek.urule.ClassUtils;
import com.bstek.urule.console.servlet.RenderPageServletHandler;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.variable.Act;
import com.bstek.urule.model.library.variable.Variable;

/**
 * @author Jacky.gao
 * @since 2016年6月3日
 */
public class VariableEditorServletHandler extends RenderPageServletHandler {

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
			Template template=ve.getTemplate("html/variable-editor.html","utf-8");
			PrintWriter writer=resp.getWriter();
			template.merge(context, writer);
			writer.close();
		}
	}
	
	public void importXml(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletContext servletContext = req.getSession().getServletContext();
		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);
		ServletFileUpload upload = new ServletFileUpload(factory);
		InputStream inputStream=null;
		try {
			List<FileItem> items = upload.parseRequest(req);
			if(items.size()!=1){
				throw new ServletException("Upload xml file is invalid.");
			}
			FileItem item=items.get(0);
			inputStream=item.getInputStream();
			String xmlContent=IOUtils.toString(inputStream, "utf-8");
			List<Variable> variables=new ArrayList<Variable>();
			Document doc=DocumentHelper.parseText(xmlContent);
			Element root=doc.getRootElement();
			String clazz=root.attributeValue("clazz");
			for(Object obj:root.elements()){
				if(obj==null || !(obj instanceof Element)){
					continue;
				}
				Element ele=(Element)obj;
				Variable var=new Variable();
				var.setAct(Act.InOut);
				var.setDefaultValue(ele.attributeValue("defaultValue"));
				var.setLabel(ele.attributeValue("label"));
				var.setName(ele.attributeValue("name"));
				var.setType(Datatype.valueOf(ele.attributeValue("type")));
				variables.add(var);
			}
			Map<String,Object> result=new HashMap<String,Object>();
			result.put("clazz", clazz);
			result.put("variables", variables);
			writeObjectToJson(resp, result);
		} catch (Exception e) {
			throw new ServletException(e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	public void generateFields(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String clazz=req.getParameter("clazz");
		try {
			List<Variable> variables=ClassUtils.classToVariables(Class.forName(clazz));
			writeObjectToJson(resp, variables);
		} catch (ClassNotFoundException e) {
			throw new ServletException(e);
		}
	}
	

	@Override
	public String url() {
		return "/variableeditor";
	}
}
