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
