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
