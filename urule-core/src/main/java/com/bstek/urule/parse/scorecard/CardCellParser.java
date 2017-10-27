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
