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
