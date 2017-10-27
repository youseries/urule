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

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.JunctionType;
import com.bstek.urule.model.rule.lhs.NamedItem;
import com.bstek.urule.model.rule.lhs.NamedJunction;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class NamedJunctionParser extends CriterionParser {
	private ValueParser valueParser;
	public Criterion parse(Element element) {
		NamedJunction junction=new NamedJunction();
		junction.setReferenceName(element.attributeValue("reference-name"));
		junction.setVariableCategory(element.attributeValue("var-category"));
		junction.setJunctionType(JunctionType.valueOf(element.attributeValue("junction-type")));
		List<NamedItem> items=new ArrayList<NamedItem>();
		junction.setItems(items);
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			String name=ele.getName();
			if(!name.equals("named-criteria")){
				continue;
			}
			items.add(parseNamedItem(ele));
		}
		return junction;
	}
	
	private NamedItem parseNamedItem(Element element){
		NamedItem item=new NamedItem();
		String variable=element.attributeValue("var");
		if(StringUtils.isNotEmpty(variable)){
			item.setVariableName(variable);
		}
		String variableLabel=element.attributeValue("var-label");
		if(StringUtils.isNotEmpty(variableLabel)){
			item.setVariableLabel(variableLabel);
		}
		String datatype=element.attributeValue("datatype");
		if(StringUtils.isNotEmpty(datatype)){
			item.setDatatype(Datatype.valueOf(datatype));
		}
		Op op=Op.valueOf(element.attributeValue("op"));
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(valueParser.support(ele.getName())){
				item.setValue(valueParser.parse(ele));
				break;
			}
		}
		item.setOp(op);
		return item;
	}
	
	public void setValueParser(ValueParser valueParser) {
		this.valueParser = valueParser;
	}

	public boolean support(String name) {
		return name.equals("named-atom");
	}
}
