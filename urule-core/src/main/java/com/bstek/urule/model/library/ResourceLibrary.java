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
