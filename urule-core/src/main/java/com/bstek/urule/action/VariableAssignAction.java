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

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.bstek.urule.RuleException;
import com.bstek.urule.Utils;
import com.bstek.urule.debug.MsgType;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.ValueCompute;


/**
 * @author Jacky.gao
 * @since 2014年12月22日
 */
public class VariableAssignAction extends AbstractAction {
	private String referenceName;//for script rule
	private String variableName;
	private String variableLabel;
	private String variableCategory;
	private Datatype datatype;
	private Value value;
	private LeftType type;
	private ActionType actionType=ActionType.VariableAssign;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ActionValue execute(Context context,Object matchedObject,List<Object> allMatchedObjects,Map<String,Object> variableMap) {
		Object targetFact=null;
		String propertyName=null;
		ValueCompute valueCompute=context.getValueCompute();
		Object obj=valueCompute.complexValueCompute(value, matchedObject, context,allMatchedObjects,variableMap);
		String label=null;
		if(type!=null && type.equals(LeftType.NamedReference)){
			String refName=referenceName;
			targetFact=variableMap.get(refName);
			if(targetFact==null){
				refName=refName.substring(1,refName.length());
				targetFact=variableMap.get(refName);
			}
			if(targetFact==null){
				throw new RuleException("Reference ["+referenceName+"] not define.");
			}
			propertyName=variableName;
			label=referenceName+"."+(variableLabel==null ? variableName : variableLabel);
		}else{
			String className=context.getVariableCategoryClass(variableCategory);
			if(className.equals(HashMap.class.getName())){
				targetFact=context.getWorkingMemory().getParameters();				
			}else{
				targetFact=valueCompute.findObject(className, matchedObject, context);				
			}
			if(targetFact==null){
				throw new RuleException("Class["+className+"] not found in workingmemory.");
			}
			if(datatype.equals(Datatype.Enum) && obj!=null && StringUtils.isNotBlank(obj.toString())){
				PropertyDescriptor pd=BeanUtils.getPropertyDescriptor(targetFact.getClass(), variableName);
				Class<Enum> targetClass=(Class<Enum>)pd.getPropertyType();
				obj=Enum.valueOf(targetClass, obj.toString());
			}else if(obj!=null){
				obj=datatype.convert(obj);
			}
			propertyName=variableName;
			label=variableCategory+"."+(variableLabel==null ? variableName : variableLabel);
		}
		Utils.setObjectProperty(targetFact, propertyName, obj);
		if(debug && Utils.isDebug()){
			String msg="###变量赋值："+label+"="+obj;
			context.debugMsg(msg, MsgType.VarAssign, debug);
		}
		return null;
	}

	public LeftType getType() {
		return type;
	}

	public void setType(LeftType type) {
		this.type = type;
	}

	public String getReferenceName() {
		return referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getVariableLabel() {
		return variableLabel;
	}

	public void setVariableLabel(String variableLabel) {
		this.variableLabel = variableLabel;
	}

	public String getVariableCategory() {
		return variableCategory;
	}
	public void setVariableCategory(String variableCategory) {
		this.variableCategory = variableCategory;
	}
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
	
	public Datatype getDatatype() {
		return datatype;
	}

	public void setDatatype(Datatype datatype) {
		this.datatype = datatype;
	}

	public ActionType getActionType() {
		return actionType;
	}
}
