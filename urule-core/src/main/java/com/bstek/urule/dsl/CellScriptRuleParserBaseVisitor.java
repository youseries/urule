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
package com.bstek.urule.dsl;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import com.bstek.urule.dsl.RuleParserParser.DecisionTableCellConditionContext;
import com.bstek.urule.dsl.RuleParserParser.MultiCellConditionsContext;
import com.bstek.urule.dsl.RuleParserParser.ParenCellConditionsContext;
import com.bstek.urule.dsl.RuleParserParser.SingleCellConditionContext;

/**
 * @author Jacky.gao
 * @since 2015年5月6日
 */
public class CellScriptRuleParserBaseVisitor extends RuleParserBaseVisitor<String> {
	private String propertyName;
	public CellScriptRuleParserBaseVisitor(String propertyName) {
		this.propertyName=propertyName;
	}
	
	@Override
	public String visitSingleCellCondition(SingleCellConditionContext ctx) {
		StringBuffer sb=new StringBuffer();
		sb.append(propertyName);
		sb.append(" ");
		String op=ctx.op().getText();
		sb.append(op);
		sb.append(" ");
		if(ctx.complexValue()!=null){
			sb.append(ctx.complexValue().getText());
		}else{
			sb.append(ctx.nullValue().getText());
		}
		sb.append(" ");
		return sb.toString();
	}
	
	@Override
	public String visitMultiCellConditions(MultiCellConditionsContext ctx) {
		StringBuffer sb=new StringBuffer();
		List<ParseTree> children=ctx.children;
		for(ParseTree child:children){
			sb.append(" ");
			buildChildren(sb, child);
		}
		return sb.toString();
	}

	@Override
	public String visitParenCellConditions(ParenCellConditionsContext ctx) {
		StringBuffer sb=new StringBuffer();
		sb.append(" ");
		sb.append(ctx.leftParen().getText());
		DecisionTableCellConditionContext context=ctx.decisionTableCellCondition();
		buildChildren(sb,context);
		sb.append(ctx.rightParen().getText());
		return sb.toString();
	}
	
	private void buildChildren(StringBuffer sb, ParseTree child) {
		if(child instanceof SingleCellConditionContext){
			SingleCellConditionContext singleCellConditionContext=(SingleCellConditionContext)child;
			sb.append(visitSingleCellCondition(singleCellConditionContext));
		}else if(child instanceof ParenCellConditionsContext){
			ParenCellConditionsContext parenCellConditionsContext=(ParenCellConditionsContext)child;
			sb.append(visitParenCellConditions(parenCellConditionsContext));
		}else if(child instanceof MultiCellConditionsContext){
			MultiCellConditionsContext multiCellConditionsContext=(MultiCellConditionsContext)child;
			sb.append(visitMultiCellConditions(multiCellConditionsContext));
		}else{
			sb.append(child.getText());
		}
	}
}
