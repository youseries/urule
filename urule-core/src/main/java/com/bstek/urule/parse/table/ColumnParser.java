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

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.table.Column;
import com.bstek.urule.model.table.ColumnType;
import com.bstek.urule.parse.Parser;

/**
 * @author Jacky.gao
 * @since 2015年1月19日
 */
public class ColumnParser implements Parser<Column> {
	public Column parse(Element element) {
		Column col=new Column();
		col.setNum(Integer.valueOf(element.attributeValue("num")));
		col.setType(ColumnType.valueOf(element.attributeValue("type")));
		col.setVariableCategory(element.attributeValue("var-category"));
		col.setVariableLabel(element.attributeValue("var-label"));
		col.setVariableName(element.attributeValue("var"));
		col.setWidth(Integer.valueOf(element.attributeValue("width")));
		String datatype=element.attributeValue("datatype");
		if(StringUtils.isNotEmpty(datatype)){
			col.setDatatype(Datatype.valueOf(datatype));			
		}
		return col;
	}
	public boolean support(String name) {
		return name.equals("col");
	}
}
