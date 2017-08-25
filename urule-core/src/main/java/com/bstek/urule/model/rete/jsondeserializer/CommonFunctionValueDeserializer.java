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

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.CommonFunctionValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;

/**
 * @author Jacky.gao
 * @since 2015年7月30日
 */
public class CommonFunctionValueDeserializer implements ValueDeserializer {
	@Override
	public Value deserialize(JsonNode jsonNode) {
		CommonFunctionValue value=new CommonFunctionValue();
		value.setArithmetic(JsonUtils.parseComplexArithmetic(jsonNode));
		value.setLabel(JsonUtils.getJsonValue(jsonNode, "label"));
		value.setName(JsonUtils.getJsonValue(jsonNode, "name"));
		value.setParameter(JsonUtils.parseCommonFunctionParameter(jsonNode));
		value.setValueType(ValueType.CommonFunction);
		return value;
	}

	@Override
	public boolean support(ValueType type) {
		return type.equals(ValueType.CommonFunction);
	}
}
