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

import java.math.BigDecimal;
import java.util.List;

import com.bstek.urule.Utils;
import com.bstek.urule.runtime.rete.EvaluationContext;

/**
 * @author Jacky.gao
 * @since 2015年5月29日
 */
public class CollectLeftPart extends AbstractLeftPart {
	private String property;
	private CollectPurpose purpose;
	public Object evaluate(EvaluationContext context,Object obj,List<Object> allMatchedObjects){
		ExprValue value=computeValue(context, obj, allMatchedObjects);
		int match=value.getMatch();
		List<Object> facts=value.getFacts();
		if(purpose.equals(CollectPurpose.count)){
			return match;
		}
		if(purpose.equals(CollectPurpose.avg)){
			BigDecimal total=new BigDecimal(0);
			for(Object fact:facts){
				Object propertyValue=Utils.getObjectProperty(fact, property);
				total=total.add(Utils.toBigDecimal(propertyValue));
			}
			return total.divide(new BigDecimal(match), 4, BigDecimal.ROUND_HALF_UP);
		}else if(purpose.equals(CollectPurpose.sum)){
			BigDecimal total=new BigDecimal(0);
			for(Object fact:facts){
				Object propertyValue=Utils.getObjectProperty(fact, property);
				total=total.add(Utils.toBigDecimal(propertyValue));
			}
			return total;
		}else if(purpose.equals(CollectPurpose.max)){
			BigDecimal max=new BigDecimal(0);
			for(Object fact:facts){
				Object propertyValue=Utils.getObjectProperty(fact, property);
				BigDecimal decValue=Utils.toBigDecimal(propertyValue);
				int result=decValue.compareTo(max);
				if(result==1){
					max=decValue;
				}
			}
			return max;
		}else if(purpose.equals(CollectPurpose.min)){
			BigDecimal min=null;
			for(Object fact:facts){
				Object propertyValue=Utils.getObjectProperty(fact, property);
				BigDecimal decValue=Utils.toBigDecimal(propertyValue);
				if(min!=null){
					int result=decValue.compareTo(min);
					if(result==-1){
						min=decValue;
					}
				}else{
					min=decValue;
				}
			}
			return min;
		}
		return 0;
	}
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public CollectPurpose getPurpose() {
		return purpose;
	}

	public void setPurpose(CollectPurpose purpose) {
		this.purpose = purpose;
	}

	@Override
	public String getId() {
		if(id==null){
			id="collect("+variableCategory+"."+variableLabel+",";
			if(multiCondition!=null){
				id+=multiCondition.getId()+")";
			}else{
				id+=")";
			}
			if(purpose.equals(CollectPurpose.count)){
				id+="."+purpose.name();
			}else{
				id+="."+property+"."+purpose.name();
			}
		}
		return id;
	}
}
