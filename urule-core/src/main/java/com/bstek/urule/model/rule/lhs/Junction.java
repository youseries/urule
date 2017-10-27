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

import java.util.ArrayList;
import java.util.List;


/**
 * @author Jacky.gao
 * @since 2014年12月25日
 */
public abstract class Junction extends BaseCriterion {
	private List<Criterion> criterions;
	public List<Criterion> getCriterions() {
		return criterions;
	}
	
	public void addCriterion(Criterion criterion){
		if(criterions==null){
			criterions=new ArrayList<Criterion>();
		}
		criterion.setParent(this);
		criterions.add(criterion);
	}

	public void setCriterions(List<Criterion> criterions) {
		for(Criterion criterion:criterions){
			criterion.setParent(this);
		}
		this.criterions = criterions;
	}
	public abstract String getJunctionType();
}
