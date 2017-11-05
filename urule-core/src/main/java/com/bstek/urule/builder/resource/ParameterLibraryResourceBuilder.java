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

import java.util.HashMap;

import org.dom4j.Element;

import com.bstek.urule.model.library.variable.CategoryType;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.parse.deserializer.ParameterLibraryDeserializer;

/**
 * @author Jacky.gao
 * @since 2015年3月11日
 */
public class ParameterLibraryResourceBuilder implements ResourceBuilder<VariableCategory> {
	private ParameterLibraryDeserializer parameterLibraryDeserializer;
	@Override
	public VariableCategory build(Element root) {
		VariableCategory category=new VariableCategory();
		category.setName(VariableCategory.PARAM_CATEGORY);
		category.setClazz(HashMap.class.getName());
		category.setType(CategoryType.Clazz);
		category.setVariables(parameterLibraryDeserializer.deserialize(root));
		return category;
	}
	@Override
	public ResourceType getType() {
		return ResourceType.ParameterLibrary;
	}
	@Override
	public boolean support(Element root) {
		return parameterLibraryDeserializer.support(root);
	}
	public void setParameterLibraryDeserializer(
			ParameterLibraryDeserializer parameterLibraryDeserializer) {
		this.parameterLibraryDeserializer = parameterLibraryDeserializer;
	}
}
