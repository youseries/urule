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

import java.util.List;

/**
 * @author Jacky.gao
 * @since 2015年6月1日
 */
public class ExprValue {
	private int total=0;
	private int match=0;
	private int notMatch=0;
	private List<Object> facts;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getMatch() {
		return match;
	}
	public void setMatch(int match) {
		this.match = match;
	}
	public int getNotMatch() {
		return notMatch;
	}
	public void setNotMatch(int notMatch) {
		this.notMatch = notMatch;
	}
	public List<Object> getFacts() {
		return facts;
	}
	public void setFacts(List<Object> facts) {
		this.facts = facts;
	}
}
