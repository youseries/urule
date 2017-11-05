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
