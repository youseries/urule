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
