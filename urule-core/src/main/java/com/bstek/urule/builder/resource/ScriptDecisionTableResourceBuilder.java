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

import com.bstek.urule.model.table.ScriptDecisionTable;
import com.bstek.urule.parse.deserializer.ScriptDecisionTableDeserializer;

/**
 * @author Jacky.gao
 * @since 2015年2月9日
 */
public class ScriptDecisionTableResourceBuilder implements ResourceBuilder<ScriptDecisionTable> {
	private ScriptDecisionTableDeserializer scriptDecisionTableDeserializer;
	public ScriptDecisionTable build(Element root) {
		return scriptDecisionTableDeserializer.deserialize(root);
	}
	public ResourceType getType() {
		return ResourceType.ScriptDecisionTable;
	}
	public boolean support(Element root) {
		return scriptDecisionTableDeserializer.support(root);
	}
	public void setScriptDecisionTableDeserializer(
			ScriptDecisionTableDeserializer scriptDecisionTableDeserializer) {
		this.scriptDecisionTableDeserializer = scriptDecisionTableDeserializer;
	}
}
