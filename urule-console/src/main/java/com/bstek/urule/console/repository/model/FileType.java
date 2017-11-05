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
package com.bstek.urule.console.repository.model;

import com.bstek.urule.RuleException;
import com.bstek.urule.dsl.Constant;

/**
 * @author Jacky.gao
 * @since 2014年12月24日
 */
public enum FileType {
	Ruleset{
		@Override
		public String toString() {
			return "rs.xml";
		}
	},DecisionTable{
		@Override
		public String toString() {
			return "dt.xml";
		}
	},ScriptDecisionTable{
		@Override
		public String toString() {
			return "dts.xml";
		}
	},ActionLibrary{
		@Override
		public String toString() {
			return "al.xml";
		}
	},VariableLibrary{
		@Override
		public String toString() {
			return "vl.xml";
		}
	},ParameterLibrary{
		@Override
		public String toString() {
			return "pl.xml";
		}
	},ConstantLibrary{
		@Override
		public String toString() {
			return "cl.xml";
		}
	},RuleFlow{
		@Override
		public String toString() {
			return "rl.xml";
		}
	},UL{
		@Override
		public String toString() {
			return Constant.UL_SUFFIX;
		}
	},DecisionTree{
		@Override
		public String toString() {
			return "dtree.xml";
		}
	},Scorecard{
		@Override
		public String toString() {
			return "sc";
		}
	},DIR{
		@Override
		public String toString() {
			return "DIR";
		}
	};
	
	public static FileType parse(String type){
		if(type.equals("rs.xml")){
			return FileType.Ruleset;
		}else if(type.equals("dt.xml")){
			return FileType.DecisionTable;
		}else if(type.equals("dts.xml")){
			return FileType.ScriptDecisionTable;
		}else if(type.equals("al.xml")){
			return FileType.ActionLibrary;
		}else if(type.equals("vl.xml")){
			return FileType.VariableLibrary;
		}else if(type.equals("pl.xml")){
			return FileType.ParameterLibrary;
		}else if(type.equals("cl.xml")){
			return FileType.ConstantLibrary;
		}else if(type.equals("rl.xml")){
			return FileType.RuleFlow;
		}else if(type.equals("ul")){
			return FileType.UL;
		}else if(type.equals("dtree.xml")){
			return FileType.DecisionTree;
		}else if(type.equals("sc")){
			return FileType.Scorecard;
		}else if(type.equals("DIR")){
			return FileType.DIR;
		}else{
			throw new RuleException("Unknow type:"+type);
		}
	}
}
