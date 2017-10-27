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
package com.bstek.urule.model.library.action;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2014年12月29日
 */
public class Method {
	private String name;
	private String methodName;
	private List<Parameter> parameters;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Parameter> getParameters() {
		return parameters;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public void addParameter(Parameter parameter) {
		if(parameters==null){
			parameters=new ArrayList<Parameter>();
		}
		parameters.add(parameter);
	}
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	
	
}
