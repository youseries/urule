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

import java.util.Collection;

import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.urule.model.table.Cell;
import com.bstek.urule.parse.ActionParser;
import com.bstek.urule.parse.Parser;
import com.bstek.urule.parse.ValueParser;

/**
 * @author Jacky.gao
 * @since 2015年1月19日
 */
public class CellParser implements Parser<Cell>,ApplicationContextAware {
	private JointParser jointParser;
	private ValueParser valueParser;
	private Collection<ActionParser> actionParsers;
	public Cell parse(Element element) {
		Cell cell=new Cell();
		cell.setRow(Integer.valueOf(element.attributeValue("row")));
		cell.setCol(Integer.valueOf(element.attributeValue("col")));
		cell.setRowspan(Integer.valueOf(element.attributeValue("rowspan")));
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			String name=ele.getName();
			if(jointParser.support(name)){
				cell.setJoint(jointParser.parse(ele));
			}else if(valueParser.support(name)){
				cell.setValue(valueParser.parse(ele));
			}else{
				for(ActionParser parser:actionParsers){
					if(parser.support(name)){
						cell.setAction(parser.parse(ele));
						break;
					}
				}
			}
		}
		return cell;
	}
	public boolean support(String name) {
		return name.equals("cell");
	}
	public void setJointParser(JointParser jointParser) {
		this.jointParser = jointParser;
	}
	public void setValueParser(ValueParser valueParser) {
		this.valueParser = valueParser;
	}
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		actionParsers=applicationContext.getBeansOfType(ActionParser.class).values();
	}
}
