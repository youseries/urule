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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bstek.urule.RuleException;
import com.bstek.urule.action.AbstractAction;
import com.bstek.urule.action.Action;
import com.bstek.urule.action.ConsolePrintAction;
import com.bstek.urule.action.VariableAssignAction;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Lhs;
import com.bstek.urule.model.table.Cell;
import com.bstek.urule.model.table.Column;
import com.bstek.urule.model.table.ColumnType;
import com.bstek.urule.model.table.DecisionTable;
import com.bstek.urule.model.table.Row;

/**
 * @author Jacky.gao
 * @since 2015年1月20日
 */
public class DecisionTableRulesBuilder {
	private CellContentBuilder cellContentBuilder;
	public List<Rule> buildRules(DecisionTable table){
		List<Rule> rules=new ArrayList<Rule>();
		List<Row> rows=table.getRows();
		List<Column> columns=table.getColumns();
		for(Row row:rows){
			Rule rule=new Rule();
			rule.setSalience(table.getSalience());
			rule.setExpiresDate(table.getExpiresDate());
			rule.setEffectiveDate(table.getEffectiveDate());
			rule.setEnabled(table.getEnabled());
			rule.setName("r"+row.getNum());
			Lhs lhs=new Lhs();
			And and=new And();
			lhs.setCriterion(and);
			rule.setLhs(lhs);
			Rhs rhs=new Rhs();
			rule.setRhs(rhs);
			rules.add(rule);
			Value value=null;
			for(Column col:columns){
				Cell cell=getCell(table,row.getNum(),col.getNum());
				ColumnType type=col.getType();
				switch(type){
				case Criteria:
					Criterion criterion=cellContentBuilder.buildCriterion(cell,col);
					if(criterion!=null){
						and.addCriterion(criterion);						
					}
					break;
				case ConsolePrint:
					value=cell.getValue();
					if(value!=null){
						ConsolePrintAction consolePrintAction=new ConsolePrintAction();
						consolePrintAction.setPriority(1000-col.getNum());
						consolePrintAction.setValue(value);
						rhs.addAction(consolePrintAction);
					}
					break;
				case Assignment:
					value=cell.getValue();
					if(value!=null){
						VariableAssignAction variableAssignAction=new VariableAssignAction();
						variableAssignAction.setPriority(1000-col.getNum());
						variableAssignAction.setValue(value);
						variableAssignAction.setDatatype(col.getDatatype());
						variableAssignAction.setVariableName(col.getVariableName());
						variableAssignAction.setVariableLabel(col.getVariableLabel());
						variableAssignAction.setVariableCategory(col.getVariableCategory());
						rhs.addAction(variableAssignAction);
					}
					break;
				case ExecuteMethod:
					Action action=cell.getAction();
					if(action!=null){
						AbstractAction aa=(AbstractAction)action;
						aa.setPriority(1000-col.getNum());
						rhs.addAction(aa);
					}
					break;
				}
			}
		}
		return rules;
	}
	private Cell getCell(DecisionTable table,int row,int column){
		Map<String,Cell> cellMap=table.getCellMap();
		Cell cell=null;
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
	public void setCellContentBuilder(CellContentBuilder cellContentBuilder) {
		this.cellContentBuilder = cellContentBuilder;
	}
}
