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

import com.bstek.urule.model.library.action.ActionLibrary;
import com.bstek.urule.parse.deserializer.ActionLibraryDeserializer;


/**
 * @author Jacky.gao
 * @since 2014年11月22日
 */
public class ActionLibraryResourceBuilder implements ResourceBuilder<ActionLibrary> {
	private ActionLibraryDeserializer actionLibraryDeserializer;
	public ActionLibrary build(Element root) {
		return actionLibraryDeserializer.deserialize(root);
	}

	public void setActionLibraryDeserializer(ActionLibraryDeserializer actionLibraryDeserializer) {
		this.actionLibraryDeserializer = actionLibraryDeserializer;
	}
	public boolean support(Element root) {
		return actionLibraryDeserializer.support(root);
	}
	public ResourceType getType() {
		return ResourceType.ActionLibrary;
	}
}
