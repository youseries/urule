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
