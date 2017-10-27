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

import com.bstek.urule.RuleException;

/**
 * @author Jacky.gao
 * @since 2014年12月25日
 */
public enum ArithmeticType {
	Add{
		@Override
		public String toString() {
			return "+";
		}
	},
	Sub{
		@Override
		public String toString() {
			return "-";
		}
	},
	Mul{
		@Override
		public String toString() {
			return "*";
		}
	},
	Div{
		@Override
		public String toString() {
			return "/";
		}
	},
	Mod{
		@Override
		public String toString() {
			return "%";
		}
	};
	public static ArithmeticType parse(String type){
		if(type.equals("+")){
			return Add;
		}else if(type.equals("-")){
			return Sub;
		}else if(type.equals("*")){
			return Mul;
		}else if(type.equals("/")){
			return Div;
		}else if(type.equals("%")){
			return Mod;
		}
		throw new RuleException("Unsupport arithmetic type ["+type+"]");
	}
}
