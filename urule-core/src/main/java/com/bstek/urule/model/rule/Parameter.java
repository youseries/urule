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
