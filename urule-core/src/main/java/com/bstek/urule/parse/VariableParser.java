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
import com.bstek.urule.model.library.variable.Act;
import com.bstek.urule.model.library.variable.Variable;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class VariableParser implements Parser<Variable> {
	public Variable parse(Element element) {
		Variable variable=new Variable();
		variable.setName(element.attributeValue("name"));
		variable.setLabel(element.attributeValue("label"));
		variable.setDefaultValue(element.attributeValue("default-value"));
		variable.setType(Datatype.valueOf(element.attributeValue("type")));
		variable.setAct(Act.valueOf(element.attributeValue("act")));
		return variable;
	}
	public boolean support(String name) {
		return name.equals("var");
	}
}
