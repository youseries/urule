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
package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.model.rule.SimpleArithmetic;

/**
 * @author Jacky.gao
 * @since 2014年12月29日
 */
public class Left {
	private String id;
	private LeftPart leftPart;
	private LeftType type;
	private SimpleArithmetic arithmetic;
	public LeftPart getLeftPart() {
		return leftPart;
	}
	public void setLeftPart(LeftPart leftPart) {
		this.leftPart = leftPart;
	}
	public SimpleArithmetic getArithmetic() {
		return arithmetic;
	}
	public void setArithmetic(SimpleArithmetic arithmetic) {
		this.arithmetic = arithmetic;
	}
	public LeftType getType() {
		return type;
	}
	public void setType(LeftType type) {
		this.type = type;
	}
	public String getId(){
		if(id==null){
			id=leftPart.getId();
			if(arithmetic!=null){
				id=id+arithmetic.getId();
			}
		}
		return id;
	}
}
