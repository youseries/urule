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

import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bstek.urule.RuleException;


/**
 * @author Jacky.gao
 * @since 2016年6月3日
 */
public abstract class BaseServletHandler implements ServletHandler {
	
	protected void invokeMethod(String methodName,HttpServletRequest req,HttpServletResponse resp){
		try{
			Method method=this.getClass().getMethod(methodName, new Class<?>[]{HttpServletRequest.class,HttpServletResponse.class});			
			method.invoke(this, new Object[]{req,resp});
		}catch(Exception ex){
			throw new RuleException(ex);
		}
	}
	
	protected String retriveMethod(HttpServletRequest req) throws ServletException{
		String path=req.getContextPath()+URuleServlet.URL;
		String uri=req.getRequestURI();
		String targetUrl=uri.substring(path.length());
		int slashPos=targetUrl.indexOf("/",1);
		if(slashPos>-1){
			String methodName=targetUrl.substring(slashPos+1).trim();
			return methodName.length()>0 ? methodName : null;
		}
		return null;
	}
}
