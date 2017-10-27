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

import org.dom4j.Element;

import com.bstek.urule.model.rule.loop.LoopEnd;
import com.bstek.urule.model.rule.loop.LoopRule;
import com.bstek.urule.model.rule.loop.LoopStart;
import com.bstek.urule.model.rule.loop.LoopTarget;

/**
 * @author Jacky.gao
 * @since 2016年5月31日
 */
public class LoopRuleParser extends AbstractRuleParser<LoopRule> {
	private ValueParser valueParser;
	public LoopRule parse(Element element) {
		LoopRule rule=new LoopRule();
		parseRule(rule, element);
		
		LoopStart loopStart=new LoopStart();
		rule.setLoopStart(loopStart);
		LoopEnd loopEnd=new LoopEnd();
		rule.setLoopEnd(loopEnd);
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			String name=ele.getName();
			if(name.equals("loop-start")){
				loopStart.setActions(rhsParser.parseActions(ele));
			}else if(name.equals("loop-end")){
				loopEnd.setActions(rhsParser.parseActions(ele));
			}else if(name.equals("loop-target")){
				LoopTarget loopTarget=new LoopTarget();
				rule.setLoopTarget(loopTarget);			
				for(Object eleObj:ele.elements()){
					if(eleObj==null || !(eleObj instanceof Element)){
						continue;
					}
					Element e=(Element)eleObj;
					if(valueParser.support(e.getName())){
						loopTarget.setValue(valueParser.parse(e));
						break;
					}
				}
			
			}
		}
		
		return rule;
	}
	
	
	public void setValueParser(ValueParser valueParser) {
		this.valueParser = valueParser;
	}
	
	public boolean support(String name) {
		return name.equals("loop-rule");
	}
}
