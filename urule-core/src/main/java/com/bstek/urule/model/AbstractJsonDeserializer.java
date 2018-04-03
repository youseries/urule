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
package com.bstek.urule.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;

import com.bstek.urule.Configure;
import com.bstek.urule.RuleException;
import com.bstek.urule.action.Action;
import com.bstek.urule.action.ActionType;
import com.bstek.urule.action.ConsolePrintAction;
import com.bstek.urule.action.ExecuteCommonFunctionAction;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.action.ScoringAction;
import com.bstek.urule.action.VariableAssignAction;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.Other;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.model.rule.loop.LoopEnd;
import com.bstek.urule.model.rule.loop.LoopRule;
import com.bstek.urule.model.rule.loop.LoopStart;
import com.bstek.urule.model.rule.loop.LoopTarget;
import com.bstek.urule.model.scorecard.AssignTargetType;
import com.bstek.urule.model.scorecard.ScoringType;
import com.bstek.urule.model.scorecard.runtime.ScoreRule;
import com.bstek.urule.runtime.KnowledgePackageWrapper;

/**
 * @author Jacky.gao
 * @since 2015年10月21日
 */
public abstract class AbstractJsonDeserializer<T> extends JsonDeserializer<T> {
	protected Rule parseRule(JsonParser jsonParser,JsonNode node){
		SimpleDateFormat sd=new SimpleDateFormat(Configure.getDateFormat());
		try {
			JsonNode ruleNode=node.get("rule");
			if(ruleNode==null){
				ruleNode=node;
			}
			Rule rule=null;
			String scoringTypeStr=JsonUtils.getJsonValue(ruleNode, "scoringType");
			if(StringUtils.isNotBlank(scoringTypeStr)){
				ScoringType scoringType=ScoringType.valueOf(scoringTypeStr);
				ScoreRule scoreRule=new ScoreRule();
				scoreRule.setScoringType(scoringType);
				buildScoreRule(jsonParser,ruleNode,scoreRule);
				rule=scoreRule;
			}else{
				String loopRuleStr=JsonUtils.getJsonValue(ruleNode, "loopRule");
				if(loopRuleStr!=null){
					boolean isLoopRule=Boolean.valueOf(loopRuleStr);
					if(isLoopRule){
						LoopRule loopRule=new LoopRule();
						buildLoopRule(ruleNode, loopRule);
						rule=loopRule;
					}else{
						rule=new Rule();					
					}
				}else{
					rule=new Rule();
				}				
			}
			rule.setActivationGroup(JsonUtils.getJsonValue(ruleNode, "activationGroup"));
			rule.setAgendaGroup(JsonUtils.getJsonValue(ruleNode, "agendaGroup"));
			String autoFocus=JsonUtils.getJsonValue(ruleNode, "autoFocus");
			if(autoFocus!=null){
				rule.setAutoFocus(Boolean.valueOf(autoFocus));
			}
			String loop=JsonUtils.getJsonValue(ruleNode, "loop");
			if(loop!=null){
				rule.setLoop(Boolean.valueOf(loop));
			}
			String effectiveDateText=JsonUtils.getJsonValue(ruleNode, "effectiveDate");
			if(effectiveDateText!=null){
				rule.setEffectiveDate(sd.parse(effectiveDateText));
			}
			String enabled=JsonUtils.getJsonValue(ruleNode, "enabled");
			if(enabled!=null){
				rule.setEnabled(Boolean.valueOf(enabled));
			}
			String debug=JsonUtils.getJsonValue(ruleNode, "debug");
			if(debug!=null){
				rule.setDebug(Boolean.valueOf(debug));
			}
			String expiresDateText=JsonUtils.getJsonValue(ruleNode, "expiresDate");
			if(expiresDateText!=null){
				rule.setExpiresDate(sd.parse(expiresDateText));
			}
			rule.setName(JsonUtils.getJsonValue(ruleNode, "name"));
			rule.setRuleflowGroup(JsonUtils.getJsonValue(ruleNode, "ruleflowGroup"));
			String salienceText=JsonUtils.getJsonValue(ruleNode, "salience");
			if(salienceText!=null){
				rule.setSalience(Integer.valueOf(salienceText));
			}
			Rhs rhs=new Rhs();
			rule.setRhs(rhs);
			JsonNode rhsNode=ruleNode.get("rhs");
			if(rhsNode!=null){
				rhs.setActions(parseActions(rhsNode));				
			}
			
			JsonNode otherNode=ruleNode.get("other");
			if(otherNode!=null){
				Other other=new Other();
				rule.setOther(other);
				other.setActions(parseActions(otherNode));				
			}
			return rule;
		} catch (ParseException e) {
			throw new RuleException(e);
		}
	}
	
	private void buildScoreRule(JsonParser jsonParser,JsonNode ruleNode,ScoreRule rule){
		rule.setScoringBean(JsonUtils.getJsonValue(ruleNode, "scoringBean"));
		AssignTargetType assignTargetType=AssignTargetType.valueOf(JsonUtils.getJsonValue(ruleNode, "assignTargetType"));
		rule.setAssignTargetType(assignTargetType);
		rule.setVariableCategory(JsonUtils.getJsonValue(ruleNode, "variableCategory"));
		rule.setVariableName(JsonUtils.getJsonValue(ruleNode, "variableName"));
		rule.setVariableLabel(JsonUtils.getJsonValue(ruleNode, "variableLabel"));
		String datatypeStr=JsonUtils.getJsonValue(ruleNode, "datatype");
		if(StringUtils.isNotBlank(datatypeStr)){
			rule.setDatatype(Datatype.valueOf(datatypeStr));
		}
		try{
			JsonNode knowledgePackageWrapperNode=ruleNode.get("knowledgePackageWrapper");
			ObjectMapper mapper = (ObjectMapper)jsonParser.getCodec();
			KnowledgePackageWrapper wrapper=mapper.readValue(knowledgePackageWrapperNode, KnowledgePackageWrapper.class);
			wrapper.buildDeserialize();
			rule.setKnowledgePackageWrapper(wrapper);			
		}catch(Exception ex){
			throw new RuleException(ex);
		}
		
	}
	
	private void buildLoopRule(JsonNode ruleNode,LoopRule rule){
		JsonNode targetNode=ruleNode.get("loopTarget");
		if(targetNode!=null){
			LoopTarget target=new LoopTarget();
			Value value=JsonUtils.parseValue(targetNode);
			target.setValue(value);
			rule.setLoopTarget(target);
		}
		JsonNode loopStartNode=ruleNode.get("loopStart");
		if(loopStartNode!=null){
			List<Action> actions=parseActions(loopStartNode);
			LoopStart start=new LoopStart();
			start.setActions(actions);
			rule.setLoopStart(start);
		}
		JsonNode loopEndNode=ruleNode.get("loopEnd");
		if(loopEndNode!=null){
			List<Action> actions=parseActions(loopEndNode);
			LoopEnd end=new LoopEnd();
			end.setActions(actions);
			rule.setLoopEnd(end);
		}
		JsonNode knowledgeWrapper=ruleNode.get("knowledgePackageWrapper");
		if(knowledgeWrapper!=null){
			KnowledgePackageWrapper wrapper=JsonUtils.parseKnowledgePackageWrapper(knowledgeWrapper.toString());
			rule.setKnowledgePackageWrapper(wrapper);
		}
	}
	
	private List<Action> parseActions(JsonNode node){
		List<Action> actions=new ArrayList<Action>();
		JsonNode nodes=node.get("actions");
		if(nodes==null)return actions;
		Iterator<JsonNode> iter=nodes.iterator();
		while(iter.hasNext()){
			JsonNode jsonNode=iter.next();
			ActionType actionType=ActionType.valueOf(JsonUtils.getJsonValue(jsonNode, "actionType"));
			switch(actionType){
			case ConsolePrint:
				ConsolePrintAction console=new ConsolePrintAction();
				console.setValue(JsonUtils.parseValue(jsonNode));
				console.setPriority(Integer.valueOf(JsonUtils.getJsonValue(jsonNode, "priority")));
				actions.add(console);
				break;
			case ExecuteMethod:
				ExecuteMethodAction method=new ExecuteMethodAction();
				method.setBeanId(JsonUtils.getJsonValue(jsonNode, "beanId"));
				method.setBeanLabel(JsonUtils.getJsonValue(jsonNode, "beanLabel"));
				method.setMethodLabel(JsonUtils.getJsonValue(jsonNode, "methodLabel"));
				method.setPriority(Integer.valueOf(JsonUtils.getJsonValue(jsonNode, "priority")));
				method.setMethodName(JsonUtils.getJsonValue(jsonNode, "methodName"));
				method.setParameters(JsonUtils.parseParameters(jsonNode));
				actions.add(method);
				break;
			case VariableAssign:
				VariableAssignAction assign=new VariableAssignAction();
				String type=JsonUtils.getJsonValue(jsonNode, "type");
				if(type!=null){					
					assign.setType(LeftType.valueOf(type)); 
				}
				assign.setReferenceName(JsonUtils.getJsonValue(jsonNode, "referenceName"));
				assign.setDatatype(Datatype.valueOf(JsonUtils.getJsonValue(jsonNode, "datatype")));
				assign.setVariableCategory(JsonUtils.getJsonValue(jsonNode, "variableCategory"));
				assign.setVariableLabel(JsonUtils.getJsonValue(jsonNode, "variableLabel"));
				assign.setVariableName(JsonUtils.getJsonValue(jsonNode, "variableName"));
				assign.setPriority(Integer.valueOf(JsonUtils.getJsonValue(jsonNode, "priority")));
				assign.setValue(JsonUtils.parseValue(jsonNode));
				actions.add(assign);
				break;
			case ExecuteCommonFunction:
				ExecuteCommonFunctionAction ca=new ExecuteCommonFunctionAction();
				ca.setLabel(JsonUtils.getJsonValue(jsonNode, "label"));
				ca.setName(JsonUtils.getJsonValue(jsonNode, "name"));
				ca.setParameter(JsonUtils.parseCommonFunctionParameter(jsonNode));
				ca.setPriority(Integer.valueOf(JsonUtils.getJsonValue(jsonNode, "priority")));
				actions.add(ca);
				break;
			case Scoring:
				int rowNumber=Integer.valueOf(JsonUtils.getJsonValue(jsonNode, "rowNumber"));
				String name=JsonUtils.getJsonValue(jsonNode, "name");
				String weight=JsonUtils.getJsonValue(jsonNode, "weight");
				ScoringAction sa=new ScoringAction(rowNumber, name, weight);
				sa.setValue(JsonUtils.parseValue(jsonNode));
				actions.add(sa);
				break;
			}
		}
		return actions;
	}
}
