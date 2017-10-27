/*******************************************************************************
 * Copyright (C) 2017 Bstek.com
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.bstek.urule.model.rete.jsondeserializer;

import org.codehaus.jackson.JsonNode;

import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.bstek.urule.model.rule.VariableCategoryValue;

/**
 * @author Jacky.gao
 * @since 2015年3月6日
 */
public class VariableCategoryValueDeserializer implements ValueDeserializer {

	@Override
	public Value deserialize(JsonNode jsonNode) {
		VariableCategoryValue value=new VariableCategoryValue();
		value.setArithmetic(JsonUtils.parseComplexArithmetic(jsonNode));
		value.setVariableCategory(JsonUtils.getJsonValue(jsonNode, "variableCategory"));
		return value;
	}

	@Override
	public boolean support(ValueType type) {
		return type.equals(ValueType.VariableCategory);
	}
}
