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
package com.bstek.urule.parse.scorecard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.bstek.urule.Configure;
import com.bstek.urule.RuleException;
import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.LibraryType;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.scorecard.AssignTargetType;
import com.bstek.urule.model.scorecard.AttributeRow;
import com.bstek.urule.model.scorecard.CardCell;
import com.bstek.urule.model.scorecard.CustomCol;
import com.bstek.urule.model.scorecard.ScorecardDefinition;
import com.bstek.urule.model.scorecard.ScoringType;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.model.table.Joint;
import com.bstek.urule.parse.Parser;

/**
 * @author Jacky.gao
 * @since 2016年9月22日
 */
public class ScorecardParser implements Parser<ScorecardDefinition> {
	private CardCellParser cardCellParser;
	private AttributeRowParser attributeRowParser=new AttributeRowParser();
	private CustomColParser customColParser=new CustomColParser();
	private RulesRebuilder rulesRebuilder;
	@Override
	public ScorecardDefinition parse(Element element) {
		ScorecardDefinition card=new ScorecardDefinition();
		card.setName(element.attributeValue("name"));
		card.setScoringType(ScoringType.valueOf(element.attributeValue("scoring-type")));
		card.setAssignTargetType(AssignTargetType.valueOf(element.attributeValue("assign-target-type")));
		card.setVariableCategory(element.attributeValue("var-category"));
		card.setVariableName(element.attributeValue("var"));
		card.setVariableLabel(element.attributeValue("var-label"));
		
		String dt=element.attributeValue("datatype");
		if(StringUtils.isNotBlank(dt)){			
			card.setDatatype(Datatype.valueOf(dt));
		}
		card.setScoringBean(element.attributeValue("custom-scoring-bean"));
		String salience=element.attributeValue("salience");
		if(StringUtils.isNotEmpty(salience)){
			card.setSalience(Integer.valueOf(salience));
		}
		String effectiveDate=element.attributeValue("effective-date");
		SimpleDateFormat sd=new SimpleDateFormat(Configure.getDateFormat());
		if(StringUtils.isNotEmpty(effectiveDate)){
			try {
				card.setEffectiveDate(sd.parse(effectiveDate));
			} catch (ParseException e) {
				throw new RuleException(e);
			}
		}
		String expiresDate=element.attributeValue("expires-date");
		if(StringUtils.isNotEmpty(expiresDate)){
			try {
				card.setExpiresDate(sd.parse(expiresDate));
			} catch (ParseException e) {
				throw new RuleException(e);
			}
		}
		String enabled=element.attributeValue("enabled");
		if(StringUtils.isNotEmpty(enabled)){
			card.setEnabled(Boolean.valueOf(enabled));
		}
		String debug=element.attributeValue("debug");
		if(StringUtils.isNotEmpty(debug)){
			card.setDebug(Boolean.valueOf(debug));
		}
		
		card.setAttributeColWidth(element.attributeValue("attr-col-width"));
		card.setAttributeColName(element.attributeValue("attr-col-name"));
		card.setAttributeColVariableCategory(element.attributeValue("attr-col-category"));
		
		card.setConditionColName(element.attributeValue("condition-col-name"));
		card.setConditionColWidth(element.attributeValue("condition-col-width"));
		
		card.setScoreColName(element.attributeValue("score-col-name"));
		card.setScoreColWidth(element.attributeValue("score-col-width"));
		
		String weightSupport=element.attributeValue("weight-support");
		if(StringUtils.isNotBlank(weightSupport)){
			card.setWeightSupport(Boolean.valueOf(weightSupport));			
		}
		
		List<CardCell> cells=new ArrayList<CardCell>();
		List<AttributeRow> rows=new ArrayList<AttributeRow>();
		List<CustomCol> cols=new ArrayList<CustomCol>();
		card.setCells(cells);
		card.setRows(rows);
		card.setCustomCols(cols);
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			String name=ele.getName();
			if(cardCellParser.support(name)){
				cells.add(cardCellParser.parse(ele));
			}else if(attributeRowParser.support(name)){
				rows.add(attributeRowParser.parse(ele));
			}else if(customColParser.support(name)){
				cols.add(customColParser.parse(ele));
			}else if(name.equals("import-variable-library")){
				card.addLibrary(new Library(ele.attributeValue("path"),null,LibraryType.Variable));
			}else if(name.equals("import-constant-library")){
				card.addLibrary(new Library(ele.attributeValue("path"),null,LibraryType.Constant));
			}else if(name.equals("import-action-library")){
				card.addLibrary(new Library(ele.attributeValue("path"),null,LibraryType.Action));
			}else if(name.equals("import-parameter-library")){
				card.addLibrary(new Library(ele.attributeValue("path"),null,LibraryType.Parameter));
			}else if(name.equals("remark")){
				card.setRemark(ele.getText());
			}
		}
		rebuildScorecard(card);
		return card;
	}
	
	private void rebuildScorecard(ScorecardDefinition card){
		ResourceLibrary resLibraries=rulesRebuilder.getResourceLibraryBuilder().buildResourceLibrary(card.getLibraries());
		String category=card.getVariableCategory();
		String name=card.getVariableName();
		if(StringUtils.isNotBlank(category) && StringUtils.isNotBlank(name)){
			Variable variable=rulesRebuilder.getVariableByName(resLibraries.getVariableCategories(), category, name, null);
			card.setVariableLabel(variable.getLabel());
			card.setDatatype(variable.getType());
		}
		String colCategory=card.getAttributeColVariableCategory();
		List<CardCell> cells=card.getCells();
		if(cells==null)return;
		for(CardCell cell:cells){
			Joint joint=cell.getJoint();
			if(joint!=null && joint.getConditions()!=null){
				for(Condition condition:joint.getConditions()){
					if(condition==null){
						continue;
					}
					Value value=condition.getValue();
					if(value!=null){
						rulesRebuilder.rebuildValue(value, resLibraries, null, false);
					}
				}
			}
			Value value=cell.getValue();
			if(value!=null){
				rulesRebuilder.rebuildValue(value, resLibraries, null, false);
			}
			String varName=cell.getVariableName();
			if(StringUtils.isNotBlank(varName)){
				Variable variable=rulesRebuilder.getVariableByName(resLibraries.getVariableCategories(), colCategory, varName, null);
				cell.setDatatype(variable.getType());
				cell.setVariableLabel(variable.getLabel());
			}
		}
	}
	
	public void setCardCellParser(CardCellParser cardCellParser) {
		this.cardCellParser = cardCellParser;
	}
	
	public void setRulesRebuilder(RulesRebuilder rulesRebuilder) {
		this.rulesRebuilder = rulesRebuilder;
	}
	
	@Override
	public boolean support(String name) {
		return name.equals("scorecard");
	}
}
