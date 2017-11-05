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
