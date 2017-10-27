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
