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
package com.bstek.urule.builder.resource;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.bstek.urule.RuleException;
import com.bstek.urule.action.Action;
import com.bstek.urule.action.ScoringAction;
import com.bstek.urule.builder.KnowledgeBase;
import com.bstek.urule.builder.ResourceLibraryBuilder;
import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rete.builder.ReteBuilder;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.Left;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.model.rule.lhs.Lhs;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;
import com.bstek.urule.model.scorecard.AttributeRow;
import com.bstek.urule.model.scorecard.CardCell;
import com.bstek.urule.model.scorecard.ConditionRow;
import com.bstek.urule.model.scorecard.CustomCol;
import com.bstek.urule.model.scorecard.ScorecardDefinition;
import com.bstek.urule.model.scorecard.runtime.ScoreRule;
import com.bstek.urule.model.scorecard.runtime.ScoreRuntimeValue;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.parse.deserializer.ScorecardDeserializer;
import com.bstek.urule.runtime.KnowledgePackageWrapper;

/**
 * @author Jacky.gao
 * @since 2016年9月26日
 */
public class ScorecardResourceBuilder implements ResourceBuilder<ScoreRule> {
	private ReteBuilder reteBuilder;
	private ResourceLibraryBuilder resourceLibraryBuilder;
	private ScorecardDeserializer scorecardDeserializer;
	private RulesRebuilder rulesRebuilder;
	@Override
	public ScoreRule build(Element root) {
		ScorecardDefinition scorecard = scorecardDeserializer.deserialize(root);
		ScoreRule scoreRule=new ScoreRule();
		scoreRule.setName(scorecard.getName());
		scoreRule.setEffectiveDate(scorecard.getEffectiveDate());
		scoreRule.setExpiresDate(scorecard.getExpiresDate());
		scoreRule.setEnabled(scorecard.getEnabled());
		scoreRule.setSalience(scorecard.getSalience());
		scoreRule.setDebug(scorecard.getDebug());

		scoreRule.setScoringBean(scorecard.getScoringBean());
		scoreRule.setScoringType(scorecard.getScoringType());
		
		scoreRule.setAssignTargetType(scorecard.getAssignTargetType());
		
		scoreRule.setDatatype(scorecard.getDatatype());
		scoreRule.setVariableCategory(scorecard.getVariableCategory());
		scoreRule.setVariableName(scorecard.getVariableName());
		scoreRule.setVariableLabel(scorecard.getVariableLabel());
		scoreRule.setLibraries(scorecard.getLibraries());
		List<AttributeRow> rows=scorecard.getRows();
		List<CardCell> cells=scorecard.getCells();
		List<CustomCol> customCols=scorecard.getCustomCols();
		String attributeVariableCategory=scorecard.getAttributeColVariableCategory();
		List<Rule> rules=new ArrayList<Rule>();
		for(AttributeRow row:rows){
			List<ConditionRow> conditionRows=row.getConditionRows();
			int attributeRowNumber=row.getRowNumber();
			Rule rule = buildRule(cells, customCols,attributeVariableCategory, attributeRowNumber,attributeRowNumber);
			rules.add(rule);
			rule.setDebug(scorecard.getDebug());
			for(ConditionRow conditionRow:conditionRows){
				int conditionRowNumber=conditionRow.getRowNumber();
				Rule r = buildRule(cells,customCols,attributeVariableCategory,attributeRowNumber,conditionRowNumber);
				r.setDebug(scorecard.getDebug());
				rules.add(r);
			}
		}
		rulesRebuilder.rebuildRules(scorecard.getLibraries(), rules);
		ResourceLibrary resourceLibrary=resourceLibraryBuilder.buildResourceLibrary(scorecard.getLibraries());
		Rete rete=reteBuilder.buildRete(rules, resourceLibrary);
		KnowledgeBase base=new KnowledgeBase(rete);
		KnowledgePackageWrapper knowledgePackageWrapper=new KnowledgePackageWrapper(base.getKnowledgePackage());
		scoreRule.setKnowledgePackageWrapper(knowledgePackageWrapper);
		return scoreRule;
	}


	private Rule buildRule(List<CardCell> cells, List<CustomCol> customCols,String attributeVariableCategory, int attributeRowNumber,int rowNumber) {
		Rule scoreRule=buildScoreRule(cells, attributeVariableCategory,attributeRowNumber,rowNumber);
		scoreRule.getRhs().getActions().addAll(buildCustomColActions(cells, customCols, rowNumber));
		return scoreRule;
	}

	
	private List<Action> buildCustomColActions(List<CardCell> cells,List<CustomCol> customCols,int rowNumber){
		List<Action> actions=new ArrayList<Action>();
		for(CustomCol col:customCols){
			ScoringAction action=new ScoringAction(rowNumber,col.getName(),null);
			CardCell cardCell=fetchCell(cells, rowNumber, col.getColNumber());
			action.setValue(cardCell.getValue());
			actions.add(action);
		}
		return actions;
	}

	private Rule buildScoreRule(List<CardCell> cells,String attributeVariableCategory,int attributeRowNumber,int rowNumber) {
		CardCell attributeCell=fetchCell(cells, attributeRowNumber, 1);
		CardCell conditionCell=fetchCell(cells, rowNumber, 2);
		CardCell scoreCell=fetchCell(cells, rowNumber, 3);
		Rule rule = buildRule(attributeVariableCategory,attributeCell,conditionCell,scoreCell);
		rule.setName("sc"+rowNumber);
		return rule;
	}
	
	
	private Rule buildRule(String variableCategory,CardCell attributeCell,CardCell conditionCell,CardCell scoreCell){
		Rule rule=new Rule();
		Lhs lhs=new Lhs();
		rule.setLhs(lhs);
		Junction jun=conditionCell.getJoint().getJunction();
		lhs.setCriterion(jun);
		for(Condition condition:conditionCell.getJoint().getConditions()){
			Criteria criteria=new Criteria();
			criteria.setOp(condition.getOp());
			Left left=new Left();
			VariableLeftPart leftPart=new VariableLeftPart();
			leftPart.setVariableCategory(variableCategory);
			leftPart.setDatatype(attributeCell.getDatatype());
			leftPart.setVariableName(attributeCell.getVariableName());
			leftPart.setVariableLabel(attributeCell.getVariableLabel());
			left.setLeftPart(leftPart);
			criteria.setLeft(left);
			left.setType(LeftType.variable);
			criteria.setValue(condition.getValue());
			jun.addCriterion(criteria);
		}
		Rhs rhs=new Rhs();
		rule.setRhs(rhs);
		ScoringAction action=new ScoringAction(conditionCell.getRow(),ScoreRuntimeValue.SCORE_VALUE,attributeCell.getWeight());
		action.setValue(scoreCell.getValue());
		rhs.addAction(action);
		return rule;
	}
	
	private CardCell fetchCell(List<CardCell> cells,int row,int col){
		for(CardCell cell:cells){
			if(cell.getRow()==row && cell.getCol()==col){
				return cell;
			}
		}
		throw new RuleException("CardCell ["+row+","+col+"] not exist.");
	}
	
	@Override
	public ResourceType getType() {
		return ResourceType.Scorecard;
	}
	@Override
	public boolean support(Element root) {
		return scorecardDeserializer.support(root);
	}
	
	public void setScorecardDeserializer(ScorecardDeserializer scorecardDeserializer) {
		this.scorecardDeserializer = scorecardDeserializer;
	}
	public void setResourceLibraryBuilder(ResourceLibraryBuilder resourceLibraryBuilder) {
		this.resourceLibraryBuilder = resourceLibraryBuilder;
	}
	public void setReteBuilder(ReteBuilder reteBuilder) {
		this.reteBuilder = reteBuilder;
	}
	public void setRulesRebuilder(RulesRebuilder rulesRebuilder) {
		this.rulesRebuilder = rulesRebuilder;
	}
}
