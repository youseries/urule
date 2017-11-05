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
package com.bstek.urule.model.rete;

import java.util.Map;

import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.runtime.rete.Activity;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.TerminalActivity;

/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public class TerminalNode extends ReteNode {
	private Rule rule;
	private NodeType nodeType=NodeType.terminal;
	public TerminalNode() {
		super(0);
	}
	public TerminalNode(Rule rule,int id){
		super(id);
		this.rule=rule;
	}
	@Override
	public NodeType getNodeType() {
		return nodeType;
	}
	public Rule[] enter(Context context, Object object) {
		return new Rule[]{rule};
	}
	public Rule getRule() {
		return rule;
	}
	public void setRule(Rule rule) {
		this.rule = rule;
	}
	@Override
	public Activity newActivity(Map<Object,Object> context) {
		if(context.containsKey(this)){
			return (TerminalActivity)context.get(this);
		}
		TerminalActivity activity=new TerminalActivity(rule);
		context.put(this, activity);
		return activity;
	}
}
