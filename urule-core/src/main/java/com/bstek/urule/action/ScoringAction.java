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
