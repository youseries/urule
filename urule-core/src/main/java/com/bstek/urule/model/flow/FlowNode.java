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
package com.bstek.urule.model.flow;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

import com.bstek.urule.debug.MsgType;
import com.bstek.urule.model.Node;
import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;
import com.bstek.urule.model.flow.ins.ProcessInstance;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.event.impl.ProcessAfterNodeTriggeredEventImpl;
import com.bstek.urule.runtime.event.impl.ProcessBeforeNodeTriggeredEventImpl;
import com.bstek.urule.runtime.response.ExecutionResponseImpl;

/**
 * @author Jacky.gao
 * @since 2015年1月28日
 */
public abstract class FlowNode implements Node{
	protected String name;
	protected String eventBean;
	protected String x;
	protected String y;
	protected String width;
	protected String height;

	protected List<Connection> connections;
	public FlowNode() {
	}
	public FlowNode(String name) {
		this.name=name;
	}
	public final void enter(FlowContext context,FlowInstance instance){
		String msg=">>>进入决策流节点："+name;
		context.debugMsg(msg, MsgType.RuleFlow, instance.isDebug());
		((ExecutionResponseImpl)context.getResponse()).addNodeName(name);
		KnowledgeSession session=(KnowledgeSession)context.getWorkingMemory();
		session.fireEvent(new ProcessBeforeNodeTriggeredEventImpl(this,instance,session));
		enterNode(context, instance);
		session.fireEvent(new ProcessAfterNodeTriggeredEventImpl(this,instance,session));
	}
	
	public abstract void enterNode(FlowContext context,FlowInstance instance);
	
	protected void leave(String connectionName,FlowContext context, FlowInstance instance) {
		for(Connection connection:connections){
			if(connectionName!=null){
				String cName=connection.getName();
				cName= cName==null ? cName : cName.trim();
				if(connectionName.trim().equals(cName)){
					connection.execute(context, instance);
					break;
				}
			}else if(connection.evaluate(context)){
				connection.execute(context, instance);
				break;
			}
		}
	}
	
	protected void executeNodeEvent(EventType type,FlowContext context,ProcessInstance instance){
		if(StringUtils.isEmpty(eventBean)){
			return;
		}
		ApplicationContext applicationContext=context.getApplicationContext();
		NodeEvent event=(NodeEvent)applicationContext.getBean(eventBean);
		if(type.equals(EventType.enter)){
			event.enter(this, instance, context);
		}else{
			event.leave(this, instance, context);
		}
	}
	
	public abstract FlowNodeType getType();
	
	public List<Connection> getConnections() {
		return connections;
	}
	public void setConnections(List<Connection> connections) {
		this.connections = connections;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEventBean() {
		return eventBean;
	}
	public void setEventBean(String eventBean) {
		this.eventBean = eventBean;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	
}
enum EventType{
	enter,leave;
}
