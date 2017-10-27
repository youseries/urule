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
