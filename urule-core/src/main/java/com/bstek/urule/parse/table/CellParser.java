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
