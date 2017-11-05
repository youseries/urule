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

import java.util.List;

import org.dom4j.Element;

import com.bstek.urule.action.Action;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.model.rule.Parameter;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class ExecuteMethodActionParser extends ActionParser {
	public Action parse(Element element) {
		ExecuteMethodAction action=new ExecuteMethodAction();
		action.setBeanId(element.attributeValue("bean"));
		action.setBeanLabel(element.attributeValue("bean-label"));
		action.setMethodLabel(element.attributeValue("method-label"));
		action.setMethodName(element.attributeValue("method-name"));
		List<Parameter> parameters = parseParameters(element,valueParser);
		action.setParameters(parameters);
		return action;
	}
	public boolean support(String name) {
		return name.equals("execute-method");
	}
}
