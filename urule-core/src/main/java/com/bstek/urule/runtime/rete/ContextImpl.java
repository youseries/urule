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
package com.bstek.urule.runtime.rete;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

import com.bstek.urule.runtime.ElCalculator;
import com.bstek.urule.runtime.WorkingMemory;
import com.bstek.urule.runtime.assertor.AssertorEvaluator;

/**
 * @author Jacky.gao
 * @since 2015年1月8日
 */
public class ContextImpl implements Context {
	private ApplicationContext applicationContext;
	private AssertorEvaluator assertorEvaluator;
	private Map<String,String> variableCategoryMap;
	private ValueCompute valueCompute;
	private WorkingMemory workingMemory;
	private ElCalculator elCalculator;
	public ContextImpl(WorkingMemory workingMemory,ApplicationContext applicationContext,Map<String,String> variableCategoryMap) {
		this.workingMemory=workingMemory;
		this.elCalculator=new ElCalculator();
		this.applicationContext = applicationContext;
		this.assertorEvaluator=(AssertorEvaluator)applicationContext.getBean(AssertorEvaluator.BEAN_ID);
		this.variableCategoryMap=variableCategoryMap;
		this.valueCompute=(ValueCompute)applicationContext.getBean(ValueCompute.BEAN_ID);
	}
	@Override
	public WorkingMemory getWorkingMemory() {
		return workingMemory;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	public AssertorEvaluator getAssertorEvaluator() {
		return assertorEvaluator;
	}
	
	@Override
	public Object parseExpression(String expression) {
		return elCalculator.eval(expression);
	}
	
	public String getVariableCategoryClass(String variableCategory) {
		String clazz=variableCategoryMap.get(variableCategory);
		if(StringUtils.isEmpty(clazz)){
			//throw new RuleException("Variable category ["+variableCategory+"] not exist.");
			clazz=HashMap.class.getName();
		}
		return clazz;
	}
	public ValueCompute getValueCompute() {
		return valueCompute;
	}
}
