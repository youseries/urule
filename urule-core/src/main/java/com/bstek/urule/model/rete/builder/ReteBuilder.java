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
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.urule.RuleException;
import com.bstek.urule.model.Node;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rete.CriteriaNode;
import com.bstek.urule.model.rete.JunctionNode;
import com.bstek.urule.model.rete.Line;
import com.bstek.urule.model.rete.ObjectTypeNode;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rete.TerminalNode;
import com.bstek.urule.model.rete.builder.BuildContext;
import com.bstek.urule.model.rete.builder.BuildContextImpl;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.lhs.BaseCriterion;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.loop.LoopRule;

/**
 * @author Jacky.gao
 * @since 2016年9月9日
 */
public class ReteBuilder implements ApplicationContextAware{
	public static final String BEAN_ID="urule.reteBuilder";
	private static Collection<CriterionBuilder> criterionBuilders;
	public Rete buildRete(List<Rule> rules,ResourceLibrary resourceLibrary){
		List<ObjectTypeNode> objectTypeNodes=new ArrayList<ObjectTypeNode>();
		Rete rete=new Rete(objectTypeNodes,resourceLibrary);
		BuildContext context=new BuildContextImpl(resourceLibrary,objectTypeNodes);
		for(Rule rule:rules){
			if(rule instanceof LoopRule)continue;
			if(rule.getLhs()==null || rule.getLhs().getCriterion()==null)continue;
			buildBranch(rule,context);
		}
		return rete;
	}

	private void buildBranch(Rule rule,BuildContext context){
		context.setCurrentRule(rule);
		Criterion criterion=rule.getLhs().getCriterion();
		BaseReteNode prevNode = buildCriterion(context, criterion);
		if(prevNode instanceof JunctionNode){
			JunctionNode junctionNode=(JunctionNode)prevNode;
			List<Line> toConnections=junctionNode.getToConnections();
			if(toConnections.size()==1){
				Line conn=toConnections.get(0);
				Node fromNode=conn.getFrom();
				if(fromNode instanceof CriteriaNode){
					CriteriaNode cnode=(CriteriaNode)fromNode;
					cnode.getLines().remove(conn);
					prevNode=cnode;
				}
			}
		}
		TerminalNode terminalNode=new TerminalNode(rule,context.nextId());
		prevNode.addLine(terminalNode);
	}

	public static BaseReteNode buildCriterion(BuildContext context,Criterion criterion) {
		BaseReteNode prevNode=null;
		for(CriterionBuilder criterionBuilder:criterionBuilders){
			if(criterionBuilder.support(criterion)){
				prevNode=criterionBuilder.buildCriterion((BaseCriterion)criterion, context);
				break;
			}
		}
		if(prevNode==null){
			throw new RuleException("Unknow criterion : "+criterion);
		}
		return prevNode;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
		ReteBuilder.criterionBuilders=applicationContext.getBeansOfType(CriterionBuilder.class).values();
	}
}
