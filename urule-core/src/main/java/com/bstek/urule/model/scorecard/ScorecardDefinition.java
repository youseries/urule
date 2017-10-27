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
