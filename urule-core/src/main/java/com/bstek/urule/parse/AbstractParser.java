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

import org.dom4j.Element;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.Value;

/**
 * @author Jacky.gao
 * @since 2015年2月28日
 */
public abstract  class AbstractParser<T> implements Parser<T> {
	protected List<Parameter> parseParameters(Element element,ValueParser valueParser) {
		List<Parameter> parameters=new ArrayList<Parameter>();
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(ele.getName().equals("parameter")){
				Parameter parameter=new Parameter();
				parameter.setName(ele.attributeValue("name"));
				parameter.setType(Datatype.valueOf(ele.attributeValue("type")));
				for(Object o:ele.elements()){
					if(o==null || !(o instanceof Element)){
						continue;
					}
					Element e=(Element)o;
					if(valueParser.support(e.getName())){
						Value value=valueParser.parse(e);
						parameter.setValue(value);
						break;
					}
				}
				parameters.add(parameter);
			}
		}
		return parameters;
	}
}
