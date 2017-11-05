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

import org.dom4j.Element;

import com.bstek.urule.model.rule.Op;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class CriteriaParser extends CriterionParser {
	private ValueParser valueParser;
	private LeftParser leftParser;
	public Criterion parse(Element element) {
		Criteria criteria=new Criteria();
		Op op=Op.valueOf(element.attributeValue("op"));
		criteria.setOp(op);
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			String name=ele.getName();
			if(name.equals("value")){
				criteria.setValue(valueParser.parse(ele));
			}else if(name.equals("left")){
				criteria.setLeft(leftParser.parse(ele));
			}
		}
		return criteria;
	}
	

	public boolean support(String name) {
		return name.equals("atom");
	}
	public void setValueParser(ValueParser valueParser) {
		this.valueParser = valueParser;
	}
	public void setLeftParser(LeftParser leftParser) {
		this.leftParser = leftParser;
	}
}
