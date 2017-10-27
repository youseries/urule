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
