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
package com.bstek.urule.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.bstek.urule.RuleException;
import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rete.Line;
import com.bstek.urule.model.rete.ObjectTypeNode;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rete.ReteNode;

/**
 * @author Jacky.gao
 * @since 2015年3月6日
 */
public class KnowledgePackageWrapper {
	@JsonDeserialize(as=KnowledgePackageImpl.class)
	private KnowledgePackage knowledgePackage;
	@JsonDeserialize(using=com.bstek.urule.model.rete.ReteNodeJsonDeserializer.class)
	private List<ReteNode> allNodes=new ArrayList<ReteNode>();
	public KnowledgePackageWrapper() {
	}
	public KnowledgePackageWrapper(KnowledgePackage knowledgePackage) {
		this.knowledgePackage=knowledgePackage;
		initNodes();
	}
	
	private void initNodes(){
		Rete rete=knowledgePackage.getRete();
		List<ObjectTypeNode> typeNodes=rete.getObjectTypeNodes();
		List<ReteNode> childreNodes=new ArrayList<ReteNode>();
		childreNodes.addAll(typeNodes);
		queryReteNodes(childreNodes);
	}
	private void queryReteNodes(List<ReteNode> reteNodes){
		if(reteNodes==null){
			return;
		}
		for(ReteNode reteNode:reteNodes){
			if(!allNodes.contains(reteNode) && !(reteNode instanceof ObjectTypeNode)){
				allNodes.add(reteNode);
			}
			if(reteNode instanceof BaseReteNode){
				BaseReteNode abstractReteNode=(BaseReteNode)reteNode;
				queryReteNodes(abstractReteNode.getChildrenNodes());
			}
		}
	}
	
	public void buildDeserialize(){
		Rete rete=knowledgePackage.getRete();
		List<ObjectTypeNode> typeNodes=rete.getObjectTypeNodes();
		for(ObjectTypeNode typeNode:typeNodes){
			List<Line> lines=typeNode.getLines();
			for(Line line:lines){
				line.setFrom(typeNode);
			}
			rebuildLine(lines, allNodes);
		}
		((KnowledgePackageImpl)knowledgePackage).buildWithElseRules();
		
		Map<String, FlowDefinition> flowMap=knowledgePackage.getFlowMap();
		if(flowMap!=null && flowMap.size()>0){
			for(FlowDefinition fd:flowMap.values()){
				fd.buildConnectionToNode();
			}
		}
	}
	
	private void rebuildLine(List<Line> lines,List<ReteNode> reteNodes){
		if(lines==null){
			return;
		}
		for(Line line:lines){
			if(line.getFrom()==null){
				int fromId=line.getFromNodeId();
				ReteNode fromNode=findTargetNode(reteNodes, fromId);
				line.setFrom(fromNode);
				if(fromNode instanceof BaseReteNode){
					BaseReteNode node=(BaseReteNode)fromNode;
					rebuildLine(node.getLines(), reteNodes);
				}
			}
			if(line.getTo()==null){
				int toId=line.getToNodeId();
				ReteNode toNode=findTargetNode(reteNodes, toId);
				line.setTo(toNode);
				if(toNode instanceof BaseReteNode){
					BaseReteNode node=(BaseReteNode)toNode;
					rebuildLine(node.getLines(), reteNodes);
				}
			}
		}
	}
	private ReteNode findTargetNode(List<ReteNode> reteNodes,int id){
		for(ReteNode node:reteNodes){
			if(node.getId()==id){
				return node;
			}
		}
		throw new RuleException("Node["+id+"] not exist.");
	}
	
	public List<ReteNode> getAllNodes() {
		return allNodes;
	}
	public KnowledgePackage getKnowledgePackage() {
		return knowledgePackage;
	}
}
