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
