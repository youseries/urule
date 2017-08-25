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
package com.bstek.urule.model.library;

import java.util.ArrayList;
import java.util.List;

import com.bstek.urule.RuleException;
import com.bstek.urule.model.library.action.ActionLibrary;
import com.bstek.urule.model.library.constant.ConstantCategory;
import com.bstek.urule.model.library.constant.ConstantLibrary;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.library.variable.VariableLibrary;

/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public class ResourceLibrary {
	private List<ConstantCategory> constantCategories;
	private List<ActionLibrary> actionLibraries;
	private List<VariableCategory> variableCategories;
	public ResourceLibrary() {
	}
	public ResourceLibrary(List<VariableLibrary> variableLibraries,List<ActionLibrary> actionLibraries,List<ConstantLibrary> constantLibraries){
		this.variableCategories=new ArrayList<VariableCategory>();
		this.actionLibraries=new ArrayList<ActionLibrary>();
		this.constantCategories=new ArrayList<ConstantCategory>();
		for(VariableLibrary vl:variableLibraries){
			for(VariableCategory category:vl.getVariableCategories()){
				variableCategories.add(category);
			}
		}
		this.actionLibraries.addAll(actionLibraries);
		for(ConstantLibrary cl:constantLibraries){
			for(ConstantCategory cc:cl.getCategories()){
				this.constantCategories.add(cc);
			}
		}
	}
	public VariableCategory getVariableCategory(String categoryName) {
		for(VariableCategory category:variableCategories){
			if(category.getName().equals(categoryName)){
				return category;
			}
		}
		throw new RuleException("Variable category ["+categoryName+"] not exist");
	}
	public List<ActionLibrary> getActionLibraries() {
		return actionLibraries;
	}
	public List<VariableCategory> getVariableCategories() {
		return variableCategories;
	}
	public List<ConstantCategory> getConstantCategories() {
		return constantCategories;
	}
}
