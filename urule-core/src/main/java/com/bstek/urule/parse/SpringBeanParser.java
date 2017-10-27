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
package com.bstek.urule.parse;

import org.dom4j.Element;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.action.Method;
import com.bstek.urule.model.library.action.Parameter;
import com.bstek.urule.model.library.action.SpringBean;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class SpringBeanParser implements Parser<SpringBean> {
	public SpringBean parse(Element element) {
		SpringBean bean=new SpringBean();
		bean.setId(element.attributeValue("id"));
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(ele.getName().equals("method")){
				Method method=parseMethod(ele);
				bean.addMethod(method);
			}
		}
		return bean;
	}
	private Method parseMethod(Element element){
		Method method=new Method();
		method.setName(element.attributeValue("name"));
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(ele.getName().equals("parameter")){
				Parameter parameter=new Parameter();
				parameter.setType(Datatype.valueOf(ele.attributeValue("type")));
				method.addParameter(parameter);
			}
		}
		return method;
	}
	public boolean support(String name) {
		return name.equals("spring-bean");
	}
}
