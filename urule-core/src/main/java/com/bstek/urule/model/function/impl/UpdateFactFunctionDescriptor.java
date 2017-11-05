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
package com.bstek.urule.model.function.impl;

import com.bstek.urule.RuleException;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;

/**
 * @author Jacky.gao
 * @since 2015年7月31日
 */
public class UpdateFactFunctionDescriptor implements FunctionDescriptor {
	private boolean disabled=false;
	@Override
	public Argument getArgument() {
		Argument arg=new Argument();
		arg.setName("要更新的对象");
		return arg;
	}

	@Override
	public Object doFunction(Object object, String property,WorkingMemory workingMemory) {
		if(object instanceof String){
			String text=(String)object;
			if(text.equals("参数") || text.equals("parameter")){
				return workingMemory.update(workingMemory.getParameters());
			}else{
				throw new RuleException("Unsupport parameter["+text+"].");
			}
		}else{
			return workingMemory.update(object);
		}
	}

	@Override
	public String getName() {
		return "UpdateFact";
	}

	@Override
	public String getLabel() {
		return "更新工作区对象";
	}

	@Override
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
