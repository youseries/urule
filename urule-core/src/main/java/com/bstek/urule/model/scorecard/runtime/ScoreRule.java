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
package com.bstek.urule.model.scorecard.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.bstek.urule.RuleException;
import com.bstek.urule.Utils;
import com.bstek.urule.action.ActionValue;
import com.bstek.urule.debug.MsgType;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.scorecard.AssignTargetType;
import com.bstek.urule.model.scorecard.ScoringType;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.ValueCompute;

/**
 * @author Jacky.gao
 * @since 2016年9月26日
 */
public class ScoreRule extends Rule{
	private ScoringType scoringType;
	private String scoringBean;
	private AssignTargetType assignTargetType;
	
	private String variableCategory;
	private String variableName;
	private String variableLabel;
	private Datatype datatype;
	@JsonIgnore
	private List<Library> libraries;
	private KnowledgePackageWrapper knowledgePackageWrapper;
	private final Log log=LogFactory.getLog(getClass());
	public List<ActionValue> execute(Context context,Object matchedObject,List<Object> allMatchedObjects,Map<String,Object> variableMap){
		KnowledgeSession parentSession=(KnowledgeSession)context.getWorkingMemory();
		List<Object> facts=parentSession.getAllFacts();
		KnowledgeSession session=KnowledgeSessionFactory.newKnowledgeSession(knowledgePackageWrapper.getKnowledgePackage(),context.getDebugMessageItems());
		for(Object fact:facts){
			session.insert(fact);
		}
		boolean isdebug=false;
		if(this.getDebug()!=null){
			isdebug=this.getDebug();
		}
		List<ActionValue> values=session.fireRules(parentSession.getParameters()).getActionValues();
		Map<Integer,RowItemImpl> rowMap=new HashMap<Integer,RowItemImpl>();
		for(ActionValue value:values){
			if(!(value.getValue() instanceof ScoreRuntimeValue)){
				continue;
			}
			ScoreRuntimeValue scoreValue=(ScoreRuntimeValue)value.getValue();
			int rowNumber=scoreValue.getRowNumber();
			if(isdebug && Utils.isDebug()){
				String msg="---行"+rowNumber+",得分："+scoreValue.getValue();
				context.debugMsg(msg, MsgType.ScoreCard, isdebug);
			}
			RowItemImpl rowItem=null;
			if(rowMap.containsKey(rowNumber)){
				rowItem=rowMap.get(rowNumber);
			}else{
				rowItem=new RowItemImpl();
				rowItem.setRowNumber(rowNumber);
				rowMap.put(rowNumber, rowItem);
			}
			if(scoreValue.getName().equals(ScoreRuntimeValue.SCORE_VALUE)){
				rowItem.setScore(scoreValue.getValue());
				rowItem.setWeight(scoreValue.getWeight());
			}else{
				CellItem cellItem=new CellItem(scoreValue.getName(),scoreValue.getValue());
				rowItem.addCellItem(cellItem);
			}
		}
		List<RowItem> items=new ArrayList<RowItem>();
		items.addAll(rowMap.values());
		ScorecardImpl card=new ScorecardImpl(getName(),items,isdebug);
		Object actualScore=null;
		if(scoringType.equals(ScoringType.sum)){
			actualScore=card.executeSum(context);
		}else if(scoringType.equals(ScoringType.weightsum)){
			actualScore=card.executeWeightSum(context);
		}else if(scoringType.equals(ScoringType.custom)){
			if(isdebug && Utils.isDebug()){
				String msg="---执行自定义评分卡得分计算Bean:"+scoringBean;
				context.debugMsg(msg, MsgType.ScoreCard, isdebug);
			}
			ScoringStrategy scoringStrategy=(ScoringStrategy)context.getApplicationContext().getBean(scoringBean);
			actualScore=scoringStrategy.calculate(card, context);
		}
		if(assignTargetType.equals(AssignTargetType.none)){
			log.warn("Scorecard ["+card.getName()+"] not setting assignment object for score value, score value is :"+actualScore);
		}else{
			Object targetFact=null;
			ValueCompute valueCompute=context.getValueCompute();
			String className=context.getVariableCategoryClass(variableCategory);
			if(className.equals(HashMap.class.getName())){
				targetFact=session.getParameters();
			}else{
				targetFact=valueCompute.findObject(className, matchedObject, context);				
			}
			if(targetFact==null){
				throw new RuleException("Class["+className+"] not found in workingmemory.");
			}
			actualScore=datatype.convert(actualScore);
			Utils.setObjectProperty(targetFact, variableName, actualScore);
		}
		parentSession.getParameters().putAll(session.getParameters());
		return null;
	}

	public ScoringType getScoringType() {
		return scoringType;
	}

	public void setScoringType(ScoringType scoringType) {
		this.scoringType = scoringType;
	}

	public String getScoringBean() {
		return scoringBean;
	}

	public void setScoringBean(String scoringBean) {
		this.scoringBean = scoringBean;
	}

	public AssignTargetType getAssignTargetType() {
		return assignTargetType;
	}

	public void setAssignTargetType(AssignTargetType assignTargetType) {
		this.assignTargetType = assignTargetType;
	}

	public String getVariableCategory() {
		return variableCategory;
	}

	public void setVariableCategory(String variableCategory) {
		this.variableCategory = variableCategory;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getVariableLabel() {
		return variableLabel;
	}

	public void setVariableLabel(String variableLabel) {
		this.variableLabel = variableLabel;
	}

	public Datatype getDatatype() {
		return datatype;
	}

	public void setDatatype(Datatype datatype) {
		this.datatype = datatype;
	}

	public List<Library> getLibraries() {
		return libraries;
	}

	public void setLibraries(List<Library> libraries) {
		this.libraries = libraries;
	}

	public KnowledgePackageWrapper getKnowledgePackageWrapper() {
		return knowledgePackageWrapper;
	}
	public void setKnowledgePackageWrapper(
			KnowledgePackageWrapper knowledgePackageWrapper) {
		this.knowledgePackageWrapper = knowledgePackageWrapper;
	}
}
