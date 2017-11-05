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
/**
 * @author Jacky.gao
 * @since 2014年12月29日
 */
public class SimpleArithmeticValue {
	private String content;
	private SimpleArithmetic arithmetic;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public SimpleArithmetic getArithmetic() {
		return arithmetic;
	}
	public void setArithmetic(SimpleArithmetic arithmetic) {
		this.arithmetic = arithmetic;
	}
	public String getId(){
		String id=content;
		if(arithmetic!=null){
			id+=arithmetic.getId();
		}
		return id;
	}
}
