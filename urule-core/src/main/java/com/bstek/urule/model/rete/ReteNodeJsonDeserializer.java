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
package com.bstek.urule.model.rete;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;

import com.bstek.urule.model.AbstractJsonDeserializer;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.ArithmeticType;
import com.bstek.urule.model.rule.Op;
import com.bstek.urule.model.rule.SimpleArithmetic;
import com.bstek.urule.model.rule.SimpleArithmeticValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.AllLeftPart;
import com.bstek.urule.model.rule.lhs.CollectLeftPart;
import com.bstek.urule.model.rule.lhs.CollectPurpose;
import com.bstek.urule.model.rule.lhs.CommonFunctionLeftPart;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.CriteriaUnit;
import com.bstek.urule.model.rule.lhs.EvalLeftPart;
import com.bstek.urule.model.rule.lhs.ExistLeftPart;
import com.bstek.urule.model.rule.lhs.FunctionLeftPart;
import com.bstek.urule.model.rule.lhs.JunctionType;
import com.bstek.urule.model.rule.lhs.Left;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.model.rule.lhs.MethodLeftPart;
import com.bstek.urule.model.rule.lhs.MultiCondition;
import com.bstek.urule.model.rule.lhs.NamedCriteria;
import com.bstek.urule.model.rule.lhs.PropertyCriteria;
import com.bstek.urule.model.rule.lhs.StatisticType;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;

/**
 * @author Jacky.gao
 * @since 2015年3月6日
 */
public class ReteNodeJsonDeserializer extends AbstractJsonDeserializer<List<ReteNode>> {
	@Override
	public List<ReteNode> deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec oc = jsonParser.getCodec();
        JsonNode jsonNode = oc.readTree(jsonParser);
        List<ReteNode> reteNodes=new ArrayList<ReteNode>();
        Iterator<JsonNode> childrenNodesIter=jsonNode.getElements();
        while(childrenNodesIter.hasNext()){
        	JsonNode childNode=childrenNodesIter.next();
        	int id=childNode.get("id").getIntValue();
        	JsonNode nodeTypeNode = childNode.get("nodeType");
        	if(nodeTypeNode==null){
        		continue;
        	}
        	String nodeTypeText=nodeTypeNode.getTextValue();
        	NodeType nodeType=NodeType.valueOf(nodeTypeText);
        	ReteNode reteNode=NodeType.newReteNodeInstance(nodeType);
        	if(reteNode instanceof ObjectTypeNode){
        		ObjectTypeNode node=(ObjectTypeNode)reteNode;
        		node.setObjectTypeClass(childNode.get("objectTypeClass").getTextValue());
        		node.setId(id);
        	}else if(reteNode instanceof AndNode){
        		AndNode node=(AndNode)reteNode;
        		node.setId(id);
        		node.setToLineCount(childNode.get("toLineCount").getIntValue());
        		node.setLines(parseLines(childNode));
        	}else if(reteNode instanceof OrNode){
        		OrNode node=(OrNode)reteNode;
        		node.setId(id);
        		node.setLines(parseLines(childNode));
        	}else if(reteNode instanceof CriteriaNode){
        		CriteriaNode node=(CriteriaNode)reteNode;
        		node.setId(id);
        		JsonNode criteriaNode=childNode.get("criteria");
        		node.setCriteria(parseCriteria(criteriaNode));
        		node.setLines(parseLines(childNode));
        	}else if(reteNode instanceof NamedCriteriaNode){
        		NamedCriteriaNode node=(NamedCriteriaNode)reteNode;
        		JsonNode criteriaNode=childNode.get("criteria");
        		node.setCriteria(parseNamedCriteria(criteriaNode));
        		node.setLines(parseLines(childNode));
        		node.setId(id);
        	}else if(reteNode instanceof TerminalNode){
        		TerminalNode node=(TerminalNode)reteNode;
        		node.setId(id);
        		node.setRule(parseRule(jsonParser,childNode));
        	}
        	reteNodes.add(reteNode);
        }
		return reteNodes;
	}
	
	private List<Line> parseLines(JsonNode node){
		JsonNode lineNodes=node.get("lines");
		if(lineNodes==null){
			return null;
		}
		List<Line> lines=new ArrayList<Line>();
		Iterator<JsonNode> iter=lineNodes.iterator();
		while(iter.hasNext()){
			JsonNode jsonNode=iter.next();
			Line line=new Line();
			line.setFromNodeId(jsonNode.get("fromNodeId").getIntValue());
			line.setToNodeId(jsonNode.get("toNodeId").getIntValue());
			lines.add(line);
		}
		return lines;
	}
	
	private NamedCriteria parseNamedCriteria(JsonNode jsonNode){
		NamedCriteria criteria=new NamedCriteria();
		criteria.setReferenceName(jsonNode.get("referenceName").getTextValue());
		criteria.setVariableCategory(jsonNode.get("variableCategory").getTextValue());
		JsonNode unitNode=jsonNode.get("unit");
		if(unitNode!=null){
			CriteriaUnit unit = parseCriteriaUnit(unitNode);
			criteria.setUnit(unit);
		}
		return criteria;
	}

	private CriteriaUnit parseCriteriaUnit(JsonNode unitNode) {
		CriteriaUnit unit=new CriteriaUnit();
		JsonNode criteriaNode=unitNode.get("criteria");
		if(criteriaNode!=null){
			unit.setCriteria(parseCriteria(criteriaNode));
		}
		JsonNode junctionTypeNode=unitNode.get("junctionType");
		if(junctionTypeNode!=null){
			unit.setJunctionType(JunctionType.valueOf(junctionTypeNode.getTextValue()));
		}
		JsonNode nextUnitNodes=unitNode.get("nextUnits");
		if(nextUnitNodes!=null){
			List<CriteriaUnit> nextUnits=new ArrayList<CriteriaUnit>();
			Iterator<JsonNode> iter=nextUnitNodes.iterator();
			while(iter.hasNext()){
				JsonNode nextNode=iter.next();
				nextUnits.add(parseCriteriaUnit(nextNode));
			}
			unit.setNextUnits(nextUnits);
		}
		return unit;
	}
	
	
	private Criteria parseCriteria(JsonNode jsonNode){
		Criteria criteria=new Criteria();
		String opText=jsonNode.get("op").getTextValue();
		Op op=Op.valueOf(opText);
		criteria.setOp(op);
		JsonNode leftJsonNode=jsonNode.get("left");
		Left left=new Left();
		criteria.setLeft(left);
		String type=JsonUtils.getJsonValue(leftJsonNode, "type");
		JsonNode leftPartJsonNode=leftJsonNode.get("leftPart");
		left.setType(LeftType.valueOf(type));
		switch(left.getType()){
		case function:
			FunctionLeftPart funPart=new FunctionLeftPart();
			funPart.setName(JsonUtils.getJsonValue(leftPartJsonNode, "name"));
			funPart.setParameters(JsonUtils.parseParameters(leftPartJsonNode));
			left.setLeftPart(funPart);
			break;
		case method:
			MethodLeftPart methodPart=new MethodLeftPart();
			methodPart.setBeanId(JsonUtils.getJsonValue(leftPartJsonNode, "beanId"));
			methodPart.setBeanLabel(JsonUtils.getJsonValue(leftPartJsonNode, "beanLabel"));
			methodPart.setMethodLabel(JsonUtils.getJsonValue(leftPartJsonNode, "methodLabel"));
			methodPart.setMethodName(JsonUtils.getJsonValue(leftPartJsonNode, "methodName"));
			methodPart.setParameters(JsonUtils.parseParameters(leftPartJsonNode));
			left.setLeftPart(methodPart);
			break;
		case eval:
			EvalLeftPart evalPart=new EvalLeftPart();
			evalPart.setExpression(JsonUtils.getJsonValue(leftPartJsonNode, "expression"));
			left.setLeftPart(evalPart);
			break;
		case all:
			AllLeftPart allLeftPart=new AllLeftPart();
			String statisticTypeStr=JsonUtils.getJsonValue(leftPartJsonNode, "statisticType");
			StatisticType statisticType=StatisticType.valueOf(statisticTypeStr);
			allLeftPart.setStatisticType(statisticType);
			if(statisticType.equals(StatisticType.percent)){
				allLeftPart.setPercent(Integer.valueOf(JsonUtils.getJsonValue(leftPartJsonNode, "percent")));
			}else if(statisticType.equals(StatisticType.amount)){
				allLeftPart.setAmount(Integer.valueOf(JsonUtils.getJsonValue(leftPartJsonNode, "amount")));
			}
			allLeftPart.setVariableCategory(JsonUtils.getJsonValue(leftPartJsonNode, "variableCategory"));
			allLeftPart.setVariableLabel(JsonUtils.getJsonValue(leftPartJsonNode, "variableLabel"));
			allLeftPart.setVariableName(JsonUtils.getJsonValue(leftPartJsonNode, "variableName"));
			JsonNode multiConditionNode=leftPartJsonNode.get("multiCondition");
			allLeftPart.setMultiCondition(parseMultiCondition(multiConditionNode));
			left.setLeftPart(allLeftPart);
			break;
		case exist:
			ExistLeftPart existLeftPart=new ExistLeftPart();
			String existStatisticTypeStr=JsonUtils.getJsonValue(leftPartJsonNode, "statisticType");
			StatisticType existStatisticType=StatisticType.valueOf(existStatisticTypeStr);
			existLeftPart.setStatisticType(existStatisticType);
			if(existStatisticType.equals(StatisticType.percent)){
				existLeftPart.setPercent(Integer.valueOf(JsonUtils.getJsonValue(leftPartJsonNode, "percent")));
			}else if(existStatisticType.equals(StatisticType.amount)){
				existLeftPart.setAmount(Integer.valueOf(JsonUtils.getJsonValue(leftPartJsonNode, "amount")));
			}
			existLeftPart.setVariableCategory(JsonUtils.getJsonValue(leftPartJsonNode, "variableCategory"));
			existLeftPart.setVariableLabel(JsonUtils.getJsonValue(leftPartJsonNode, "variableLabel"));
			existLeftPart.setVariableName(JsonUtils.getJsonValue(leftPartJsonNode, "variableName"));
			JsonNode existMultiConditionNode=leftPartJsonNode.get("multiCondition");
			existLeftPart.setMultiCondition(parseMultiCondition(existMultiConditionNode));
			left.setLeftPart(existLeftPart);
			break;
		case collect:
			CollectLeftPart collectLeftPart=new CollectLeftPart();
			collectLeftPart.setVariableCategory(JsonUtils.getJsonValue(leftPartJsonNode, "variableCategory"));
			collectLeftPart.setVariableLabel(JsonUtils.getJsonValue(leftPartJsonNode, "variableLabel"));
			collectLeftPart.setVariableName(JsonUtils.getJsonValue(leftPartJsonNode, "variableName"));
			collectLeftPart.setProperty(JsonUtils.getJsonValue(leftPartJsonNode, "property"));
			collectLeftPart.setPurpose(CollectPurpose.valueOf(JsonUtils.getJsonValue(leftPartJsonNode, "purpose")));
			JsonNode collectMultiConditionNode=leftPartJsonNode.get("multiCondition");
			collectLeftPart.setMultiCondition(parseMultiCondition(collectMultiConditionNode));
			left.setLeftPart(collectLeftPart);
			break;
		case commonfunction:
			CommonFunctionLeftPart functionPart=new CommonFunctionLeftPart();
			functionPart.setLabel(JsonUtils.getJsonValue(leftPartJsonNode, "label"));
			functionPart.setName(JsonUtils.getJsonValue(leftPartJsonNode, "name"));
			functionPart.setParameter(JsonUtils.parseCommonFunctionParameter(leftPartJsonNode));
			left.setLeftPart(functionPart);
			break;			
		default:
			VariableLeftPart varPart=new VariableLeftPart();
			varPart.setVariableCategory(JsonUtils.getJsonValue(leftPartJsonNode, "variableCategory"));
			varPart.setVariableLabel(JsonUtils.getJsonValue(leftPartJsonNode, "variableLabel"));
			varPart.setVariableName(JsonUtils.getJsonValue(leftPartJsonNode, "variableName"));
			varPart.setDatatype(Datatype.valueOf(JsonUtils.getJsonValue(leftPartJsonNode, "datatype")));
			left.setLeftPart(varPart);
		}
		left.setArithmetic(parseSimpleArithmetic(leftJsonNode));
		Value value=JsonUtils.parseValue(jsonNode);
		if(value!=null){
			criteria.setValue(value);
		}
		return criteria;
	}
	

	private MultiCondition parseMultiCondition(JsonNode multiConditionNode) {
		MultiCondition condition=new MultiCondition();
		condition.setType(JunctionType.valueOf(JsonUtils.getJsonValue(multiConditionNode, "type")));
		Iterator<JsonNode> iter=multiConditionNode.get("conditions").getElements();
		while(iter.hasNext()){
			JsonNode propertyCriteriaNode=iter.next();
			PropertyCriteria pc=new PropertyCriteria();
			pc.setOp(Op.valueOf(JsonUtils.getJsonValue(propertyCriteriaNode, "op")));
			pc.setProperty(JsonUtils.getJsonValue(propertyCriteriaNode, "property"));
			pc.setValue(JsonUtils.parseValue(propertyCriteriaNode));
			condition.addCondition(pc);
		}
		return condition;
	}
	
	private SimpleArithmetic parseSimpleArithmetic(JsonNode node){
		JsonNode arithNode=node.get("arithmetic");
		if(arithNode==null){
			return null;
		}
		SimpleArithmetic arith=new SimpleArithmetic();
		arith.setType(ArithmeticType.valueOf(JsonUtils.getJsonValue(arithNode, "type")));
		arith.setValue(parseSimpleArithmeticValue(arithNode));
		return arith;
	}
	
	private SimpleArithmeticValue parseSimpleArithmeticValue(JsonNode node){
		JsonNode valueNode=node.get("value");
		SimpleArithmeticValue value=new SimpleArithmeticValue();
		value.setContent(valueNode.get("content").getTextValue());
		value.setArithmetic(parseSimpleArithmetic(valueNode));
		return value;
	}
}
