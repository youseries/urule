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
package com.bstek.urule.action;

import java.util.List;
import java.util.Map;

import com.bstek.urule.runtime.rete.Context;

/**
 * @author Jacky.gao
 * @since 2014年12月22日
 */
public class SimpleAction extends AbstractAction {
	public SimpleAction(Object value) {
	}
	public ActionValue execute(Context context,Object matchedObject,List<Object> allMatchedObjects,Map<String,Object> variableMap) {
		return null;
	}
	public ActionType getActionType() {
		return ActionType.ConsolePrint;
	}
}
