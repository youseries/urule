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
