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
