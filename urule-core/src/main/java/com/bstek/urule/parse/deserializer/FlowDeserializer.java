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
package com.bstek.urule.parse.deserializer;

import org.dom4j.Element;

import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.parse.flow.FlowDefinitionParser;
/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class FlowDeserializer implements Deserializer<FlowDefinition>{
	public static final String BEAN_ID="urule.flowDeserializer";
	private FlowDefinitionParser flowDefinitionParser;
	public FlowDefinition deserialize(Element root) {
		return (FlowDefinition)flowDefinitionParser.parse(root);
	}
	public boolean support(Element root) {
		if(flowDefinitionParser.support(root.getName())){
			return true;
		}else{
			return false;
		}
	}
	public void setFlowDefinitionParser(FlowDefinitionParser flowDefinitionParser) {
		this.flowDefinitionParser = flowDefinitionParser;
	}
}
