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
import com.bstek.urule.runtime.rete.ObjectTypeActivity;

/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public class ObjectTypeNode extends BaseReteNode{
	public static final String NON_CLASS="*";
	private String objectTypeClass;
	private NodeType nodeType=NodeType.objectType;
	public ObjectTypeNode() {
		super(0);
	}
	public ObjectTypeNode(String objectTypeClass,int id) {
		super(id);
		this.objectTypeClass = objectTypeClass;
	}
	@Override
	public NodeType getNodeType() {
		return nodeType;
	}
	public boolean support(Object object){
		return support(object.getClass().getName());
	}
	public boolean support(String className){
		return objectTypeClass.equals(className);
	}
	public String getObjectTypeClass() {
		return objectTypeClass;
	}
	public void setObjectTypeClass(String objectTypeClass) {
		this.objectTypeClass = objectTypeClass;
	}
	@Override
	public Activity newActivity(Map<Object,Object> context) {
		Class<?> targetClass=null;
		ObjectTypeActivity activity=null;
		try {
			if(!objectTypeClass.equals(ObjectTypeNode.NON_CLASS)){
				targetClass = Class.forName(objectTypeClass);	
			}
			activity=new ObjectTypeActivity(targetClass);
		} catch (ClassNotFoundException e) {
			activity=new ObjectTypeActivity(objectTypeClass);
		}
		for(Line line:lines){
			activity.addPath(line.newPath(context));
		}
		return activity;
	}
}
