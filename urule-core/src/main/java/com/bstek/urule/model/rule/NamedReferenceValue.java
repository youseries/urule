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

import com.bstek.urule.model.library.Datatype;

/**
 * @author Jacky.gao
 * @since 2016年8月16日
 */
public class NamedReferenceValue extends AbstractValue{
	private String referenceName;
	private String propertyLabel;
	private String propertyName;
	private Datatype datatype;
	
	@Override
	public String getId() {
		String id="[REF]"+referenceName+"."+propertyLabel;
		if(arithmetic!=null){
			id+=arithmetic.getId();
		}
		return id;
	}
	@Override
	public ValueType getValueType() {
		return ValueType.NamedReference;
	}
	public String getReferenceName() {
		return referenceName;
	}
	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}
	public String getPropertyLabel() {
		return propertyLabel;
	}
	public void setPropertyLabel(String propertyLabel) {
		this.propertyLabel = propertyLabel;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public Datatype getDatatype() {
		return datatype;
	}
	public void setDatatype(Datatype datatype) {
		this.datatype = datatype;
	}
}
