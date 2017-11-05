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

import com.bstek.urule.model.Node;
import com.bstek.urule.runtime.rete.Activity;

/**
 * @author Jacky.gao
 * @since 2015年1月12日
 */
public abstract class ReteNode implements Node {
	private int id;
	public ReteNode(int id){
		this.id=id;
	}
	public abstract NodeType getNodeType();
	public abstract Activity newActivity(Map<Object,Object> context);
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
