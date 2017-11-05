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
