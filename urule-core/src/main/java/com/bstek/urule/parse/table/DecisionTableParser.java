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
package com.bstek.urule.parse.table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.bstek.urule.Configure;
import com.bstek.urule.RuleException;
import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.LibraryType;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.table.Cell;
import com.bstek.urule.model.table.Column;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.model.table.DecisionTable;
import com.bstek.urule.parse.Parser;

/**
 * @author Jacky.gao
 * @since 2015年1月19日
 */
public class DecisionTableParser implements Parser<DecisionTable> {
	private RowParser rowParser;
	private ColumnParser columnParser;
	private CellParser cellParser;
	private RulesRebuilder rulesRebuilder;
	public DecisionTable parse(Element element) {
		DecisionTable table =new DecisionTable();
		String salience=element.attributeValue("salience");
		if(StringUtils.isNotEmpty(salience)){
			table.setSalience(Integer.valueOf(salience));
		}
		String effectiveDate=element.attributeValue("effective-date");
		SimpleDateFormat sd=new SimpleDateFormat(Configure.getDateFormat());
		if(StringUtils.isNotEmpty(effectiveDate)){
			try {
				table.setEffectiveDate(sd.parse(effectiveDate));
			} catch (ParseException e) {
				throw new RuleException(e);
			}
		}
		String expiresDate=element.attributeValue("expires-date");
		if(StringUtils.isNotEmpty(expiresDate)){
			try {
				table.setExpiresDate(sd.parse(expiresDate));
			} catch (ParseException e) {
				throw new RuleException(e);
			}
		}
		String enabled=element.attributeValue("enabled");
		if(StringUtils.isNotEmpty(enabled)){
			table.setEnabled(Boolean.valueOf(enabled));
		}
		
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			String name=ele.getName();
			if(rowParser.support(name)){
				table.addRow(rowParser.parse(ele));
			}else if(columnParser.support(name)){
				table.addColumn(columnParser.parse(ele));
			}else if(cellParser.support(name)){
				table.addCell(cellParser.parse(ele));
			}if(name.equals("import-variable-library")){
				table.addLibrary(new Library(ele.attributeValue("path"),null,LibraryType.Variable));
			}else if(name.equals("import-constant-library")){
				table.addLibrary(new Library(ele.attributeValue("path"),null,LibraryType.Constant));
			}else if(name.equals("import-action-library")){
				table.addLibrary(new Library(ele.attributeValue("path"),null,LibraryType.Action));
			}else if(name.equals("import-parameter-library")){
				table.addLibrary(new Library(ele.attributeValue("path"),null,LibraryType.Parameter));
			}else if(name.equals("remark")){
				table.setRemark(ele.getText());
			}
		}
		rebuildTable(table);
		return table;
	}
	
	private void rebuildTable(DecisionTable table){
		List<Library> libraries=table.getLibraries();
		ResourceLibrary resLibraries=rulesRebuilder.getResourceLibraryBuilder().buildResourceLibrary(libraries);
		Map<String,String> namedMap=new HashMap<String,String>();
		for(Cell cell:table.getCellMap().values()){
			if(cell.getAction()!=null){
				rulesRebuilder.rebuildAction(cell.getAction(), resLibraries, namedMap, false);;
			}else if(cell.getValue()!=null){
				rulesRebuilder.rebuildValue(cell.getValue(), resLibraries, namedMap, false);
			}else if(cell.getJoint()!=null){
				if(cell.getJoint()!=null && cell.getJoint().getJunction()!=null){					
					List<Condition> conditions=cell.getJoint().getConditions();
					if(conditions!=null){					
						for(Condition condition:conditions){
							Value value=condition.getValue();
							if(value!=null){
								rulesRebuilder.rebuildValue(value, resLibraries, namedMap, false);													
							}
						}
					}
				}
			}
		}
		for(Column col:table.getColumns()){
			String category=col.getVariableCategory();
			String name=col.getVariableName();
			if(StringUtils.isBlank(category) || StringUtils.isBlank(name)){
				continue;
			}
			Variable variable=rulesRebuilder.getVariableByName(resLibraries.getVariableCategories(), category, name, namedMap);
			col.setDatatype(variable.getType());
			col.setVariableLabel(variable.getLabel());
		}
	}
	
	public boolean support(String name) {
		return name.equals("decision-table");
	}
	public void setColumnParser(ColumnParser columnParser) {
		this.columnParser = columnParser;
	}
	public void setRowParser(RowParser rowParser) {
		this.rowParser = rowParser;
	}
	public void setCellParser(CellParser cellParser) {
		this.cellParser = cellParser;
	}
	public void setRulesRebuilder(RulesRebuilder rulesRebuilder) {
		this.rulesRebuilder = rulesRebuilder;
	}
}
