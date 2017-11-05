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

import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;

/**
 * @author Jacky.gao
 * @since 2015年7月31日
 */
public class UpdateParameterFunctionDescriptor implements FunctionDescriptor {
	@Override
	public Argument getArgument() {
		return null;
	}

	@Override
	public Object doFunction(Object object, String property,WorkingMemory workingMemory) {
		return workingMemory.update(workingMemory.getParameters());
	}

	@Override
	public String getName() {
		return "UpdateParameter";
	}

	@Override
	public String getLabel() {
		return "更新参数";
	}

	@Override
	public boolean isDisabled() {
		return false;
	}
}
