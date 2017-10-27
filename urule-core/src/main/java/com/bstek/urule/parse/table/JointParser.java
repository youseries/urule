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

import com.bstek.urule.model.rule.Op;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.model.table.Joint;
import com.bstek.urule.model.table.JointType;
import com.bstek.urule.parse.Parser;
import com.bstek.urule.parse.ValueParser;

/**
 * @author Jacky.gao
 * @since 2015年1月19日
 */
public class JointParser implements Parser<Joint> {
	private ValueParser valueParser;
	public Joint parse(Element element) {
		Joint joint=new Joint();
		joint.setType(JointType.valueOf(element.attributeValue("type")));
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(ele.getName().equals("condition")){
				joint.addCondition(parseCondition(ele));
			}else if(support(ele.getName())){
				joint.addJoint(parse(ele));
			}
		}
		return joint;
	}
	public Condition parseCondition(Element element) {
		Condition condition=new Condition();
		condition.setOp(Op.valueOf(element.attributeValue("op")));
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(valueParser.support(ele.getName())){
				condition.setValue(valueParser.parse(ele));
				break;
			}
		}
		return condition;
	}
	public boolean support(String name) {
		return name.equals("joint");
	}
	public void setValueParser(ValueParser valueParser) {
		this.valueParser = valueParser;
	}
}
