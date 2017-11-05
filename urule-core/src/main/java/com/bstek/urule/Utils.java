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
package com.bstek.urule;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.model.library.Datatype;

/**
 * @author Jacky.gao
 * @since 2015年1月8日
 */
public class Utils implements ApplicationContextAware{
	private static boolean debug;
	private static ApplicationContext applicationContext;
	private static Map<String,FunctionDescriptor> functionDescriptorMap=new HashMap<String,FunctionDescriptor>();
	private static Map<String,FunctionDescriptor> functionDescriptorLabelMap=new HashMap<String,FunctionDescriptor>();
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public static String decodeURL(String str){
		if(StringUtils.isBlank(str)){
			return str;
		}
		try {
			return URLDecoder.decode(URLDecoder.decode(str,"utf-8"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuleException(e);
		}
	}
	
	public static String encodeURL(String str){
		if(StringUtils.isBlank(str)){
			return str;
		}
		try {
			return URLEncoder.encode(str,"utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuleException(e);
		}
	}
	
	public static String toUTF8(String text){
		try{
			if (text == null) {
				return null;
			}
			byte[] fileBytes=text.getBytes("iso8859-1");
			boolean isiso=text.equals(new String(fileBytes, "iso8859-1"));
			if(isiso){
				text=new String(fileBytes,"utf-8");
			}
			isiso=text.equals(new String(text.getBytes("iso8859-1"), "iso8859-1"));
			if(isiso){
				text=new String(fileBytes,"utf-8");
			}
			return text;			
		}catch(Exception ex){
			throw new RuleException(ex);
		}
	}
	public static Object getObjectProperty(Object object,String property){
		try {
			return PropertyUtils.getProperty(object, property);
		} catch (Exception e) {
			throw new RuleException(e);
		}
	}
	public static void setObjectProperty(Object object,String property,Object value){
		try {
			BeanUtils.setProperty(object, property, value);
		} catch (Exception e) {
			throw new RuleException(e);
		}
	}
	
	public static Datatype getDatatype(Object obj){
		Datatype datatype=null;
		if(obj==null){
			datatype=Datatype.Object;
		}else{
			if(obj instanceof Integer){
				datatype=Datatype.Integer;
			}else if(obj instanceof Long){
				datatype=Datatype.Long;
			}else if(obj instanceof Double){
				datatype=Datatype.Double;
			}else if(obj instanceof Float){
				datatype=Datatype.Float;
			}else if(obj instanceof BigDecimal){
				datatype=Datatype.BigDecimal;
			}else if(obj instanceof Boolean){
				datatype=Datatype.Boolean;
			}else if(obj instanceof Date){
				datatype=Datatype.Date;
			}else if(obj instanceof List){
				datatype=Datatype.List;
			}else if(obj instanceof Set){
				datatype=Datatype.Set;
			}else if(obj instanceof Enum){
				datatype=Datatype.Enum;
			}else if(obj instanceof Map){
				datatype=Datatype.Map;
			}else if(obj instanceof String){
				datatype=Datatype.String;
			}else if(obj instanceof Character){
				datatype=Datatype.Char;
			}else{
				datatype=Datatype.Object;
			}
		}
		return datatype;
	}
	
	public static BigDecimal toBigDecimal(Object val) {
		try{
			if (val instanceof BigDecimal) {
				return (BigDecimal) val;
			} else if (val == null) {
				throw new IllegalArgumentException("Null can not to BigDecimal.");
			} else if (val instanceof String) {
				String string = (String) val;
				if ("".equals(string.trim())) {
					return BigDecimal.valueOf(0);
				}
				return new BigDecimal(string);
			} else if (val instanceof Number) {
				return new BigDecimal(val.toString());
			} else if (val instanceof Character) {
				int i = ((Character) val).charValue();
				return new BigDecimal(i);
			}			
		}catch(Exception ex){
			throw new NumberFormatException("Can not convert "+val+" to number.");
		}
        
        throw new IllegalArgumentException(val.getClass().getName()+" can not to BigDecimal.");
    }
	
	public static FunctionDescriptor findFunctionDescriptor(String functionName){
		if(!functionDescriptorMap.containsKey(functionName)){
			throw new RuleException("Function["+functionName+"] not exist.");
		}
		return functionDescriptorMap.get(functionName);
	}
	
	public static Map<String, FunctionDescriptor> getFunctionDescriptorLabelMap() {
		return functionDescriptorLabelMap;
	}
	public static Map<String, FunctionDescriptor> getFunctionDescriptorMap() {
		return functionDescriptorMap;
	}
	
	public void setDebug(boolean debug) {
		Utils.debug = debug;
	}
	
	public static boolean isDebug() {
		return debug;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Collection<FunctionDescriptor> functionDescriptors=applicationContext.getBeansOfType(FunctionDescriptor.class).values();
		for(FunctionDescriptor fun:functionDescriptors){
			if(fun.isDisabled()){
				continue;
			}
			if(functionDescriptorMap.containsKey(fun.getName())){
				throw new RuntimeException("Duplicate function ["+fun.getName()+"]");
			}
			functionDescriptorMap.put(fun.getName(), fun);
			functionDescriptorLabelMap.put(fun.getLabel(), fun);
		}
		Utils.applicationContext=applicationContext;
		new Splash().print();
	}
}
