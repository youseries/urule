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

import com.bstek.urule.model.table.ScriptCell;
import com.bstek.urule.parse.Parser;

/**
 * @author Jacky.gao
 * @since 2015年1月19日
 */
public class ScriptCellParser implements Parser<ScriptCell>{
	public ScriptCell parse(Element element) {
		ScriptCell cell=new ScriptCell();
		cell.setRow(Integer.valueOf(element.attributeValue("row")));
		cell.setCol(Integer.valueOf(element.attributeValue("col")));
		cell.setRowspan(Integer.valueOf(element.attributeValue("rowspan")));
		cell.setScript(element.getStringValue());
		return cell;
	}
	public boolean support(String name) {
		return name.equals("script-cell");
	}
}
