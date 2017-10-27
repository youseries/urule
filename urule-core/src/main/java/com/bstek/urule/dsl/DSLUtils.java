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

import com.bstek.urule.RuleException;
import com.bstek.urule.dsl.RuleParserParser.OpContext;
import com.bstek.urule.model.rule.Op;

/**
 * @author Jacky.gao
 * @since 2015年5月6日
 */
public class DSLUtils {
	public static Op parseOp(OpContext ctx){
		if(ctx.GreaterThen()!=null){
			return Op.GreaterThen;
		}else if(ctx.GreaterThenOrEquals()!=null){
			return Op.GreaterThenEquals;
		}else if(ctx.LessThen()!=null){
			return Op.LessThen;
		}else if(ctx.LessThenOrEquals()!=null){
			return Op.LessThenEquals;
		}else if(ctx.Equals()!=null){
			return Op.Equals;
		}else if(ctx.NotEquals()!=null){
			return Op.NotEquals;
		}else if(ctx.EndWith()!=null){
			return Op.EndWith;
		}else if(ctx.NotEndWith()!=null){
			return Op.NotEndWith;
		}else if(ctx.StartWith()!=null){
			return Op.StartWith;
		}else if(ctx.NotStartWith()!=null){
			return Op.NotStartWith;
		}else if(ctx.In()!=null){
			return Op.In;
		}else if(ctx.NotIn()!=null){
			return Op.NotIn;
		}else if(ctx.Match()!=null){
			return Op.Match;
		}else if(ctx.NotMatch()!=null){
			return Op.NotMatch;
		}else if(ctx.EqualsIgnoreCase()!=null){
			return Op.EqualsIgnoreCase;
		}else if(ctx.NotEqualsIgnoreCase()!=null){
			return Op.NotEqualsIgnoreCase;
		}
		throw new RuleException("Operator ["+ctx+"] is invalid.");
	}
}
