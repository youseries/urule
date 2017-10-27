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
package com.bstek.urule.runtime.agenda;

import java.util.List;

import com.bstek.urule.action.ActionValue;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.RuleInfo;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.EvaluationContext;
/**
 * @author Jacky.gao
 * @since 2015年1月2日
 */
public interface Activation extends Comparable<Activation>{
	boolean isProcessed();
	Rule getRule();
	boolean reevaluate(Object obj,EvaluationContext context);
	boolean contain(Object obj);
	RuleInfo execute(Context context,List<RuleInfo> executedRules,List<ActionValue> actionValues);
}
