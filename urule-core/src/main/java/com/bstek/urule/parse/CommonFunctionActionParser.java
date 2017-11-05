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

import com.bstek.urule.action.Action;
import com.bstek.urule.action.ExecuteCommonFunctionAction;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;

/**
 * @author Jacky.gao
 * @since 2015年7月31日
 */
public class CommonFunctionActionParser extends ActionParser {
	@Override
	public Action parse(Element element) {
		ExecuteCommonFunctionAction action=new ExecuteCommonFunctionAction();
		action.setLabel(element.attributeValue("function-label"));
		action.setName(element.attributeValue("function-name"));
		for(Object obj:element.elements()){
			if(!(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(!ele.getName().equals("function-parameter")){
				continue;
			}
			CommonFunctionParameter p=new CommonFunctionParameter();
			p.setName(ele.attributeValue("name"));
			p.setProperty(ele.attributeValue("property-name"));
			p.setPropertyLabel(ele.attributeValue("property-label"));
			for(Object object:ele.elements()){
				if(!(object instanceof Element)){
					continue;
				}
				Element e=(Element)object;
				if(!e.getName().equals("value")){
					continue;
				}
				p.setObjectParameter(valueParser.parse(e));
			}
			action.setParameter(p);
		}
		return action;
	}
	@Override
	public boolean support(String name) {
		return name.equals("execute-function");
	}
}
