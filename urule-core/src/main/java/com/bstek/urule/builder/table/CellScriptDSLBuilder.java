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
