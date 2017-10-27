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

import com.bstek.urule.runtime.rete.EvaluationContext;

/**
 * @author Jacky.gao
 * @since 2015年5月29日
 */
public class AllLeftPart extends AbstractLeftPart {
	protected int amount;
	protected int percent;
	protected StatisticType statisticType=StatisticType.none;
	public boolean evaluate(EvaluationContext context,Object obj,List<Object> allMatchedObjects){
		ExprValue value=computeValue(context, obj, allMatchedObjects);
		int total=value.getTotal(),match=value.getMatch(),notMatch=value.getNotMatch();
		switch(statisticType){
		case none:
			if(notMatch==0){
				return true;
			}else{
				return false;
			}
		case amount:
			if(match==amount){
				return true;
			}else{
				return false;
			}
		case percent:
			BigDecimal left=new BigDecimal(match);
			BigDecimal currentPercent=left.divide(new BigDecimal(total),4,BigDecimal.ROUND_HALF_UP);
			int result=currentPercent.compareTo((new BigDecimal(percent)).divide(new BigDecimal(100)));
			if(result==0){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	
	@Override
	public String getId() {
		if(id==null){
			id="all("+variableCategory+"."+variableLabel+","+multiCondition.getId()+")";
		}
		return id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public StatisticType getStatisticType() {
		return statisticType;
	}

	public void setStatisticType(StatisticType statisticType) {
		this.statisticType = statisticType;
	}
}
