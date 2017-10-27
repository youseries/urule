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
package com.bstek.urule.dsl.builder;

import org.antlr.v4.runtime.ParserRuleContext;

import com.bstek.urule.RuleException;
import com.bstek.urule.dsl.RuleParserParser.ResourceContext;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.LibraryType;

/**
 * @author Jacky.gao
 * @since 2015年2月15日
 */
public class LibraryContextBuilder extends AbstractContextBuilder {

	@Override
	public Library build(ParserRuleContext context) {
		ResourceContext ctx=(ResourceContext)context;
		if(ctx.importActionLibrary()!=null){
			String path=BuildUtils.getSTRINGContent(ctx.importActionLibrary().STRING());
			return new Library(path,null,LibraryType.Action);
		}else if(ctx.importConstantLibrary()!=null){
			String path=BuildUtils.getSTRINGContent(ctx.importConstantLibrary().STRING());
			return new Library(path,null,LibraryType.Constant);
		}else if(ctx.importVariableLibrary()!=null){
			String path=BuildUtils.getSTRINGContent(ctx.importVariableLibrary().STRING());
			return new Library(path,null,LibraryType.Variable);
		}else if(ctx.importParameterLibrary()!=null){
			String path=BuildUtils.getSTRINGContent(ctx.importParameterLibrary().STRING());
			return new Library(path,null,LibraryType.Parameter);
		}
		throw new RuleException("Unsupport context "+ctx.getClass().getName()+"");
	}

	@Override
	public boolean support(ParserRuleContext context) {
		return context instanceof ResourceContext;
	}
}
