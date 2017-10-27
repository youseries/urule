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
package com.bstek.urule.model.rule;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2014年12月30日
 */
public class RuleSet {
	private String remark;
	private List<Library> libraries;
	private List<Rule> rules;
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<Library> getLibraries() {
		return libraries;
	}
	public void setLibraries(List<Library> libraries) {
		this.libraries = libraries;
	}
	public void addLibrary(Library library) {
		if(this.libraries==null){
			this.libraries=new ArrayList<Library>();
		}
		this.libraries.add(library);
	}
	public List<Rule> getRules() {
		return rules;
	}
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
}
