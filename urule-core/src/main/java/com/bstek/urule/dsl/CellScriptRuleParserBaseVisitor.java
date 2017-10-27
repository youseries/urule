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
