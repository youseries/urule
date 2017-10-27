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
package com.bstek.urule.parse;

import java.util.List;

import org.dom4j.Element;

import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Or;
/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class JunctionParser extends CriterionParser {
	public Criterion parse(Element element) {
		List<Criterion> list=parseCriterion(element);
		if(list==null || list.size()==0){
			return null;
		}
		String name=element.getName();
		if(name.equals("and")){
			And and=new And();
			and.setCriterions(list);
			return and;
		}else{
			Or or=new Or();
			or.setCriterions(list);
			return or;
		}
	}

	public boolean support(String name) {
		return name.equals("and") || name.equals("or");
	}
}
