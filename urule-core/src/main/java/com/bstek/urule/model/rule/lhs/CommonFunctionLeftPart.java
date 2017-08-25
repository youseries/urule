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
package com.bstek.urule.model.rule.lhs;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.bstek.urule.Utils;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.EvaluationContext;


/**
 * @author Jacky.gao
 * @since 2015年7月28日
 */
public class CommonFunctionLeftPart implements LeftPart {
	@JsonIgnore
	private String id;
	private String name;
	private String label;
	private CommonFunctionParameter parameter;
	public Object evaluate(EvaluationContext context,Object obj,List<Object> allMatchedObjects){
		FunctionDescriptor function=Utils.findFunctionDescriptor(name);
		Value value=parameter.getObjectParameter();
		Object object=context.getValueCompute().complexValueCompute(value, obj, context, allMatchedObjects,null);
		Argument arg=function.getArgument();
		String property=null;
		if(arg.isNeedProperty()){
			property=parameter.getProperty();
		}
		return function.doFunction(object, property,context.getWorkingMemory());
	}
	@Override
	public String getId() {
		if(id==null){
			id= label+"("+parameter.getId()+")";			
		}
		return id;
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
