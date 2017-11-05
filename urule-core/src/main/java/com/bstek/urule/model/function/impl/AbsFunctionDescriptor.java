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

import java.math.BigDecimal;

import com.bstek.urule.Utils;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;


/**
 * @author Jacky.gao
 * @since 2015年7月22日
 */
public class AbsFunctionDescriptor implements FunctionDescriptor{
	private boolean disabled=false;
	
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	@Override
	public String getLabel() {
		return "求绝对值";
	}
	@Override
	public String getName() {
		return "Abs";
	}
	@Override
	public Object doFunction(Object object, String property,WorkingMemory workingMemory) {
		Object value=Utils.getObjectProperty(object, property);
		BigDecimal bigvalue=Utils.toBigDecimal(value);
		return Math.abs(bigvalue.doubleValue());
	}
	@Override
	public Argument getArgument() {
		Argument p=new Argument();
		p.setName("对象");
		p.setNeedProperty(true);
		return p;
	}
}
