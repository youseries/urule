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
package com.bstek.urule.builder.table;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import com.bstek.urule.RuleException;
import com.bstek.urule.dsl.CellScriptRuleParserBaseVisitor;
import com.bstek.urule.dsl.RuleParserLexer;
import com.bstek.urule.dsl.RuleParserParser;
import com.bstek.urule.dsl.ScriptDecisionTableErrorListener;

/**
 * @author Jacky.gao
 * @since 2015年5月6日
 */
public class CellScriptDSLBuilder {
	public String buildCriteriaScript(String script,String propertyName){
		ANTLRInputStream antlrInputStream=new ANTLRInputStream(script);
		RuleParserLexer lexer=new RuleParserLexer(antlrInputStream);
		CommonTokenStream tokenStream=new CommonTokenStream(lexer);
		RuleParserParser parser=new RuleParserParser(tokenStream);
		ScriptDecisionTableErrorListener errorListener=new ScriptDecisionTableErrorListener();
		parser.addErrorListener(errorListener);
		CellScriptRuleParserBaseVisitor visitor=new CellScriptRuleParserBaseVisitor(propertyName);
		String resultScript=visitor.visit(parser.decisionTableCellCondition());
		String error=errorListener.getErrorMessage();
		if(error!=null){
			throw new RuleException("Script Parse error:"+error);
		}
		return resultScript;
	}
}
