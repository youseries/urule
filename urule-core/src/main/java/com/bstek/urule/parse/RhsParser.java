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
import java.util.Collection;
import java.util.List;

import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.urule.action.Action;
import com.bstek.urule.model.rule.Rhs;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class RhsParser implements Parser<Rhs>,ApplicationContextAware {
	private Collection<ActionParser> actionParsers;
	public Rhs parse(Element element) {
		Rhs rhs=new Rhs();
		rhs.setActions(parseActions(element));
		return rhs;
	}
	public List<Action> parseActions(Element element){
		List<Action> actions=new ArrayList<Action>();
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			String name=ele.getName();
			for(ActionParser actionParser:actionParsers){
				if(actionParser.support(name)){
					actions.add(actionParser.parse(ele));
					break;
				}
			}
		}
		return actions;
	}
	
	public boolean support(String name) {
		return name.equals("then");
	}
	
	public Collection<ActionParser> getActionParsers() {
		return actionParsers;
	}
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		actionParsers=context.getBeansOfType(ActionParser.class).values();
	}
}
