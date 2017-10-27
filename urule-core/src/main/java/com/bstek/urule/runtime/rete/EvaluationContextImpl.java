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

import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.bstek.urule.runtime.WorkingMemory;

/**
 * @author Jacky.gao
 * @since 2015年3月9日
 */
public class EvaluationContextImpl extends ContextImpl implements EvaluationContext {
	private Activity prevActivity;
	public EvaluationContextImpl(WorkingMemory workingMemory,
			ApplicationContext applicationContext, 
			Map<String, String> variableCategoryMap) {
		super(workingMemory, applicationContext, variableCategoryMap);
	}
	@Override
	public Activity getPrevActivity() {
		return prevActivity;
	}
	@Override
	public void setPrevActivity(Activity activity) {
		this.prevActivity=activity;
	}
}
