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
package com.bstek.urule.model.library.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class ConstantCategory {
	private String name;
	private String label;
	private List<Constant> constants;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<Constant> getConstants() {
		return constants;
	}
	public void setConstants(List<Constant> constants) {
		this.constants = constants;
	}
	public void addConstant(Constant constant) {
		if(this.constants==null){
			this.constants=new ArrayList<Constant>();
		}
		this.constants.add(constant);
	}
}
