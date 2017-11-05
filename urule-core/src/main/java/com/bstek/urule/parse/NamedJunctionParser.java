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
