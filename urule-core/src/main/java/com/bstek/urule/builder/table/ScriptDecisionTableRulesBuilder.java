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
package com.bstek.urule.builder.table;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.urule.RuleException;
import com.bstek.urule.dsl.DSLRuleSetBuilder;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.LibraryType;
import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.model.table.Column;
import com.bstek.urule.model.table.ColumnType;
import com.bstek.urule.model.table.Row;
import com.bstek.urule.model.table.ScriptCell;
import com.bstek.urule.model.table.ScriptDecisionTable;

/**
 * @author Jacky.gao
 * @since 2015年1月20日
 */
public class ScriptDecisionTableRulesBuilder {
	private CellScriptDSLBuilder cellScriptDSLBuilder;
	private DSLRuleSetBuilder dslRuleSetBuilder;
	public RuleSet buildRules(ScriptDecisionTable table) throws IOException{
		List<Row> rows=table.getRows();
		List<Column> columns=table.getColumns();
		List<Library> libraries=table.getLibraries();
		StringBuffer sb = buildLibraryScript(libraries);
		for(Row row:rows){
			sb.append("rule \"r"+row.getNum()+"\"");
			sb.append("\r\n");
			sb.append("if");
			sb.append("\r\n");
			StringBuffer criteriasSb=new StringBuffer();
			StringBuffer actionsSb=new StringBuffer();
			for(Column col:columns){
				ScriptCell cell=getCell(table,row.getNum(),col.getNum());
				String script=cell.getScript();
				if(StringUtils.isBlank(script)){
					continue;
				}
				ColumnType type=col.getType();
				switch(type){
				case Criteria:
					String propertyName=col.getVariableCategory()+"."+col.getVariableLabel();
					String newScript=cellScriptDSLBuilder.buildCriteriaScript(script, propertyName);
					if(StringUtils.isBlank(newScript)){
						continue;
					}
					newScript=newScript.trim();
					if(criteriasSb.length()>1){
						criteriasSb.append(" and ");
					}
					if(!newScript.startsWith("(")){
						newScript="("+newScript+")";
					}
					criteriasSb.append(newScript);
					break;
				case ConsolePrint:
					actionsSb.append("out("+script+");\r\n");
					break;
				case Assignment:
					propertyName=col.getVariableCategory()+"."+col.getVariableLabel();
					actionsSb.append(propertyName+" = "+script+";\r\n");
					break;
				case ExecuteMethod:
					actionsSb.append(script+";\r\n");
					break;
				}
			}
			sb.append(criteriasSb);
			sb.append("\r\n");
			sb.append("then");
			sb.append("\r\n");
			sb.append(actionsSb);
			sb.append("\r\n");
			sb.append("end;");
			sb.append("\r\n");
		}
		RuleSet ruleSet=dslRuleSetBuilder.build(sb.toString());
		return ruleSet;
	}
	private StringBuffer buildLibraryScript(List<Library> libraries) {
		StringBuffer sb=new StringBuffer();
		for(Library lib:libraries){
			LibraryType type=lib.getType();
			switch(type){
			case Action:
				sb.append("importActionLibrary \""+lib.getPath()+"\";\r\n");
				break;
			case Constant:
				sb.append("importConstantLibrary \""+lib.getPath()+"\";\r\n");
				break;
			case Parameter:
				sb.append("importParameterLibrary \""+lib.getPath()+"\";\r\n");
				break;
			case Variable:
				sb.append("importVariableLibrary \""+lib.getPath()+"\";\r\n");
				break;
			}
		}
		return sb;
	}
	private ScriptCell getCell(ScriptDecisionTable table,int row,int column){
		Map<String,ScriptCell> cellMap=table.getCellMap();
		ScriptCell cell=null;
		for(int i=row;i>-1;i--){
			String key=table.buildCellKey(i,column);
			if(cellMap.containsKey(key)){
				cell=cellMap.get(key);
				break;
			}
		}
		if(cell==null){
			throw new RuleException("Decision table cell["+row+","+column+"] not exist.");
		}
		return cell;
	}
	
	public void setCellScriptDSLBuilder(CellScriptDSLBuilder cellScriptDSLBuilder) {
		this.cellScriptDSLBuilder = cellScriptDSLBuilder;
	}
	public void setDslRuleSetBuilder(DSLRuleSetBuilder dslRuleSetBuilder) {
		this.dslRuleSetBuilder = dslRuleSetBuilder;
	}
}
