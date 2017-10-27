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
package com.bstek.urule.console.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bstek.urule.console.exception.NoPermissionException;

/**
 * @author Jacky.gao
 * @since 2016年5月23日
 */
public class URuleServlet extends HttpServlet{
	private static final long serialVersionUID = -5067484267904906233L;
	private Map<String,ServletHandler> handlerMap=new HashMap<String,ServletHandler>();
	public static final String URL="/urule";
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		WebApplicationContext applicationContext=WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		Collection<ServletHandler> handlers=applicationContext.getBeansOfType(ServletHandler.class).values();
		for(ServletHandler handler:handlers){
			String url=handler.url();
			if(handlerMap.containsKey(url)){
				throw new RuntimeException("Handler ["+url+"] already exist.");
			}
			handlerMap.put(url, handler);
		}
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestHolder.set(req, resp);
		try{
			String path=req.getContextPath()+URL;
			String uri=req.getRequestURI();
			String targetUrl=uri.substring(path.length());
			if(targetUrl.length()<1){
				resp.sendRedirect(req.getContextPath()+"/urule/frame");
				return;
			}
			int slashPos=targetUrl.indexOf("/",1);
			if(slashPos>-1){
				targetUrl=targetUrl.substring(0,slashPos);
			}
			ServletHandler targetHandler=handlerMap.get(targetUrl);
			if(targetHandler==null){
				outContent(resp,"Handler ["+targetUrl+"] not exist.");
				return;
			}
			targetHandler.execute(req, resp);
		}catch(Exception ex){
			Throwable e=getCause(ex);
			resp.setCharacterEncoding("UTF-8");
			PrintWriter pw=resp.getWriter();
			if(e instanceof NoPermissionException){
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				pw.write("<h1>Permission denied!</h1>");
				pw.close();
			}else{
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				String errorMsg = e.getMessage();
				if(StringUtils.isBlank(errorMsg)){
					errorMsg=e.getClass().getName();
				}
				pw.write(errorMsg);
				pw.close();				
				throw new ServletException(ex);				
			}
		}finally{
			RequestHolder.reset();
		}
	}

	private void outContent(HttpServletResponse resp,String msg) throws IOException {
		resp.setContentType("text/html");
		PrintWriter pw=resp.getWriter();
		pw.write("<html>");
		pw.write("<header><title>URule Console</title></header>");
		pw.write("<body>");
		pw.write(msg);
		pw.write("</body>");
		pw.write("</html>");
		pw.flush();
		pw.close();
	}
	
	private Throwable getCause(Throwable e){
		if(e.getCause()!=null){
			return getCause(e.getCause());
		}
		return e;
	}
}
