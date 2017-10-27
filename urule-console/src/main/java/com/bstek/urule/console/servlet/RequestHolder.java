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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jacky.gao
 * @since 2016年9月1日
 */
public class RequestHolder {
	private static ThreadLocal<HttpServletRequest> request=new ThreadLocal<HttpServletRequest>();
	private static ThreadLocal<HttpServletResponse> response=new ThreadLocal<HttpServletResponse>();
	public static void set(HttpServletRequest request,HttpServletResponse response) {
		RequestHolder.request.set(request);
		RequestHolder.response.set(response);
	}
	public static RequestContext newRequestContext(){
		return new RequestContext(request.get(),response.get());
	}
	public static void reset(){
		request.remove();
		response.remove();
	}
	public static HttpServletRequest getRequest() {
		return request.get();
	}
	public static HttpServletResponse getResponse() {
		return response.get();
	}
}
