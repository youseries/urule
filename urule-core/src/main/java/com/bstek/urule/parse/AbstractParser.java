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
