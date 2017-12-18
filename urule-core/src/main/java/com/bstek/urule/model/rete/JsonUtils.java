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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.bstek.urule.Configure;
import com.bstek.urule.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rete.jsondeserializer.CommonFunctionValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.ConstantValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.InputValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.MethodValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.NameReferenceValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.ParameterValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.ParenValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.ValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.VariableCategoryValueDeserializer;
import com.bstek.urule.model.rete.jsondeserializer.VariableValueDeserializer;
import com.bstek.urule.model.rule.ArithmeticType;
import com.bstek.urule.model.rule.ComplexArithmetic;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import com.bstek.urule.runtime.KnowledgePackageWrapper;

/**
 * @author Jacky.gao
 * @since 2015年3月6日
 */
public class JsonUtils {
	private static List<ValueDeserializer> valueDeserializers;
	static{
		valueDeserializers=new ArrayList<ValueDeserializer>();
		valueDeserializers.add(new ConstantValueDeserializer());
		valueDeserializers.add(new InputValueDeserializer());
		valueDeserializers.add(new ParameterValueDeserializer());
		valueDeserializers.add(new MethodValueDeserializer());
		valueDeserializers.add(new VariableCategoryValueDeserializer());
		valueDeserializers.add(new VariableValueDeserializer());
		valueDeserializers.add(new CommonFunctionValueDeserializer());
		valueDeserializers.add(new ParenValueDeserializer());
		valueDeserializers.add(new NameReferenceValueDeserializer());
	}
	public static String getJsonValue(JsonNode node,String propName){
		if(node.get(propName)!=null){
			return node.get(propName).asText();
		}
		return null;
	}
	public static ComplexArithmetic parseComplexArithmetic(JsonNode node){
		JsonNode arithmeticNode=node.get("arithmetic");
		if(arithmeticNode==null){
			return null;
		}
		ComplexArithmetic arith=new ComplexArithmetic();
		arith.setType(ArithmeticType.valueOf(getJsonValue(arithmeticNode, "type")));
		arith.setValue(parseValue(arithmeticNode));
		return arith;
	}
	public static List<Parameter> parseParameters(JsonNode node){
		JsonNode parametersNode=node.get("parameters");
		if(parametersNode==null){
			return null;
		}
		Iterator<JsonNode> iter=parametersNode.iterator();
		List<Parameter> parameters=new ArrayList<Parameter>();
		while(iter.hasNext()){
			JsonNode parameterNode=iter.next();
			Parameter param=new Parameter();
			param.setName(getJsonValue(parameterNode, "name"));
			String type=getJsonValue(parameterNode, "type");
			if(type!=null){
				param.setType(Datatype.valueOf(type));
			}
			String valueTypeText=getJsonValue(parameterNode, "valueType");
			if(valueTypeText!=null){
				param.setValue(parseValue(parameterNode));
			}
			param.setValue(parseValue(parameterNode));
			parameters.add(param);
		}
		return parameters;
	}
	public static Value parseValueNode(JsonNode valueNode){
		Value value=null;
		ValueType valueType=ValueType.valueOf(getJsonValue(valueNode, "valueType"));
		for(ValueDeserializer des:valueDeserializers){
			if(des.support(valueType)){
				value = des.deserialize(valueNode);
				break;
			}
		}
		return value;
	}
	
	public static KnowledgePackageWrapper parseKnowledgePackageWrapper(String content){
		try{			
			ObjectMapper mapper=new ObjectMapper();
			mapper.getDeserializationConfig().withDateFormat(new SimpleDateFormat(Configure.getDateFormat()));
			KnowledgePackageWrapper wrapper=mapper.readValue(content, KnowledgePackageWrapper.class);
			wrapper.buildDeserialize();
			return wrapper;
		}catch(Exception ex){
			throw new RuleException(ex);
		}
	}
	
	public static CommonFunctionParameter parseCommonFunctionParameter(JsonNode node){
		JsonNode parameterNode=node.get("parameter");
		if(parameterNode==null){
			return null;
		}
		CommonFunctionParameter parameter=new CommonFunctionParameter();
		parameter.setName(JsonUtils.getJsonValue(parameterNode, "name"));
		parameter.setProperty(JsonUtils.getJsonValue(parameterNode, "property"));
		parameter.setPropertyLabel(JsonUtils.getJsonValue(parameterNode, "propertyLabel"));
		parameter.setObjectParameter(JsonUtils.parseValueNode(parameterNode.get("objectParameter")));
		return parameter;
	}
	
	public static Value parseValue(JsonNode node){
		JsonNode valueNode=node.get("value");
		if(valueNode==null){
			return null;
		}
		return parseValueNode(valueNode);
	}
	public static List<ValueDeserializer> getValueDeserializers() {
		return valueDeserializers;
	}
}
