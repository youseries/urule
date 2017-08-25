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
package com.bstek.urule.model.decisiontree;

import java.util.List;

import com.bstek.urule.model.rule.lhs.Left;

/**
 * @author Jacky.gao
 * @since 2016年2月26日
 */
public class VariableTreeNode extends TreeNode{
	private Left left;
	private List<ConditionTreeNode> conditionTreeNodes;
	public Left getLeft() {
		return left;
	}
	public void setLeft(Left left) {
		this.left = left;
	}
	public List<ConditionTreeNode> getConditionTreeNodes() {
		return conditionTreeNodes;
	}
	public void setConditionTreeNodes(List<ConditionTreeNode> conditionTreeNodes) {
		this.conditionTreeNodes = conditionTreeNodes;
	}
}
