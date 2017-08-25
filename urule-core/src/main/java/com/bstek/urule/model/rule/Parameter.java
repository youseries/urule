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
package com.bstek.urule.model.rule;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.bstek.urule.RuleException;
import com.bstek.urule.model.library.Datatype;

/**
 * @author Jacky.gao
 * @since 2014年12月31日
 */
public class Parameter {
	@JsonIgnore
	private String id;
	private String name;
	private Datatype type;
	private Value value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Datatype getType() {
		return type;
	}
	public void setType(Datatype type) {
		this.type = type;
	}
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
	public String getId() {
		if(id==null){
			if(value==null){
				throw new RuleException("Parameter ["+name+"] not assignment value.");
			}
			id=value.getId();
		}
		return id;
	}
}
