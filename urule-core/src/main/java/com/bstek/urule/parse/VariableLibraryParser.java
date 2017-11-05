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

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.bstek.urule.model.library.variable.VariableCategory;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class VariableLibraryParser implements Parser<List<VariableCategory>> {
	private VariableCategoryParser variableCategoryParser;
	public List<VariableCategory> parse(Element element) {
		List<VariableCategory> variableCategories=new ArrayList<VariableCategory>();
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			String name=ele.getName();
			if(variableCategoryParser.support(name)){
				variableCategories.add(variableCategoryParser.parse(ele));
			}
		}
		return variableCategories;
	}
	public boolean support(String name) {
		return name.equals("variable-library");
	}
	public void setVariableCategoryParser(VariableCategoryParser variableCategoryParser) {
		this.variableCategoryParser = variableCategoryParser;
	}
}
