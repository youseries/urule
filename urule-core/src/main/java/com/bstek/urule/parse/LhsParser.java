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

import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Lhs;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class LhsParser implements Parser<Lhs>,ApplicationContextAware {
	private Collection<CriterionParser> criterionParsers;
	public Lhs parse(Element element) {
		Lhs lhs=new Lhs();
		lhs.setCriterion(parseCriterion(element));
		return lhs;
	}
	
	public Criterion parseCriterion(Element element){
		Criterion criterion=null;
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			String name=ele.getName();
			for(CriterionParser parser:criterionParsers){
				if(parser.support(name)){
					criterion=(Criterion)parser.parse(ele);
					if(criterion!=null){
						break;
					}
				}
			}
		}
		return criterion;
	}
	
	
	public boolean support(String name) {
		return name.equals("if");
	}
	
	public Collection<CriterionParser> getCriterionParsers() {
		return criterionParsers;
	}
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		criterionParsers=applicationContext.getBeansOfType(CriterionParser.class).values();
	}
}
