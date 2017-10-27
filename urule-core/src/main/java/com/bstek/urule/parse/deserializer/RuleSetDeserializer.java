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
package com.bstek.urule.parse.deserializer;

import org.dom4j.Element;

import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.parse.RuleSetParser;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class RuleSetDeserializer implements Deserializer<RuleSet>{
	public static final String BEAN_ID="urule.ruleSetDeserializer";
	private RuleSetParser ruleSetParser;
	public RuleSet deserialize(Element root) {
		return ruleSetParser.parse(root);
	}
	public boolean support(Element root) {
		if(ruleSetParser.support(root.getName())){
			return true;
		}else{
			return false;
		}
	}
	public void setRuleSetParser(RuleSetParser ruleSetParser) {
		this.ruleSetParser = ruleSetParser;
	}
}
