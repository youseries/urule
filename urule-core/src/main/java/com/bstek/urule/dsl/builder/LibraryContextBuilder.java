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
