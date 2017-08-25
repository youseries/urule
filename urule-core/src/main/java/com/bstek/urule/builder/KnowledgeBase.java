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
