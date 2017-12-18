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
package com.bstek.urule.builder.resource;

import org.dom4j.Element;

import com.bstek.urule.model.library.variable.VariableLibrary;
import com.bstek.urule.parse.deserializer.VariableLibraryDeserializer;

/**
 * @author Jacky.gao
 * @since 2014年12月22日
 */
public class VariableLibraryResourceBuilder implements ResourceBuilder<VariableLibrary> {
	private VariableLibraryDeserializer variableLibraryDeserializer;
	public VariableLibrary build(Element root) {
		VariableLibrary lib=new VariableLibrary();
		lib.setVariableCategories(variableLibraryDeserializer.deserialize(root));
		return lib;
	}

	public boolean support(Element root) {
		return variableLibraryDeserializer.support(root);
	}
	public ResourceType getType() {
		return ResourceType.VariableLibrary;
	}
	public void setVariableLibraryDeserializer(VariableLibraryDeserializer variableLibraryDeserializer) {
		this.variableLibraryDeserializer = variableLibraryDeserializer;
	}
}
