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
import com.bstek.urule.runtime.rete.OrActivity;


/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public class OrNode extends JunctionNode {
	private NodeType nodeType=NodeType.or;
	public OrNode() {
		super(0);
	}
	public OrNode(int id) {
		super(id);
	}
	@Override
	public NodeType getNodeType() {
		return nodeType;
	}
	@Override
	public Activity newActivity(Map<Object,Object> context) {
		if(context.containsKey(this)){
			return (OrActivity)context.get(this);
		}
		OrActivity or=new OrActivity();
		for(Line line:lines){
			or.addPath(line.newPath(context));
		}
		context.put(this, or);
		return or;
	}
}
