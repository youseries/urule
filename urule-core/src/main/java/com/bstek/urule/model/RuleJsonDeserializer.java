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
package com.bstek.urule.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;

import com.bstek.urule.model.rule.Rule;

/**
 * @author Jacky.gao
 * @since 2015年10月21日
 */
public class RuleJsonDeserializer extends AbstractJsonDeserializer<List<Rule>>{
	@Override
	public List<Rule> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec oc = jp.getCodec();
        JsonNode jsonNode = oc.readTree(jp);
        Iterator<JsonNode> childrenNodesIter=jsonNode.getElements();
        List<Rule> rules=new ArrayList<Rule>();
        while(childrenNodesIter.hasNext()){
        	JsonNode childNode=childrenNodesIter.next();
        	rules.add(parseRule(jp,childNode));
        }
		return rules;
	}
}
