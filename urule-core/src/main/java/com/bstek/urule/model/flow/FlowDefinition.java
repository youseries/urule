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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.bstek.urule.RuleException;
import com.bstek.urule.builder.KnowledgeBase;
import com.bstek.urule.builder.KnowledgeBuilder;
import com.bstek.urule.builder.ResourceBase;
import com.bstek.urule.dsl.DSLRuleSetBuilder;
import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;
import com.bstek.urule.model.flow.ins.ProcessInstance;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.event.impl.ProcessBeforeCompletedEventImpl;
import com.bstek.urule.runtime.event.impl.ProcessBeforeStartedEventImpl;
import com.bstek.urule.runtime.response.ExecutionResponseImpl;
import com.bstek.urule.runtime.service.KnowledgePackageService;

/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public class FlowDefinition implements ProcessDefinition {
	private String id;
	private boolean debug;
	@JsonIgnore
	private List<Library> libraries;
	@JsonDeserialize(using=com.bstek.urule.model.flow.FlowNodeJsonDeserializer.class)
	private List<FlowNode> nodes;
	public ProcessInstance newInstance(FlowContext context){
		ExecutionResponseImpl response=(ExecutionResponseImpl)context.getResponse();
		response.setFlowId(id);
		StartNode startNode=null;
		for(FlowNode node:nodes){
			if(node instanceof StartNode){
				startNode=(StartNode)node;
				break;
			}
		}
		if(startNode==null){
			throw new RuleException("StartNode must be define.");
		}
		response.addNodeName(startNode.getName());
		FlowInstance instance=new FlowInstance(this,debug);
		KnowledgeSession session=(KnowledgeSession)context.getWorkingMemory();
		session.fireEvent(new ProcessBeforeStartedEventImpl(instance,session));
		startNode.enter(context, instance);
		session.fireEvent(new ProcessBeforeCompletedEventImpl(instance,session));
		return instance;
	}
	
	public void buildConnectionToNode(){
		for(FlowNode node:nodes){
			List<Connection> connections=node.getConnections();
			if(connections==null || connections.size()==0){
				continue;
			}
			for(Connection conn:connections){
				String nodeName=conn.getToName();
				conn.setTo(getFlowNode(nodeName));
			}
		}
	}
	
	private FlowNode getFlowNode(String nodeName){
		for(FlowNode node:nodes){
			if(node.getName().equals(nodeName)){
				return node;
			}
		}
		throw new RuleException("Flow node ["+nodeName+"] not found.");
	}
	
	public void initNodeKnowledgePackage(KnowledgeBuilder knowledgeBuilder,KnowledgePackageService knowledgePackageService,DSLRuleSetBuilder dslRuleSetBuilder) throws IOException{
		for(FlowNode node:nodes){
			if(node instanceof RuleNode){
				ResourceBase resourceBase=knowledgeBuilder.newResourceBase();
				RuleNode ruleNode=(RuleNode)node;
				resourceBase.addResource(ruleNode.getFile(), ruleNode.getVersion());
				KnowledgeBase knowledgeBase=knowledgeBuilder.buildKnowledgeBase(resourceBase);
				KnowledgePackage knowledgePackage=knowledgeBase.getKnowledgePackage();
				ruleNode.setKnowledgePackageWrapper(new KnowledgePackageWrapper(knowledgePackage));
			}else if(node instanceof RulePackageNode){
				RulePackageNode rulePackageNode=(RulePackageNode)node;
				String packageId=rulePackageNode.getProject()+"/"+rulePackageNode.getPackageId();
				KnowledgePackage knowledgePackage=knowledgePackageService.buildKnowledgePackage(packageId);
				rulePackageNode.setKnowledgePackageWrapper(new KnowledgePackageWrapper(knowledgePackage));
			}else if(node instanceof DecisionNode){
				DecisionNode decisionNode=(DecisionNode)node;
				if(decisionNode.getDecisionType().equals(DecisionType.Criteria)){
					String script=decisionNode.buildDSLScript(libraries);
					RuleSet ruleSet=dslRuleSetBuilder.build(script);
					KnowledgeBase knowledgeBase=knowledgeBuilder.buildKnowledgeBase(ruleSet);
					decisionNode.setKnowledgePackageWrapper(new KnowledgePackageWrapper(knowledgeBase.getKnowledgePackage()));					
				}
			}else if(node instanceof ScriptNode){
				ScriptNode scriptNode=(ScriptNode)node;
				String script=scriptNode.buildDSLScript(libraries);
				RuleSet ruleSet=dslRuleSetBuilder.build(script);
				KnowledgeBase knowledgeBase=knowledgeBuilder.buildKnowledgeBase(ruleSet);
				scriptNode.setKnowledgePackageWrapper(new KnowledgePackageWrapper(knowledgeBase.getKnowledgePackage()));
			}else if(node instanceof ForkNode){
				List<Connection> connections=node.getConnections();
				for(Connection conn:connections){
					String script=conn.getScript();
					if(script==null){
						continue;
					}
					script=conn.buildDSLScript(libraries);
					RuleSet ruleSet=dslRuleSetBuilder.build(script);
					KnowledgeBase knowledgeBase=knowledgeBuilder.buildKnowledgeBase(ruleSet);
					conn.setKnowledgePackageWrapper(new KnowledgePackageWrapper(knowledgeBase.getKnowledgePackage()));
				}
			}
		}
	}
	
	public void addLibrary(Library lib){
		if(libraries==null){
			libraries=new ArrayList<Library>();
		}
		libraries.add(lib);
	}
	@Override
	public List<Library> getLibraries() {
		return libraries;
	}
	public void setLibraries(List<Library> libraries) {
		this.libraries = libraries;
	}
	
	@Override
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	@Override
	public List<FlowNode> getNodes() {
		return nodes;
	}
	public void setNodes(List<FlowNode> nodes) {
		this.nodes = nodes;
	}
}
