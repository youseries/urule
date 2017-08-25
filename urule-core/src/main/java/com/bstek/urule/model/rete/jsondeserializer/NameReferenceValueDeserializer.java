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
package com.bstek.urule.model.rete.jsondeserializer;

import org.codehaus.jackson.JsonNode;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.NamedReferenceValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;

/**
 * @author Jacky.gao
 * @since 2016年8月16日
 */
public class NameReferenceValueDeserializer implements ValueDeserializer {
	@Override
	public Value deserialize(JsonNode jsonNode) {
		NamedReferenceValue value=new NamedReferenceValue();
		value.setArithmetic(JsonUtils.parseComplexArithmetic(jsonNode));
		String datatypeText=JsonUtils.getJsonValue(jsonNode, "datatype");
		if(datatypeText!=null){
			value.setDatatype(Datatype.valueOf(datatypeText));
		}
		value.setReferenceName(JsonUtils.getJsonValue(jsonNode, "referenceName"));
		value.setPropertyLabel(JsonUtils.getJsonValue(jsonNode, "propertyLabel"));
		value.setPropertyName(JsonUtils.getJsonValue(jsonNode, "propertyName"));
		return value;
	}

	@Override
	public boolean support(ValueType type) {
		return type.equals(ValueType.NamedReference);
	}
}
