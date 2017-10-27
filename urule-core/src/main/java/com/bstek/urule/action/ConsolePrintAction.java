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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.bstek.urule.Utils;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.ValueCompute;

/**
 * @author Jacky.gao
 * @since 2014年12月22日
 */
public class ConsolePrintAction extends AbstractAction {
	private Value value;
	private ActionType actionType=ActionType.ConsolePrint;
	public ActionValue execute(Context context,Object matchedObject,List<Object> allMatchedObjects,Map<String,Object> variableMap) {
		if(!Utils.isDebug()){
			return null;
		}
		ValueCompute valueCompute=(ValueCompute)context.getApplicationContext().getBean(ValueCompute.BEAN_ID);
		Object content=valueCompute.complexValueCompute(value, matchedObject, context,allMatchedObjects,variableMap);
		if(content instanceof BigDecimal){
			BigDecimal b=(BigDecimal)content;
			System.out.println(b.floatValue());
		}else if(content instanceof Double){
			Double d=(Double)content;
			System.out.println(d.floatValue());
		}else{
			System.out.println(content);
		}
		return null;
	}
	
	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}
	public ActionType getActionType() {
		return actionType;
	}
}
