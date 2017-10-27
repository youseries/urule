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

import java.util.Collection;

import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.urule.model.rule.Other;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class OtherParser implements Parser<Other>,ApplicationContextAware {
	private Collection<ActionParser> actionParsers;
	public Other parse(Element element) {
		Other other=new Other();
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			String name=ele.getName();
			for(ActionParser actionParser:actionParsers){
				if(actionParser.support(name)){
					other.addAction(actionParser.parse(ele));
					break;
				}
			}
		}
		return other;
	}
	public boolean support(String name) {
		return name.equals("else");
	}
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		actionParsers=context.getBeansOfType(ActionParser.class).values();
	}
}
