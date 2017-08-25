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

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.scorecard.CardCell;
import com.bstek.urule.model.scorecard.CellType;
import com.bstek.urule.parse.Parser;
import com.bstek.urule.parse.ValueParser;
import com.bstek.urule.parse.table.JointParser;

/**
 * @author Jacky.gao
 * @since 2016年9月22日
 */
public class CardCellParser implements Parser<CardCell> {
	private ValueParser valueParser;
	private JointParser jointParser;
	@Override
	public CardCell parse(Element element) {
		CardCell cell=new CardCell();
		cell.setType(CellType.valueOf(element.attributeValue("type")));
		cell.setCol(Integer.valueOf(element.attributeValue("col")));
		cell.setRow(Integer.valueOf(element.attributeValue("row")));
		String datatype=element.attributeValue("datatype");
		if(StringUtils.isNotBlank(datatype)){
			cell.setDatatype(Datatype.valueOf(datatype));
		}
		cell.setVariableName(element.attributeValue("var"));
		cell.setVariableLabel(element.attributeValue("var-label"));
		cell.setWeight(element.attributeValue("weight"));
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(valueParser.support(ele.getName())){
				cell.setValue(valueParser.parse(ele));
			}else if(jointParser.support(ele.getName())){
				cell.setJoint(jointParser.parse(ele));
			}
		}
		return cell;
	}
	
	public void setJointParser(JointParser jointParser) {
		this.jointParser = jointParser;
	}
	public void setValueParser(ValueParser valueParser) {
		this.valueParser = valueParser;
	}
	
	@Override
	public boolean support(String name) {
		return name.equals("card-cell");
	}
}
