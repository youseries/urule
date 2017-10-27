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
