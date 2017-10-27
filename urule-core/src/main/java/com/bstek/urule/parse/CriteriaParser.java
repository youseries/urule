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
