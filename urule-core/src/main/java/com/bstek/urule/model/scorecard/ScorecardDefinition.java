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
package com.bstek.urule.model.scorecard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Library;

/**
 * @author Jacky.gao
 * @since 2016年9月20日
 */
public class ScorecardDefinition {
	private String name;
	private Integer salience;
	private Date effectiveDate;
	private Date expiresDate;
	private Boolean enabled;
	private Boolean debug;
	
	private String attributeColName;
	private String attributeColWidth;
	private String attributeColVariableCategory;
	
	private String conditionColName;
	private String conditionColWidth;
	
	private String scoreColName;
	private String scoreColWidth;
	
	private boolean weightSupport;
	private ScoringType scoringType;
	private String scoringBean;
	private AssignTargetType assignTargetType;
	
	private String remark;
	private String variableCategory;
	private String variableName;
	private String variableLabel;
	private Datatype datatype;
	
	private List<CardCell> cells;
	private List<CustomCol> customCols;
	private List<AttributeRow> rows;
	private List<Library> libraries;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSalience() {
		return salience;
	}
	public void setSalience(Integer salience) {
		this.salience = salience;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Date getExpiresDate() {
		return expiresDate;
	}
	public void setExpiresDate(Date expiresDate) {
		this.expiresDate = expiresDate;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public Boolean getDebug() {
		return debug;
	}
	public void setDebug(Boolean debug) {
		this.debug = debug;
	}
	public String getAttributeColName() {
		return attributeColName;
	}
	public void setAttributeColName(String attributeColName) {
		this.attributeColName = attributeColName;
	}
	public String getAttributeColWidth() {
		return attributeColWidth;
	}
	public void setAttributeColWidth(String attributeColWidth) {
		this.attributeColWidth = attributeColWidth;
	}
	public String getAttributeColVariableCategory() {
		return attributeColVariableCategory;
	}
	public void setAttributeColVariableCategory(String attributeColVariableCategory) {
		this.attributeColVariableCategory = attributeColVariableCategory;
	}
	public String getConditionColName() {
		return conditionColName;
	}
	public void setConditionColName(String conditionColName) {
		this.conditionColName = conditionColName;
	}
	public String getConditionColWidth() {
		return conditionColWidth;
	}
	public void setConditionColWidth(String conditionColWidth) {
		this.conditionColWidth = conditionColWidth;
	}
	public String getScoreColName() {
		return scoreColName;
	}
	public void setScoreColName(String scoreColName) {
		this.scoreColName = scoreColName;
	}
	public String getScoreColWidth() {
		return scoreColWidth;
	}
	public void setScoreColWidth(String scoreColWidth) {
		this.scoreColWidth = scoreColWidth;
	}
	public boolean isWeightSupport() {
		return weightSupport;
	}
	public void setWeightSupport(boolean weightSupport) {
		this.weightSupport = weightSupport;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Datatype getDatatype() {
		return datatype;
	}
	public void setDatatype(Datatype datatype) {
		this.datatype = datatype;
	}
	public List<CustomCol> getCustomCols() {
		return customCols;
	}
	public void setCustomCols(List<CustomCol> customCols) {
		this.customCols = customCols;
	}
	public List<AttributeRow> getRows() {
		return rows;
	}
	public void setRows(List<AttributeRow> rows) {
		this.rows = rows;
	}
	public List<Library> getLibraries() {
		return libraries;
	}
	public void setLibraries(List<Library> libraries) {
		this.libraries = libraries;
	}
	public void addLibrary(Library library){
		if(libraries==null){
			libraries=new ArrayList<Library>();
		}
		libraries.add(library);
	}
	public List<CardCell> getCells() {
		return cells;
	}
	public void setCells(List<CardCell> cells) {
		this.cells = cells;
	}
}
