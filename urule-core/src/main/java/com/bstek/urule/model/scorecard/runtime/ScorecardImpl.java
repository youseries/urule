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
