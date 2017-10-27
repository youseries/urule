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
package com.bstek.urule.console.repository.updater;

import java.io.IOException;
import java.util.List;

import com.bstek.urule.RuleException;
import com.bstek.urule.console.repository.model.FileType;
import com.bstek.urule.dsl.DSLRuleSetBuilder;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.RuleSet;

/**
 * @author Jacky.gao
 * @since 2015年8月4日
 */
public class ScriptRuleSetReferenceUpdater extends AbstractReferenceUpdater {
	private DSLRuleSetBuilder builder;
	public boolean contain(String path, String xml) {
		try {
			RuleSet rs=builder.build(xml);
			List<Library> libs=rs.getLibraries();
			if(libs!=null){
				for(Library lib:libs){
					String libpath=lib.getPath();
					if(libpath.indexOf(path)!=-1){
						return true;
					}
				}
			}
			return false;
		} catch (IOException e) {
			throw new RuleException(e);
		}
	}
	public String update(String path, String newPath, String xml) {
		return null;
	}
	public void setBuilder(DSLRuleSetBuilder builder) {
		this.builder = builder;
	}
	public boolean support(String path) {
		return path.endsWith(FileType.UL.toString());
	}
}
