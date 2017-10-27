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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.LibraryType;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.RuleSet;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class RuleSetParser implements Parser<RuleSet> {
	private RuleParser ruleParser;
	private LoopRuleParser loopRuleParser;
	private RulesRebuilder rulesRebuilder;
	public RuleSet parse(Element element) {
		RuleSet ruleSet=new RuleSet();
		String parameterLibrary=element.attributeValue("parameter-library");
		if(StringUtils.isNotEmpty(parameterLibrary)){
			ruleSet.addLibrary(new Library(parameterLibrary,null,LibraryType.Parameter));
		}
		List<Rule> rules=new ArrayList<Rule>();
		for(Object obj:element.elements()){
			if(obj==null){
				continue;
			}
			if(!(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			String name=ele.getName();
			if(ruleParser.support(name)){
				rules.add(ruleParser.parse(ele));
			}else if(loopRuleParser.support(name)){
				rules.add(loopRuleParser.parse(ele));
			}else if(name.equals("import-variable-library")){
				ruleSet.addLibrary(new Library(ele.attributeValue("path"),null,LibraryType.Variable));
			}else if(name.equals("import-constant-library")){
				ruleSet.addLibrary(new Library(ele.attributeValue("path"),null,LibraryType.Constant));
			}else if(name.equals("import-action-library")){
				ruleSet.addLibrary(new Library(ele.attributeValue("path"),null,LibraryType.Action));
			}else if(name.equals("import-parameter-library")){
				ruleSet.addLibrary(new Library(ele.attributeValue("path"),null,LibraryType.Parameter));
			}else if(name.equals("remark")){
				ruleSet.setRemark(ele.getText());
			}
		}
		ruleSet.setRules(rules);
		rulesRebuilder.rebuildRules(ruleSet.getLibraries(), rules);
		return ruleSet;
	}
	public void setRulesRebuilder(RulesRebuilder rulesRebuilder) {
		this.rulesRebuilder = rulesRebuilder;
	}
	
	public boolean support(String name) {
		return name.equals("rule-set");
	}
	public void setRuleParser(RuleParser ruleParser) {
		this.ruleParser = ruleParser;
	}
	public void setLoopRuleParser(LoopRuleParser loopRuleParser) {
		this.loopRuleParser = loopRuleParser;
	}
}
