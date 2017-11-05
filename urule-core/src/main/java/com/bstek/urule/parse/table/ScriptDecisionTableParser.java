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
