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
import com.bstek.urule.model.library.action.ActionLibrary;
import com.bstek.urule.model.library.action.Method;
import com.bstek.urule.model.library.action.Parameter;
import com.bstek.urule.model.library.action.SpringBean;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class ActionLibraryParser implements Parser<ActionLibrary> {
	public ActionLibrary parse(Element element) {
		ActionLibrary lib=new ActionLibrary();
		for(Object obj:element.elements()){
			if(obj==null){
				continue;
			}
			if(!(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(ele.getName().equals("spring-bean")){
				lib.addSpringBean(parseSpringBean(ele));
			}
		}
		return lib;
	}
	private SpringBean parseSpringBean(Element element){
		SpringBean bean=new SpringBean();
		bean.setId(element.attributeValue("id"));
		bean.setName(element.attributeValue("name"));
		for(Object obj:element.elements()){
			if(obj==null){
				continue;
			}
			if(!(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			Method method=new Method();
			method.setMethodName(ele.attributeValue("method-name"));
			method.setName(ele.attributeValue("name"));
			for(Object o:ele.elements()){
				if(o==null){
					continue;
				}
				if(!(o instanceof Element)){
					continue;
				}
				Element e=(Element)o;
				if(e.getName().equals("parameter")){
					Parameter parameter=new Parameter();
					parameter.setName(e.attributeValue("name"));
					parameter.setType(Datatype.valueOf(e.attributeValue("type")));
					method.addParameter(parameter);
				}
			}
			bean.addMethod(method);
		}
		return bean;
	}
	public boolean support(String name) {
		return name.equals("action-library");
	}
}
