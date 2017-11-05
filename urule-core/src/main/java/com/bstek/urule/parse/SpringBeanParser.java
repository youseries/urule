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
