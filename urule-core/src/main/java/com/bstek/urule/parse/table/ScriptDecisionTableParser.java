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

import org.dom4j.Element;

import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.LibraryType;
import com.bstek.urule.model.table.ScriptDecisionTable;
import com.bstek.urule.parse.Parser;

/**
 * @author Jacky.gao
 * @since 2015年1月19日
 */
public class ScriptDecisionTableParser implements Parser<ScriptDecisionTable> {
	private RowParser rowParser;
	private ColumnParser columnParser;
	private ScriptCellParser scriptCellParser;
	public ScriptDecisionTable parse(Element element) {
		ScriptDecisionTable table =new ScriptDecisionTable();
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
			}else if(scriptCellParser.support(name)){
				table.addCell(scriptCellParser.parse(ele));
			}if(name.equals("import-variable-library")){
				table.addLibrary(new Library(ele.attributeValue("path"),null,LibraryType.Variable));
			}else if(name.equals("import-constant-library")){
				table.addLibrary(new Library(ele.attributeValue("path"),null,LibraryType.Constant));
			}else if(name.equals("import-action-library")){
				table.addLibrary(new Library(ele.attributeValue("path"),null,LibraryType.Action));
			}else if(name.equals("import-parameter-library")){
				table.addLibrary(new Library(ele.attributeValue("path"),null,LibraryType.Parameter));
			}
		}
		return table;
	}
	public boolean support(String name) {
		return name.equals("script-decision-table");
	}
	public void setColumnParser(ColumnParser columnParser) {
		this.columnParser = columnParser;
	}
	public void setRowParser(RowParser rowParser) {
		this.rowParser = rowParser;
	}
	public void setScriptCellParser(ScriptCellParser scriptCellParser) {
		this.scriptCellParser = scriptCellParser;
	}
}
