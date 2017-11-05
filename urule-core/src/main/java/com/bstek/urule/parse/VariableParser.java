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

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.variable.Act;
import com.bstek.urule.model.library.variable.Variable;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class VariableParser implements Parser<Variable> {
	public Variable parse(Element element) {
		Variable variable=new Variable();
		variable.setName(element.attributeValue("name"));
		variable.setLabel(element.attributeValue("label"));
		variable.setDefaultValue(element.attributeValue("default-value"));
		variable.setType(Datatype.valueOf(element.attributeValue("type")));
		variable.setAct(Act.valueOf(element.attributeValue("act")));
		return variable;
	}
	public boolean support(String name) {
		return name.equals("var");
	}
}
