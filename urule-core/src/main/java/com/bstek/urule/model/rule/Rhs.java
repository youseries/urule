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
package com.bstek.urule.model.rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bstek.urule.action.Action;

/**
 * @author Jacky.gao
 * @since 2014年12月25日
 */
public class Rhs {
	private List<Action> actions;

	public List<Action> getActions() {
		return actions;
	}
	
	public void setActions(List<Action> actions) {
		this.actions = actions;
		Collections.sort(actions);
	}
	
	public void addAction(Action action) {
		if(actions==null){
			actions=new ArrayList<Action>();
		}
		actions.add(action);
		Collections.sort(actions);
	}
}
