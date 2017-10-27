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
