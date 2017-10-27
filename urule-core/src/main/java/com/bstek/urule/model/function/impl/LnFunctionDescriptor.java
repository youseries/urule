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

import java.math.BigDecimal;

import com.bstek.urule.Utils;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;

/**
 * @author Jacky.gao
 * @since 2015年7月31日
 */
public class LnFunctionDescriptor implements FunctionDescriptor {
	private boolean disabled=false;
	@Override
	public Argument getArgument() {
		Argument arg=new Argument();
		arg.setName("对象");
		arg.setNeedProperty(true);
		return arg;
	}

	@Override
	public Object doFunction(Object object, String property,WorkingMemory workingMemory) {
		Object obj=Utils.getObjectProperty(object, property);
		BigDecimal bigobj=Utils.toBigDecimal(obj);
		return Math.log(bigobj.doubleValue());
	}

	@Override
	public String getName() {
		return "Ln";
	}

	@Override
	public String getLabel() {
		return "求自然对数";
	}

	@Override
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
