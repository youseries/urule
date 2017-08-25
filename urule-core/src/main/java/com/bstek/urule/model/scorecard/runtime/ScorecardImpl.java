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
package com.bstek.urule.model.scorecard.runtime;

import java.math.BigDecimal;
import java.util.List;

import com.bstek.urule.Utils;

/**
 * @author Jacky.gao
 * @since 2016年9月26日
 */
public class ScorecardImpl implements Scorecard {
	private String name;
	private List<RowItem> rowItems;
	
	public ScorecardImpl(String name, List<RowItem> rowItems) {
		this.name = name;
		this.rowItems = rowItems;
	}

	public BigDecimal executeSum(){
		BigDecimal result=new BigDecimal(0);
		for(RowItem row:rowItems){
			BigDecimal score=Utils.toBigDecimal(row.getScore());
			row.setActualScore(score);
			result=result.add(score);
		}
		return result;
	}
	public BigDecimal executeWeightSum(){
		BigDecimal result=new BigDecimal(0);
		for(RowItem row:rowItems){
			BigDecimal score=Utils.toBigDecimal(row.getScore());
			BigDecimal weight=Utils.toBigDecimal(row.getWeight());
			BigDecimal actualScore=score.multiply(weight);
			row.setActualScore(actualScore);
			result=result.add(actualScore);
		}
		return result;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public List<RowItem> getRowItems() {
		return rowItems;
	}
}
