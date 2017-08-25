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
package com.bstek.urule.model.rule.lhs;

import java.math.BigDecimal;
import java.util.List;

import com.bstek.urule.runtime.rete.EvaluationContext;

/**
 * @author Jacky.gao
 * @since 2015年5月29日
 */
public class ExistLeftPart extends AllLeftPart {
	public boolean evaluate(EvaluationContext context,Object obj,List<Object> allMatchedObjects){
		ExprValue value=computeValue(context, obj, allMatchedObjects);
		int total=value.getTotal(),match=value.getMatch();
		switch(statisticType){
		case none:
			if(match>0){
				return true;
			}else{
				return false;
			}
		case amount:
			if(match>=amount){
				return true;
			}else{
				return false;
			}
		case percent:
			BigDecimal left=new BigDecimal(match);
			BigDecimal currentPercent=left.divide(new BigDecimal(total),4,BigDecimal.ROUND_HALF_UP);
			int result=currentPercent.compareTo((new BigDecimal(percent)).divide(new BigDecimal(100)));
			if(result!=-1){
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
			id="all("+variableCategory+"."+variableLabel+","+multiCondition.getId();
			if(statisticType.equals(StatisticType.amount)){
				id+=","+amount+")";
			}else if(statisticType.equals(StatisticType.percent)){
				id+=","+percent+"%)";
			}else{
				id+=")";
			}
		}
		return id;
	}
}
