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
package com.bstek.urule.model.decisiontree;

import java.util.List;

import com.bstek.urule.model.rule.Op;
import com.bstek.urule.model.rule.Value;

/**
 * @author Jacky.gao
 * @since 2016年2月26日
 */
public class ConditionTreeNode extends TreeNode{
	private Value value;
	private Op op;
	private List<ConditionTreeNode> conditionTreeNodes;
	private List<VariableTreeNode> variableTreeNodes;
	private List<ActionTreeNode> actionTreeNodes;
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
	public Op getOp() {
		return op;
	}
	public void setOp(Op op) {
		this.op = op;
	}
	public List<ConditionTreeNode> getConditionTreeNodes() {
		return conditionTreeNodes;
	}
	public void setConditionTreeNodes(List<ConditionTreeNode> conditionTreeNodes) {
		this.conditionTreeNodes = conditionTreeNodes;
	}
	public List<VariableTreeNode> getVariableTreeNodes() {
		return variableTreeNodes;
	}
	public void setVariableTreeNodes(List<VariableTreeNode> variableTreeNodes) {
		this.variableTreeNodes = variableTreeNodes;
	}
	public List<ActionTreeNode> getActionTreeNodes() {
		return actionTreeNodes;
	}
	public void setActionTreeNodes(List<ActionTreeNode> actionTreeNodes) {
		this.actionTreeNodes = actionTreeNodes;
	}
}
