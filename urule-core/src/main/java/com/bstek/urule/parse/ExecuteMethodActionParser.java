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

import java.util.List;

import org.dom4j.Element;

import com.bstek.urule.action.Action;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.model.rule.Parameter;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class ExecuteMethodActionParser extends ActionParser {
	public Action parse(Element element) {
		ExecuteMethodAction action=new ExecuteMethodAction();
		action.setBeanId(element.attributeValue("bean"));
		action.setBeanLabel(element.attributeValue("bean-label"));
		action.setMethodLabel(element.attributeValue("method-label"));
		action.setMethodName(element.attributeValue("method-name"));
		List<Parameter> parameters = parseParameters(element,valueParser);
		action.setParameters(parameters);
		return action;
	}
	public boolean support(String name) {
		return name.equals("execute-method");
	}
}
