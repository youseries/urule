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
package com.bstek.urule.model.rete.builder;

import java.util.ArrayList;
import java.util.List;

import com.bstek.urule.RuleException;
import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rete.OrNode;
import com.bstek.urule.model.rete.builder.BuildContext;
import com.bstek.urule.model.rule.lhs.BaseCriterion;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Or;

/**
 * @author Jacky.gao
 * @since 2016年9月9日
 */
public class OrBuilder extends JunctionBuilder{
	@Override
	public BaseReteNode buildCriterion(BaseCriterion c, BuildContext context) {
		Or or=(Or)c;
		List<Criterion> criterions=or.getCriterions();
		if(criterions==null || criterions.size()==0){
			throw new RuleException("Condition join node[or] need one child at least.");
		}
		List<BaseReteNode> childNodes=new ArrayList<BaseReteNode>();
		for(Criterion criterion:criterions){
			BaseReteNode node=buildCriterion(criterion, context , null);
			if(node==null){
				continue;
			}
			childNodes.add(node);
		}
		if(childNodes.size()==0){
			return null;
		}
		if(childNodes.size()==1){
			return childNodes.get(0);
		}
		OrNode orNode=new OrNode(context.nextId());
		for(BaseReteNode node:childNodes){
			node.addLine(orNode);
		}
		return orNode;
	}
	@Override
	public boolean support(Criterion criterion) {
		return criterion instanceof Or;
	}
}
