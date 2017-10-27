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

import com.bstek.urule.model.library.variable.Variable;

/**
 * @author Jacky.gao
 * @since 2015年3月10日
 */
public class ParameterLibraryParser implements Parser<List<Variable>> {
	private VariableParser variableParser;
	@Override
	public List<Variable> parse(Element element) {
		List<Variable> variables=new ArrayList<Variable>();
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			String name=ele.getName();
			if(name.equals("parameter")){
				variables.add(variableParser.parse(ele));
			}
		}
		return variables;
	}
	@Override
	public boolean support(String name) {
		return name.equals("parameter-library");
	}
	public void setVariableParser(VariableParser variableParser) {
		this.variableParser = variableParser;
	}
}
