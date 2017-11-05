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

import com.bstek.urule.runtime.rete.Activity;
import com.bstek.urule.runtime.rete.AndActivity;

/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public class AndNode extends JunctionNode {
	private NodeType nodeType=NodeType.and;
	public AndNode() {
		super(0);
	}
	public AndNode(int id) {
		super(id);
	}
	@Override
	public NodeType getNodeType() {
		return nodeType;
	}
	public void setToLineCount(int toLineCount){
		this.toLineCount=toLineCount;
	}
	@Override
	public Activity newActivity(Map<Object,Object> context) {
		if(context.containsKey(this)){
			return (AndActivity)context.get(this);
		}
		AndActivity and=new AndActivity();
		for(Line line:lines){
			and.addPath(line.newPath(context));
		}
		context.put(this, and);
		return and;
	}
}
