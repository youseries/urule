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
public enum Op {
	Equals{
		@Override
		public String toString() {
			return "等于";
		}
	},EqualsIgnoreCase{
		@Override
		public String toString() {
			return "等于(不分大小写)";
		}
	}, NotEquals{
		@Override
		public String toString() {
			return "不等于";
		}
	}, NotEqualsIgnoreCase{
		@Override
		public String toString() {
			return "不等于(不分大小写)";
		}
	}, LessThen{
		@Override
		public String toString() {
			return "小于";
		}
	}, LessThenEquals{
		@Override
		public String toString() {
			return "小于等于";
		}
	}, GreaterThen{
		@Override
		public String toString() {
			return "大于";
		}
	}, GreaterThenEquals{
		@Override
		public String toString() {
			return "大于等于";
		}
	}, In{
		@Override
		public String toString() {
			return "在集合中";
		}
	}, NotIn{
		@Override
		public String toString() {
			return "不在集合中";
		}
	}, StartWith{
		@Override
		public String toString() {
			return "开始于";
		}
	}, NotStartWith{
		@Override
		public String toString() {
			return "不开始于";
		}
	}, EndWith{
		@Override
		public String toString() {
			return "结束于";
		}
	}, NotEndWith{
		@Override
		public String toString() {
			return "不结束于";
		}
	}, Null{
		@Override
		public String toString() {
			return "为空";
		}
	}, NotNull{
		@Override
		public String toString() {
			return "不为空";
		}
	}, Match{
		@Override
		public String toString() {
			return "匹配";
		}
	}, NotMatch{
		@Override
		public String toString() {
			return "不匹配";
		}
	}, Contain{
		@Override
		public String toString() {
			return "包含";
		}
	}, NotContain{
		@Override
		public String toString() {
			return "不包含";
		}
	};
	
	public static Op parse(String op){
		if(op.equals(">")){
			return GreaterThen;
		}else if(op.equals(">=")){
			return GreaterThenEquals;
		}else if(op.equals("==")){
			return Equals;
		}else if(op.equals("EqualsIgnoreCase")){
			return EqualsIgnoreCase;
		}else if(op.equals("!=")){
			return NotEquals;
		}else if(op.equals("NotEqualsIgnoreCase")){
			return NotEqualsIgnoreCase;
		}else if(op.equals("<")){
			return LessThen;
		}else if(op.equals("<=")){
			return LessThenEquals;
		}else if(op.equals("In")){
			return In;
		}else if(op.equals("NotIn")){
			return NotIn;
		}else if(op.equals("StartWith")){
			return StartWith;
		}else if(op.equals("NotStartWidth")){
			return NotStartWith;
		}else if(op.equals("EndWith")){
			return EndWith;
		}else if(op.equals("NotEndWith")){
			return NotEndWith;
		}else if(op.equals("Null")){
			return Null;
		}else if(op.equals("Notnull")){
			return NotNull;
		}else if(op.equals("Match")){
			return Match;
		}else if(op.equals("NotMatch")){
			return NotMatch;
		}else if(op.equals("Contain")){
			return Contain;
		}else if(op.equals("NotContain")){
			return NotContain;
		}
		throw new RuleException("Unsupport op "+op+"");
	}
}
