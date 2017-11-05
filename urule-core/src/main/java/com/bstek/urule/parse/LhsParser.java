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
