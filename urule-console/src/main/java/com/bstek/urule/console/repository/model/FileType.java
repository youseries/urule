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
