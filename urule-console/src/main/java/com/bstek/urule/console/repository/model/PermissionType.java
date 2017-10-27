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

/**
 * @author Jacky.gao
 * @since 2015年5月7日
 */
public enum PermissionType {
	ProjectVisible, NewVar, NewParam, NewConst, NewAction, 
	NewRule, NewDslRule, NewDecisionTable,NewDslDecisionTable,NewRuleFlow,NewDecisionTree,
	
	DelVar,DelParam,DelConst,DelAction,DelRule,DelDslRule,
	DelDecisionTable,DelDslDecisionTable,DelRuleFlow,DelDecisionTree,
	
	ModVar,ModParam,ModConst,ModAction,ModRule,ModDslRule,
	ModDecisionTable,ModDslDecisionTable,ModRuleFlow,ModDecisionTree,
	
	InsertRow, DelRow, InsertConditionCol, ModConditionCol, DelConditionCol,
	InsertActionCol, ModActionCol, DelActionCol,
	
	InsertDslRow, DelDslRow, InsertDslConditionCol, ModDslConditionCol, DelDslConditionCol,
	InsertDslActionCol, ModDslActionCol, DelDslActionCol
}
