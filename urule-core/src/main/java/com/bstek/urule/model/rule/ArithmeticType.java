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
