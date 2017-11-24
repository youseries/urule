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
package com.bstek.urule.console.servlet;

import java.lang.reflect.InvocationTargetException;
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
		Method method;
		try {
			method = this.getClass().getMethod(methodName, new Class<?>[]{HttpServletRequest.class,HttpServletResponse.class});
			method.invoke(this, new Object[]{req,resp});
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuleException(e);
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
