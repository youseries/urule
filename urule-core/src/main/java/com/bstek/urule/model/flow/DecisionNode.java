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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.LibraryType;
import com.bstek.urule.runtime.KnowledgeSession;

/**
 * @author Jacky.gao
 * @since 2015年4月20日
 */
public class DecisionNode extends BindingNode {
	private final Logger log=Logger.getLogger(DecisionNode.class.getName());
	private List<DecisionItem> items;
	private FlowNodeType type=FlowNodeType.Decision;
	private DecisionType decisionType=DecisionType.Criteria;
	public DecisionNode() {
	}
	public DecisionNode(String name) {
		super(name);
	}
	@Override
	public void enterNode(FlowContext context,FlowInstance instance) {
		instance.setCurrentNode(this);
		if(decisionType.equals(DecisionType.Criteria)){
			doCriteria(context, instance);
		}else{
			doPercent(context, instance);
		}
		executeNodeEvent(EventType.enter, context, instance);
	}
	private void doPercent(FlowContext context,FlowInstance instance){
		String nodeKey=instance.getProcessDefinition().getId()+"_"+getName();
		long total=getAmount(nodeKey, context)+1;
		List<PercentItem> percentItems=new ArrayList<PercentItem>();
		for(DecisionItem item:items){
			PercentItem percent=new PercentItem();
			percent.setName(item.getTo());
			percent.setPercent(item.getPercent());
			String itemKey=nodeKey+"."+item.getTo();
			long itemTotal=getAmount(itemKey, context);
			percent.setTotal(itemTotal);
			percentItems.add(percent);
		}
		PercentItem percentItem=computePercent(percentItems, total);
		setAmount(nodeKey, total, context);
		setAmount(nodeKey+"."+percentItem.getName(), percentItem.getTotal()+1, context);
		executeNodeEvent(EventType.leave, context, instance);
		leave(percentItem.getName(), context, instance);
	}
	
	private long getAmount(String key,FlowContext context){
		Object value=context.getSessionValue(key);
		if(value==null){
			return 0;
		}
		return (Long)value;
	}
	
	private void setAmount(String key,long value,FlowContext context){
		context.setSessionValue(key, value);
	}
	
	private void doCriteria(FlowContext context,FlowInstance instance){
		KnowledgeSession session=executeKnowledgePackage(context, instance);
		executeNodeEvent(EventType.leave, context, instance);
		Object to=session.getParameter(DecisionItem.RETURN_VALUE_KEY);
		if(to==null){
			log.info("Decision node ["+getName()+"] no matching conditions.");
			return;
		}
		session.getParameters().remove(DecisionItem.RETURN_VALUE_KEY);
		leave(to.toString(), context, instance);
	}
	private PercentItem computePercent(List<PercentItem> items,long total){
		BigDecimal totalValue=new BigDecimal(total);
		for(PercentItem item:items){
			long itemTotal=item.getTotal();
			BigDecimal left=new BigDecimal(itemTotal);
			BigDecimal newPercent=left.divide(totalValue,20,BigDecimal.ROUND_HALF_EVEN);
			BigDecimal defaultPercent=new BigDecimal(item.getPercent());
			defaultPercent=defaultPercent.divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_EVEN);
			int result=newPercent.compareTo(defaultPercent);
			if(result==-1){
				return item;
			}
		}
		return items.get(0);
	}
	
	@Override
	public FlowNodeType getType() {
		return type;
	}
	public List<DecisionItem> getItems() {
		return items;
	}
	public void setItems(List<DecisionItem> items) {
		this.items = items;
	}
	public String buildDSLScript(List<Library> libraries){
		StringBuffer sb=new StringBuffer();
		if(libraries!=null){
			for(Library lib:libraries){
				String path=lib.getPath();
				if(lib.getVersion()!=null){
					path+=":"+lib.getVersion();
				}
				LibraryType type=lib.getType();
				switch(type){
				case Action:
					sb.append("importActionLibrary \""+path+"\"");
					sb.append("\r\n");
					break;
				case Constant:
					sb.append("importConstantLibrary \""+path+"\"");
					sb.append("\r\n");
					break;
				case Parameter:
					sb.append("importParameterLibrary \""+path+"\"");
					sb.append("\r\n");
					break;
				case Variable:
					sb.append("importVariableLibrary \""+path+"\"");
					sb.append("\r\n");
					break;
				}
			}
		}
		for(int i=0;i<items.size();i++){
			DecisionItem item=items.get(i);
			sb.append(item.buildDSLScript(i));
			sb.append("\r\n");
		}
		return sb.toString();
	}
	public DecisionType getDecisionType() {
		return decisionType;
	}
	public void setDecisionType(DecisionType decisionType) {
		this.decisionType = decisionType;
	}
}
