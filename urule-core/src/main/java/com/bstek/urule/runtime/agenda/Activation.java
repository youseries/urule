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
