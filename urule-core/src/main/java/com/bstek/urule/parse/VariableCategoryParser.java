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
