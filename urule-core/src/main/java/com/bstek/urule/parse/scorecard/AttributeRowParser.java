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
package com.bstek.urule.parse.scorecard;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.bstek.urule.model.scorecard.AttributeRow;
import com.bstek.urule.model.scorecard.ConditionRow;
import com.bstek.urule.parse.Parser;

/**
 * @author Jacky.gao
 * @since 2016年9月22日
 */
public class AttributeRowParser implements Parser<AttributeRow> {
	@Override
	public AttributeRow parse(Element element) {
		AttributeRow row=new AttributeRow();
		row.setRowNumber(Integer.valueOf(element.attributeValue("row-number")));
		List<ConditionRow> rows=new ArrayList<ConditionRow>();
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(ele.getName().equals("condition-row")){
				ConditionRow r=new ConditionRow();
				r.setRowNumber(Integer.valueOf(ele.attributeValue("row-number")));
				rows.add(r);
			}
		}
		row.setConditionRows(rows);
		return row;
	}
	@Override
	public boolean support(String name) {
		return name.equals("attribute-row");
	}
}
