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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bstek.urule.model.Node;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.runtime.rete.ObjectTypeActivity;
import com.bstek.urule.runtime.rete.ReteInstance;

/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public class Rete implements Node {
	private List<ObjectTypeNode> objectTypeNodes;
	private ResourceLibrary resourceLibrary;
	public Rete() {
	}
	public Rete(List<ObjectTypeNode> objectTypeNodes,ResourceLibrary resourceLibrary){
		this.objectTypeNodes = objectTypeNodes;
		this.resourceLibrary=resourceLibrary;
	}
	public List<ObjectTypeNode> getObjectTypeNodes() {
		return objectTypeNodes;
	}
	public ResourceLibrary getResourceLibrary() {
		return resourceLibrary;
	}

	public ReteInstance newReteInstance(){
		List<ObjectTypeActivity> objectTypeActivities=new ArrayList<ObjectTypeActivity>();
		Map<Object,Object> contextMap=new HashMap<Object,Object>();
		for(ObjectTypeNode node:objectTypeNodes){
			objectTypeActivities.add((ObjectTypeActivity)node.newActivity(contextMap));
		}
		return new ReteInstance(objectTypeActivities);
	}
}
