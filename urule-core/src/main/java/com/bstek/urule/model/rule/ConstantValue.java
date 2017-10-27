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
/**
 * @author Jacky.gao
 * @since 2014年12月29日
 */
public class ConstantValue extends AbstractValue{
	private String constantName;
	private String constantLabel;
	private String constantCategory;
	private ValueType valueType=ValueType.Constant;
	public String getConstantName() {
		return constantName;
	}
	public void setConstantName(String constantName) {
		this.constantName = constantName;
	}
	public String getConstantLabel() {
		return constantLabel;
	}
	public void setConstantLabel(String constantLabel) {
		this.constantLabel = constantLabel;
	}
	public String getConstantCategory() {
		return constantCategory;
	}
	public void setConstantCategory(String constantCategory) {
		this.constantCategory = constantCategory;
	}
	public ValueType getValueType() {
		return valueType;
	}
	public String getId() {
		String id="[CONST]"+constantCategory+"."+constantLabel;
		if(arithmetic!=null){
			id+=arithmetic.getId();
		}
		return id;
	}
}
