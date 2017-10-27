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
package com.bstek.urule.model.rete.builder;

import java.util.List;

import com.bstek.urule.RuleException;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rete.ObjectTypeNode;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.ParameterValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.VariableCategoryValue;
import com.bstek.urule.model.rule.VariableValue;
import com.bstek.urule.model.rule.lhs.AbstractLeftPart;
import com.bstek.urule.model.rule.lhs.BaseCriteria;
import com.bstek.urule.model.rule.lhs.CommonFunctionLeftPart;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.FunctionLeftPart;
import com.bstek.urule.model.rule.lhs.LeftPart;
import com.bstek.urule.model.rule.lhs.MethodLeftPart;
import com.bstek.urule.model.rule.lhs.NamedCriteria;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;

/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public class BuildContextImpl implements BuildContext {
	private ResourceLibrary resourceLibrary;
	private List<ObjectTypeNode> objectTypeNodes;
	private int id=0;
	public BuildContextImpl(ResourceLibrary resourceLibrary,List<ObjectTypeNode> objectTypeNodes) {
		this.resourceLibrary = resourceLibrary;
		this.objectTypeNodes = objectTypeNodes;
	}
	
	public boolean assertSameType(BaseCriteria left, BaseCriteria right) {
		VariableCategory leftCategory = fetchVariableCategory(left);
		VariableCategory rightCategory = fetchVariableCategory(right);
		if(leftCategory==null || rightCategory==null){
			return false;
		}
		return leftCategory.getClazz().equals(rightCategory.getClazz());
	}

	private VariableCategory fetchVariableCategory(BaseCriteria criteria) {
		VariableCategory vc=null;
		if(criteria instanceof Criteria){
			Criteria c=(Criteria)criteria;
			LeftPart leftLeftPart=c.getLeft().getLeftPart();
			if(leftLeftPart instanceof VariableLeftPart){
				VariableLeftPart part=(VariableLeftPart)leftLeftPart;
				vc=resourceLibrary.getVariableCategory(part.getVariableCategory());
			}
		}else if(criteria instanceof NamedCriteria){
			NamedCriteria c=(NamedCriteria)criteria;
			vc=resourceLibrary.getVariableCategory(c.getVariableCategory());
		}else{
			throw new RuleException("Unknow Criteria : "+criteria);
		}
		return vc;
	}
	
	public ObjectTypeNode getObjectTypeNode(BaseCriteria criteria) {
		String objectType=getObjectType(criteria);
		return buildObjectTypeNode(objectType);
	}
	
	@Override
	public String getObjectType(BaseCriteria criteria) {
		String className=null;
		if(criteria instanceof Criteria){
			Criteria c=(Criteria)criteria;
			LeftPart leftPart=c.getLeft().getLeftPart();
			if(leftPart instanceof VariableLeftPart){
				VariableLeftPart varPart=(VariableLeftPart)leftPart;
				String variableCategory=varPart.getVariableCategory();
				VariableCategory category=resourceLibrary.getVariableCategory(variableCategory);
				className=category.getClazz();
			}else if(leftPart instanceof AbstractLeftPart){
				AbstractLeftPart lp=(AbstractLeftPart)leftPart;
				VariableCategory category=resourceLibrary.getVariableCategory(lp.getVariableCategory());
				className=category.getClazz();
			}else if(leftPart instanceof CommonFunctionLeftPart){
				CommonFunctionLeftPart funPart=(CommonFunctionLeftPart)leftPart;
				CommonFunctionParameter parameter=funPart.getParameter();
				Value value=parameter.getObjectParameter();
				className = buildValueClass(value);
			}else if(leftPart instanceof MethodLeftPart){
				MethodLeftPart methodLeftPart=(MethodLeftPart)leftPart;
				List<Parameter> params=methodLeftPart.getParameters();
				if(params!=null && params.size()>0){
					Parameter param=params.get(0);
					Value value=param.getValue();
					className = buildValueClass(value);
				}else{
					className=ObjectTypeNode.NON_CLASS;
				}
			}else if(leftPart instanceof FunctionLeftPart){
				FunctionLeftPart funLeftPart=(FunctionLeftPart)leftPart;
				List<Parameter> params=funLeftPart.getParameters();
				if(params!=null && params.size()>0){
					Parameter param=params.get(0);
					Value value=param.getValue();
					className = buildValueClass(value);
				}else{
					className=ObjectTypeNode.NON_CLASS;
				}
			}else{
				className=ObjectTypeNode.NON_CLASS;
			}
		}else if(criteria instanceof NamedCriteria){
			NamedCriteria c=(NamedCriteria)criteria;
			VariableCategory category=resourceLibrary.getVariableCategory(c.getVariableCategory());
			className=category.getClazz();
		}else{
			throw new RuleException("Unknow Criteria : "+criteria);
		}
		return className;
	}

	private String buildValueClass(Value value) {
		String className;
		if(value instanceof VariableValue){
			VariableValue varValue=(VariableValue)value;
			String variableCategory=varValue.getVariableCategory();
			VariableCategory category=resourceLibrary.getVariableCategory(variableCategory);
			className=category.getClazz();
		}else if(value instanceof ParameterValue){
			VariableCategory category=resourceLibrary.getVariableCategory(VariableCategory.PARAM_CATEGORY);
			className=category.getClazz();
		}else if(value instanceof VariableCategoryValue){
			VariableCategoryValue categoryValue=(VariableCategoryValue)value;
			VariableCategory category=resourceLibrary.getVariableCategory(categoryValue.getVariableCategory());
			className=category.getClazz();
		}else{
			className=ObjectTypeNode.NON_CLASS;
		}
		return className;
	}
	
	@Override
	public ObjectTypeNode buildObjectTypeNode(String className){
		ObjectTypeNode targetObjectTypeNode=null;
		for(ObjectTypeNode typeNode:objectTypeNodes){
			if(typeNode.support(className)){
				targetObjectTypeNode=typeNode;
				break;
			}
		}
		if(targetObjectTypeNode==null){
			targetObjectTypeNode=new ObjectTypeNode(className,nextId());
			objectTypeNodes.add(targetObjectTypeNode);
		}
		return targetObjectTypeNode;
	}

	@Override
	public ResourceLibrary getResourceLibrary() {
		return resourceLibrary;
	}
	
	@Override
	public int nextId() {
		return id++;
	}
}
