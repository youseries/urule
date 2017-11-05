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

import com.bstek.urule.model.library.variable.CategoryType;
import com.bstek.urule.model.library.variable.VariableCategory;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class VariableCategoryParser implements Parser<VariableCategory> {
	private VariableParser variableParser;
	public VariableCategory parse(Element element) {
		VariableCategory category=new VariableCategory();
		category.setName(element.attributeValue("name"));
		category.setClazz(element.attributeValue("clazz"));
		category.setType(CategoryType.valueOf(element.attributeValue("type")));
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			String name=ele.getName();
			if(variableParser.support(name)){
				category.addVariable(variableParser.parse(ele));
			}
		}
		return category;
	}
	public boolean support(String name) {
		return name.equals("category");
	}
	public void setVariableParser(VariableParser variableParser) {
		this.variableParser = variableParser;
	}
}
