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

import java.util.List;

import org.dom4j.Element;

import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.parse.ParameterLibraryParser;

/**
 * @author Jacky.gao
 * @since 2015年3月10日
 */
public class ParameterLibraryDeserializer implements Deserializer<List<Variable>> {
	public static final String BEAN_ID="urule.parameterLibraryDeserializer";
	private ParameterLibraryParser parameterLibraryParser;
	@Override
	public List<Variable> deserialize(Element root) {
		return parameterLibraryParser.parse(root);
	}
	@Override
	public boolean support(Element root) {
		return parameterLibraryParser.support(root.getName());
	}
	public void setParameterLibraryParser(
			ParameterLibraryParser parameterLibraryParser) {
		this.parameterLibraryParser = parameterLibraryParser;
	}
}
