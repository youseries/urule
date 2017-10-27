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
package com.bstek.urule.model.rule.lhs;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.bstek.urule.model.rule.Value;

/**
 * @author Jacky.gao
 * @since 2015年7月28日
 */
public class CommonFunctionParameter {
	@JsonIgnore
	private String id;
	private Value objectParameter;
	private String name;
	private String property;
	private String propertyLabel;
	public Value getObjectParameter() {
		return objectParameter;
	}
	public String getId(){
		if(id==null){
			id=objectParameter.getId();
			if(property!=null){
				id+=","+property;
			}
		}
		return id;
	}
	public void setObjectParameter(Value objectParameter) {
		this.objectParameter = objectParameter;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getPropertyLabel() {
		return propertyLabel;
	}
	public void setPropertyLabel(String propertyLabel) {
		this.propertyLabel = propertyLabel;
	}
}
