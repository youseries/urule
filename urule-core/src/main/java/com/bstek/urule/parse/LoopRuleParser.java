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
