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
