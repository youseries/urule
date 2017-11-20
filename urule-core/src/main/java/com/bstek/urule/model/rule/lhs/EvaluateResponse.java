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

/**
 * @author Jacky.gao
 * @since 2017年11月17日
 */
public class EvaluateResponse {
	private boolean result;
	private Object leftResult;
	private Object rightResult;
	
	public void setLeftResult(Object leftResult) {
		this.leftResult = leftResult;
	}
	public void setRightResult(Object rightResult) {
		this.rightResult = rightResult;
	}
	public Object getLeftResult() {
		return leftResult;
	}
	public Object getRightResult() {
		return rightResult;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public boolean getResult() {
		return result;
	}
}
