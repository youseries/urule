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
