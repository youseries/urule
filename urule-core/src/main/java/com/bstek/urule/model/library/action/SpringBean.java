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
public class SpringBean {
	private String id;
	private String name;
	private List<Method> methods;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Method> getMethods() {
		return methods;
	}
	public void addMethod(Method method) {
		if(methods==null){
			methods=new ArrayList<Method>();
		}
		this.methods.add(method);
	}
	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}
	
	
}
