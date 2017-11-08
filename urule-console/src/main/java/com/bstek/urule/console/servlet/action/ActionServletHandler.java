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
package com.bstek.urule.console.servlet.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import com.bstek.urule.console.servlet.RenderPageServletHandler;
import com.bstek.urule.model.ExposeAction;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.action.Method;
import com.bstek.urule.model.library.action.Parameter;

public class ActionServletHandler extends RenderPageServletHandler{
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
			Template template=ve.getTemplate("html/action-editor.html","utf-8");
			PrintWriter writer=resp.getWriter();
			template.merge(context, writer);
			writer.close();
		}
	}
	public void loadMethods(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String beanId=req.getParameter("beanId");
		Object o=applicationContext.getBean(beanId);
		Object bean=getTarget(o);
		List<Method> list=new ArrayList<Method>();
		java.lang.reflect.Method[] methods=bean.getClass().getMethods();
		for(java.lang.reflect.Method m:methods){
			ExposeAction action=m.getAnnotation(ExposeAction.class);
			if(action==null){
				continue;
			}
			String name=m.getName();
			Method method=new Method();
			method.setMethodName(name);
			method.setName(action.value());
			method.setParameters(buildParameters(m));
			list.add(method);
		}
		writeObjectToJson(resp, list);
	}
	
	private Object getTarget(Object proxy){
		if(!AopUtils.isAopProxy(proxy)) {
			return proxy;//不是代理对象
		}
		try {
			if(AopUtils.isJdkDynamicProxy(proxy)) {
					return getJdkDynamicProxyTargetObject(proxy);
			} else { //cglib
				return getCglibProxyTargetObject(proxy);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Object getCglibProxyTargetObject(Object proxy) throws Exception {
		Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
		h.setAccessible(true);
		Object dynamicAdvisedInterceptor = h.get(proxy);
		Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
		advised.setAccessible(true);

		Object target = ((AdvisedSupport)advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();

		return target;
	}


	private Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
		Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
		h.setAccessible(true);
		AopProxy aopProxy = (AopProxy) h.get(proxy);
		Field advised = aopProxy.getClass().getDeclaredField("advised");
		advised.setAccessible(true);
		Object target = ((AdvisedSupport)advised.get(aopProxy)).getTargetSource().getTarget();
		return target;
	}
	
	private List<Parameter> buildParameters(java.lang.reflect.Method m){
		List<Parameter> parameters=new ArrayList<Parameter>();
		Class<?>[] classes=m.getParameterTypes();
		for(int i=0;i<classes.length;i++){
			Class<?> c=classes[i];
			Parameter p=new Parameter();
			p.setName("参数"+i);
			p.setType(buildDatatype(c));
			parameters.add(p);
		}
		return parameters;
	}
	
	private Datatype buildDatatype(Class<?> clazz) {
		if(clazz.equals(String.class)){
			return Datatype.String;
		}else if(clazz.equals(BigDecimal.class)){
			return Datatype.BigDecimal;
		}else if(clazz.equals(Boolean.class)){
			return Datatype.Boolean;
		}else if(clazz.equals(Boolean.class)){
			return Datatype.Boolean;
		}else if(clazz.equals(boolean.class)){
			return Datatype.Boolean;
		}else if(clazz.equals(Date.class)){
			return Datatype.Date;
		}else if(clazz.equals(Double.class)){
			return Datatype.Double;
		}else if(clazz.equals(double.class)){
			return Datatype.Double;
		}else if(Enum.class.isAssignableFrom(clazz)){
			return Datatype.Enum;
		}else if(clazz.equals(Float.class)){
			return Datatype.Float;
		}else if(clazz.equals(float.class)){
			return Datatype.Float;
		}else if(clazz.equals(Integer.class)){
			return Datatype.Integer;
		}else if(clazz.equals(int.class)){
			return Datatype.Integer;
		}else if(clazz.equals(Character.class)){
			return Datatype.Char;
		}else if(clazz.equals(char.class)){
			return Datatype.Char;
		}else if(List.class.isAssignableFrom(clazz)){
			return Datatype.List;
		}else if(clazz.equals(long.class)){
			return Datatype.Long;
		}else if(clazz.equals(Long.class)){
			return Datatype.Long;
		}else if(Map.class.isAssignableFrom(clazz)){
			return Datatype.Map;
		}else if(Set.class.isAssignableFrom(clazz)){
			return Datatype.Set;
		}else{
			return Datatype.Object;
		}
	}	
	
	
	@Override
	public String url() {
		return "/actioneditor";
	}
	
}
