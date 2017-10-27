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
package com.bstek.urule.dsl;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.urule.RuleException;
import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.builder.resource.Resource;
import com.bstek.urule.dsl.builder.ContextBuilder;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.RuleSet;

/**
 * @author Jacky.gao
 * @since 2015年2月16日
 */
public class DSLRuleSetBuilder implements ApplicationContextAware{
	public static final String BEAN_ID="urule.dslRuleSetBuilder";
	private Collection<ContextBuilder> contextBuilders;
	private RulesRebuilder rulesRebuilder;
	public RuleSet build(String script) throws IOException{
		ANTLRInputStream antlrInputStream=new ANTLRInputStream(script);
		RuleParserLexer lexer=new RuleParserLexer(antlrInputStream);
		CommonTokenStream tokenStream=new CommonTokenStream(lexer);
		RuleParserParser parser=new RuleParserParser(tokenStream);
		ScriptDecisionTableErrorListener errorListener=new ScriptDecisionTableErrorListener();
		parser.addErrorListener(errorListener);
		BuildRulesVisitor visitor=new BuildRulesVisitor(contextBuilders,tokenStream);
		RuleSet ruleSet=visitor.visitRuleSet(parser.ruleSet());
		rebuildRuleSet(ruleSet);
		String error=errorListener.getErrorMessage();
		if(error!=null){
			throw new RuleException("Script parse error:"+error);
		}
		return ruleSet;
	}
	
	private void rebuildRuleSet(RuleSet ruleSet){
		List<Library> libraries=ruleSet.getLibraries();
		List<Rule> rules=ruleSet.getRules();
		rulesRebuilder.rebuildRulesForDSL(libraries, rules);
	}
	
	public void setRulesRebuilder(RulesRebuilder rulesRebuilder) {
		this.rulesRebuilder = rulesRebuilder;
	}


	public boolean support(Resource resource){
		String path=resource.getPath();
		return path.toLowerCase().endsWith(Constant.UL_SUFFIX);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		contextBuilders=applicationContext.getBeansOfType(ContextBuilder.class).values();
	}
}
