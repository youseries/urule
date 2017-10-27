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

import com.bstek.urule.model.rule.SimpleArithmetic;

/**
 * @author Jacky.gao
 * @since 2014年12月29日
 */
public class Left {
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
}
