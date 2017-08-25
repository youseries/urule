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
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;

import com.bstek.urule.runtime.KnowledgePackageWrapper;

/**
 * @author Jacky.gao
 * @since 2015年4月21日
 */
public class FlowNodeJsonDeserializer extends JsonDeserializer<List<FlowNode>> {
	@Override
	public List<FlowNode> deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {
		ObjectMapper mapper = (ObjectMapper)jsonParser.getCodec();
        JsonNode jsonNode = mapper.readTree(jsonParser);
        List<FlowNode> flowNodes=new ArrayList<FlowNode>();
        Iterator<JsonNode> childrenNodesIter=jsonNode.getElements();
        while(childrenNodesIter.hasNext()){
        	JsonNode childNode=childrenNodesIter.next();
        	JsonNode type=childNode.get("type");
        	if(type==null){
        		continue;
        	}
        	FlowNode node=null;
        	FlowNodeType nodeType=FlowNodeType.valueOf(type.getTextValue());
        	switch(nodeType){
        	case Action:
        		ActionNode actionNode=new ActionNode();
        		JsonNode actionBeanNode=childNode.get("actionBean");
        		if(actionBeanNode!=null){
        			actionNode.setActionBean(actionBeanNode.getTextValue());
        		}
        		node=actionNode;
        		break;
        	case Script:
        		ScriptNode scriptNode=new ScriptNode();
        		JsonNode sn=childNode.get("script");
        		if(sn!=null){
        			scriptNode.setScript(sn.getTextValue());
        		}
        		node=scriptNode;
        		break;
        	case Decision:
        		DecisionNode decisionNode=new DecisionNode();
        		DecisionType decisionType=DecisionType.valueOf(childNode.get("decisionType").getTextValue());
        		decisionNode.setDecisionType(decisionType);
        		JsonNode itemsNode=childNode.get("items");
        		Iterator<JsonNode> iter=itemsNode.getElements();
        		List<DecisionItem> items=new ArrayList<DecisionItem>();
        		while(iter.hasNext()){
        			JsonNode itemNode=iter.next();
        			DecisionItem item=new DecisionItem();
        			item.setTo(itemNode.get("to").getTextValue());
        			if(decisionType.equals(DecisionType.Criteria)){
        				item.setScript(itemNode.get("script").getTextValue());        				
        			}else{
        				item.setPercent(itemNode.get("percent").getIntValue());
        			}
        			items.add(item);
        		}
        		decisionNode.setItems(items);
        		node=decisionNode;
        		break;
        	case End:
        		node=new EndNode();
        		break;
        	case Fork:
        		node=new ForkNode();
        		break;
        	case Join:
        		node=new JoinNode();
        		break;
        	case Rule:
        		RuleNode ruleNode=new RuleNode();
        		ruleNode.setFile(childNode.get("file").getTextValue());
        		JsonNode versionNode=childNode.get("version");
        		if(versionNode!=null){
        			ruleNode.setVersion(versionNode.getTextValue());
        		}
        		node=ruleNode;
        		break;
        	case RulePackage:
        		RulePackageNode packageNode=new RulePackageNode();
        		packageNode.setPackageId(childNode.get("packageId").getTextValue());
        		if(childNode.get("project")!=null){
        			packageNode.setPackageId(childNode.get("project").getTextValue());        			
        		}
        		node=packageNode;
        		break;
        	case Start:
        		node=new StartNode();
        		break;
        	}
        	String name=childNode.get("name").getTextValue();
        	node.setName(name);
        	JsonNode eventNode=childNode.get("eventBean");
        	if(eventNode!=null){
        		node.setEventBean(eventNode.getTextValue());
        	}
        	JsonNode connectionsNode=childNode.get("connections");
        	if(connectionsNode!=null){
        		List<Connection> connections=new ArrayList<Connection>();
        		Iterator<JsonNode> iter=connectionsNode.getElements();
        		while(iter.hasNext()){
        			JsonNode connNode=iter.next();
        			Connection conn=mapper.readValue(connNode,Connection.class);
        			connections.add(conn);
        		}
        		for(Connection conn:connections){
        			conn.buildDeserialize();
        		}
        		node.setConnections(connections);
        	}
        	JsonNode knowledgePackageWrapperNode=childNode.get("knowledgePackageWrapper");
        	if(knowledgePackageWrapperNode!=null){
        		KnowledgePackageWrapper wrapper=mapper.readValue(knowledgePackageWrapperNode, KnowledgePackageWrapper.class);
        		wrapper.buildDeserialize();
        		BindingNode bindingNode=(BindingNode)node;
        		bindingNode.setKnowledgePackageWrapper(wrapper);
        	}
        	flowNodes.add(node);
        }
        return flowNodes;
	}
}
