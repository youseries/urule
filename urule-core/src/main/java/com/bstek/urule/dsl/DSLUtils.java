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
		}else if(ctx.Contain()!=null){
			return Op.Contain;
		}else if(ctx.NotContain()!=null){
			return Op.NotContain;
		}
		throw new RuleException("Operator ["+ctx+"] is invalid.");
	}
}
