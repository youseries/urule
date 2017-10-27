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

import com.bstek.urule.model.rule.ArithmeticType;
import com.bstek.urule.model.rule.SimpleArithmetic;
import com.bstek.urule.model.rule.SimpleArithmeticValue;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class SimpleArithmeticParser implements Parser<SimpleArithmetic> {
	public SimpleArithmetic parse(Element element) {
		SimpleArithmetic arithmetic=new SimpleArithmetic();
		ArithmeticType arithmeticType=ArithmeticType.valueOf(element.attributeValue("type"));
		arithmetic.setType(arithmeticType);
		SimpleArithmeticValue value=new SimpleArithmeticValue();
		value.setContent(element.attributeValue("value"));
		arithmetic.setValue(value);
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(this.support(ele.getName())){
				value.setArithmetic(this.parse(ele));
				break;
			}
		}
		return arithmetic;
	}
	public boolean support(String name) {
		return name.equals("simple-arith");
	}
}
