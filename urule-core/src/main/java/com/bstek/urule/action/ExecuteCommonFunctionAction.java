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
package com.bstek.urule.action;

import java.util.List;
import java.util.Map;

import com.bstek.urule.RuleException;
import com.bstek.urule.Utils;
import com.bstek.urule.debug.MsgType;
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
		String info=(label==null)?name:label;
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
		if(debug && Utils.isDebug()){
			info=info+(object==null ? "" : object);
			String msg="***执行函数："+info;
			context.debugMsg(msg, MsgType.ExecuteFunction, debug);
		}
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
