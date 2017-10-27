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
package com.bstek.urule.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageImpl;

/**
 * @author Jacky.gao
 * @since 2014年12月22日
 */
public class KnowledgeBase {
	private ResourceLibrary resourceLibrary;
	private Map<String,FlowDefinition> flowMap;
	private Rete rete;
	private KnowledgePackageImpl knowledgePackage;
	private List<Rule> noLhsRules;
	public KnowledgeBase(Rete rete) {
		this(rete, null, null);
	}
	protected KnowledgeBase(Rete rete,Map<String,FlowDefinition> flowMap,List<Rule> noLhsRules) {
		this.rete=rete;
		this.resourceLibrary=rete.getResourceLibrary();
		this.flowMap=flowMap;
		this.noLhsRules=noLhsRules;
	}
	
	public KnowledgePackage getKnowledgePackage(){
		if(knowledgePackage!=null){
			return knowledgePackage;
		}
		knowledgePackage=new KnowledgePackageImpl();
		knowledgePackage.setRete(rete);
		knowledgePackage.setNoLhsRules(noLhsRules);
		knowledgePackage.setFlowMap(flowMap);
		knowledgePackage.buildWithElseRules();
		
		Map<String,String> variableCategoryMap=new HashMap<String,String>();
		knowledgePackage.setVariableCategoryMap(variableCategoryMap);
		List<VariableCategory> variableCategories=resourceLibrary.getVariableCategories();
		Map<String,String> parameters=new HashMap<String,String>();
		knowledgePackage.setParameters(parameters);
		for(VariableCategory category:variableCategories){
			String name=category.getName();
			variableCategoryMap.put(name, category.getClazz());
			if(name.equals(VariableCategory.PARAM_CATEGORY)){
				List<Variable> variables=category.getVariables();
				if(variables==null || variables.size()==0){
					continue;
				}
				for(Variable var:variables){
					parameters.put(var.getName(), var.getType().name());
				}
			}
		}
		return knowledgePackage;
	}
	
	
	public List<Rule> getNoLhsRules() {
		return noLhsRules;
	}

	public Rete getRete() {
		return rete;
	}
	public ResourceLibrary getResourceLibrary() {
		return resourceLibrary;
	}
	public Map<String, FlowDefinition> getFlowMap() {
		return flowMap;
	}
}
