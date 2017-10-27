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
package com.bstek.urule.action;

import java.util.List;
import java.util.Map;

import com.bstek.urule.RuleException;
import com.bstek.urule.Utils;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import com.bstek.urule.runtime.rete.Context;

/**
 * @author Jacky.gao
 * @since 2015年7月31日
 */
public class ExecuteCommonFunctionAction extends AbstractAction{
	private String name;
	private String label;
	private CommonFunctionParameter parameter;
	@Override
	public ActionValue execute(Context context, Object matchedObject,List<Object> allMatchedObjects,Map<String,Object> variableMap) {
		FunctionDescriptor function=null;
		if(Utils.getFunctionDescriptorMap().containsKey(name)){
			function=Utils.findFunctionDescriptor(name);
		}else if(Utils.getFunctionDescriptorLabelMap().containsKey(label)){
			function=Utils.getFunctionDescriptorLabelMap().get(label);
		}
		if(function==null){
			throw new RuleException("Function["+name+"] not exist.");
		}
		Value value=null;
		Object object=null;
		if(parameter!=null){
			value=parameter.getObjectParameter();			
			object=context.getValueCompute().complexValueCompute(value, matchedObject, context, allMatchedObjects,variableMap);
		}
		String property=null;
		if(function.getArgument()!=null && function.getArgument().isNeedProperty()){
			property=parameter.getProperty();
		}
		Object result=function.doFunction(object, property,context.getWorkingMemory());
		if(result!=null){
			return new ActionValueImpl(name,result);					
		}else{
			return null;
		}
	}

	@Override
	public ActionType getActionType() {
		return ActionType.ExecuteCommonFunction;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public CommonFunctionParameter getParameter() {
		return parameter;
	}

	public void setParameter(CommonFunctionParameter parameter) {
		this.parameter = parameter;
	}
}
