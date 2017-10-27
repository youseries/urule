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
