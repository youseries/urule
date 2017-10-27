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
package com.bstek.urule.model.flow;

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;

/**
 * @author Jacky.gao
 * @since 2015年4月20日
 */
public class RulePackageNode extends BindingNode {
	private FlowNodeType type=FlowNodeType.RulePackage;
	private String packageId;
	private String project;
	public RulePackageNode() {
	}
	public RulePackageNode(String name) {
		super(name);
	}
	
	@Override
	public FlowNodeType getType() {
		return type;
	}
	@Override
	public void enterNode(FlowContext context, FlowInstance instance) {
		instance.setCurrentNode(this);
		executeNodeEvent(EventType.enter, context, instance);
		executeKnowledgePackage(context, instance);
		executeNodeEvent(EventType.leave, context, instance);
		leave(null, context, instance);
	}

	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
}
