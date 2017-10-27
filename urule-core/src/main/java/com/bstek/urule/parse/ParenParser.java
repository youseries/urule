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

import com.bstek.urule.model.rule.ParenValue;

/**
 * @author Jacky.gao
 * @since 2015年6月14日
 */
public class ParenParser implements Parser<ParenValue> {
	private ValueParser valueParser;
	private ComplexArithmeticParser arithmeticParser;
	@Override
	public ParenValue parse(Element element) {
		ParenValue value=new ParenValue();
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(valueParser.support(ele.getName())){
				value.setValue(valueParser.parse(ele));
			}else if(arithmeticParser.support(ele.getName())){
				value.setArithmetic(arithmeticParser.parse(ele));
			}
		}
		return value;
	}

	@Override
	public boolean support(String name) {
		return name.equals("paren");
	}
	public void setValueParser(ValueParser valueParser) {
		this.valueParser = valueParser;
	}
	public void setArithmeticParser(ComplexArithmeticParser arithmeticParser) {
		this.arithmeticParser = arithmeticParser;
	}
}
