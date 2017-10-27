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

import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.scorecard.runtime.ScoreRuntimeValue;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.ValueCompute;

/**
 * @author Jacky.gao
 * @since 2016年9月26日
 */
public class ScoringAction extends AbstractAction {
	private Value value;
	private int rowNumber;
	private String name;
	private String weight;
	private ActionType actionType=ActionType.Scoring;
	
	public ScoringAction(int rowNumber,String name,String weight) {
		this.rowNumber=rowNumber;
		this.name=name;
		this.weight=weight;
	}

	@Override
	public ActionValue execute(Context context, Object matchedObject,List<Object> allMatchedObjects, Map<String, Object> variableMap) {
		ValueCompute valueCompute=(ValueCompute)context.getApplicationContext().getBean(ValueCompute.BEAN_ID);
		Object content=valueCompute.complexValueCompute(value, matchedObject, context,allMatchedObjects,variableMap);
		ScoreRuntimeValue scoreRuntimeValue=new ScoreRuntimeValue(this.rowNumber,this.name,this.weight,content);
		return new ActionValueImpl(scoreRuntimeValue.getName(),scoreRuntimeValue);
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}
	public String getWeight() {
		return weight;
	}
	@Override
	public ActionType getActionType() {
		return actionType;
	}
	public int getRowNumber() {
		return rowNumber;
	}
}
