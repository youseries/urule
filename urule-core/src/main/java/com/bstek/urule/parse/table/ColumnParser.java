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
