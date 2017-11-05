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
import java.util.Collection;

import com.bstek.urule.RuleException;
import com.bstek.urule.Utils;
import com.bstek.urule.model.function.Argument;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.runtime.WorkingMemory;


/**
 * @author Jacky.gao
 * @since 2015年7月22日
 */
public class MaxFunctionDescriptor implements FunctionDescriptor{
	private boolean disabled=false;
	
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	@Override
	public String getLabel() {
		return "求最大值";
	}
	@Override
	public String getName() {
		return "Max";
	}
	@Override
	public Object doFunction(Object object, String property,WorkingMemory workingMemory) {
		Collection<?> list=null;
		if(object instanceof Collection){
			list=(Collection<?>)object;
		}else{
			throw new RuleException("Function[max] parameter must be java.util.Collection type.");
		}
		BigDecimal max=null;
		Object maxObj=null;
		for(Object obj:list){
			Object pvalue=Utils.getObjectProperty(obj, property);
			BigDecimal a=Utils.toBigDecimal(pvalue);
			if(max==null){
				max=a;
				maxObj=obj;
			}else{
				int result=a.compareTo(max);
				if(result==1){
					max=a;
					maxObj=obj;
				}
			}
		}
		return maxObj;
	}
	@Override
	public Argument getArgument() {
		Argument p=new Argument();
		p.setName("集合对象");
		p.setNeedProperty(true);
		return p;
	}

}
