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
