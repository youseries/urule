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

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.bstek.urule.action.Action;
import com.bstek.urule.action.VariableAssignAction;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.lhs.LeftType;
/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class VariableAssignActionParser extends ActionParser {
	public Action parse(Element element) {
		VariableAssignAction action=new VariableAssignAction();
		String referenceName=element.attributeValue("reference-name");
		if(StringUtils.isNotEmpty(referenceName)){
			action.setReferenceName(referenceName);
		}
		String variable=element.attributeValue("var");
		if(StringUtils.isEmpty(variable)){
			variable=element.attributeValue("property-name");
		}
		action.setVariableName(variable);
		String variableLabel=element.attributeValue("var-label");
		if(StringUtils.isEmpty(variableLabel)){
			variableLabel=element.attributeValue("property-label");
		}
		action.setVariableLabel(variableLabel);
		String variableCategory=element.attributeValue("var-category");
		action.setVariableCategory(variableCategory);
		String datatype=element.attributeValue("datatype");
		if(StringUtils.isNotEmpty(datatype)){
			action.setDatatype(Datatype.valueOf(datatype));
		}
		String type=element.attributeValue("type");
		if(StringUtils.isNotEmpty(type)){
			action.setType(LeftType.valueOf(type));
		}
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(valueParser.support(ele.getName())){
				action.setValue(valueParser.parse(ele));
				break;
			}
		}
		return action;
	}
	public boolean support(String name) {
		return name.equals("var-assign");
	}
}
