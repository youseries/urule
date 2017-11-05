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
