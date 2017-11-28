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
package com.bstek.urule.console.servlet.console;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.bstek.urule.console.servlet.RenderPageServletHandler;

/**
 * @author Jacky.gao
 * @since 2017年11月28日
 */
public class ConsoleServletHandler extends RenderPageServletHandler {
	private DebugMessageHolder debugMessageHolder;
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String key=req.getParameter("key");
		String msg=null;
		if(StringUtils.isBlank(key)){
			msg="<h2 style='color:red'>请指定要查看的调试消息的key值</h2>";
		}else{
			msg=debugMessageHolder.getDebugMessage(key);
		}
		VelocityContext context = new VelocityContext();
		context.put("title", "URule Console");
		context.put("msg", msg);
		resp.setContentType("text/html");
		resp.setCharacterEncoding("utf-8");
		Template template=ve.getTemplate("html/console.html","utf-8");
		PrintWriter writer=resp.getWriter();
		template.merge(context, writer);
		writer.close();
	}
	
	public void setDebugMessageHolder(DebugMessageHolder debugMessageHolder) {
		this.debugMessageHolder = debugMessageHolder;
	}

	@Override
	public String url() {
		return "/console";
	}
}
