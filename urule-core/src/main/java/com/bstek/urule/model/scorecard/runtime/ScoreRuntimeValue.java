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
package com.bstek.urule.model.scorecard.runtime;
/**
 * @author Jacky.gao
 * @since 2016年9月26日
 */
public class ScoreRuntimeValue {
	public static final String SCORE_VALUE="scoring_value";
	private int rowNumber;
	private String name;
	private String weight;
	private Object value;
	public ScoreRuntimeValue(int rowNumber,String name,String weight,Object value) {
		this.rowNumber=rowNumber;
		this.name = name;
		this.weight=weight;
		this.value=value;
	}
	public int getRowNumber() {
		return rowNumber;
	}
	public String getName() {
		return name;
	}
	
	public String getWeight() {
		return weight;
	}
	
	public Object getValue() {
		return value;
	}
}
