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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.bstek.urule.Utils;
import com.bstek.urule.debug.MsgType;
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
			context.debugMsg("☢☢☢控制台输出："+b.toPlainString(), MsgType.ConsoleOutput, true);
		}else if(content instanceof Double){
			Double d=(Double)content;
			context.debugMsg("☢☢☢控制台输出："+d.toString(), MsgType.ConsoleOutput, true);
		}else{
			String msg=(content==null ? "null" : content.toString());
			context.debugMsg("☢☢☢控制台输出："+msg, MsgType.ConsoleOutput, true);
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
