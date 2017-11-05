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
