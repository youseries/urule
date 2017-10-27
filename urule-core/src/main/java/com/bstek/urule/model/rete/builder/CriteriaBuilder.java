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
package com.bstek.urule.model.rete.builder;

import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rete.builder.BuildContext;
import com.bstek.urule.model.rule.lhs.BaseCriterion;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;

/**
 * @author Jacky.gao
 * @since 2016年9月9日
 */
public class CriteriaBuilder extends CriterionBuilder{
	@Override
	public BaseReteNode buildCriterion(BaseCriterion c, BuildContext context) {
		Criteria criteria=(Criteria)c;
		return buildCriteria(criteria,null,context);
	}
	@Override
	public boolean support(Criterion criterion) {
		return criterion instanceof Criteria;
	}
}
