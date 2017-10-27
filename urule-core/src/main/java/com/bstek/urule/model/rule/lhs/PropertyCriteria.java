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
package com.bstek.urule.model.rule.lhs;

import java.util.List;

import com.bstek.urule.Utils;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.assertor.AssertorEvaluator;
import com.bstek.urule.runtime.rete.EvaluationContext;

/**
 * @author Jacky.gao
 * @since 2015年6月1日
 */
public class PropertyCriteria {
	private String property;
	private Op op;
	private Value value;
	private String id;
	public String getId(){
		if(id==null){
			id=property+op.name()+value.getId();
		}
		return id;
	}
	public boolean evaluate(EvaluationContext context,Object obj,List<Object> allMatchedObjects){
		Object left=Utils.getObjectProperty(obj, property);
		Object right=context.getValueCompute().complexValueCompute(value,obj,context,allMatchedObjects,null);
		if(right==null){
			return false;
		}
		AssertorEvaluator assertorEvaluator=context.getAssertorEvaluator();
		Datatype datatype=Utils.getDatatype(left);
		boolean result=assertorEvaluator.evaluate(left, right, datatype,op);
		return result;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public Op getOp() {
		return op;
	}
	public void setOp(Op op) {
		this.op = op;
	}
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
}
