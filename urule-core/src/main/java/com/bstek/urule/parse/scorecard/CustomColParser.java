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

import org.dom4j.Element;

import com.bstek.urule.model.scorecard.CustomCol;
import com.bstek.urule.parse.Parser;

/**
 * @author Jacky.gao
 * @since 2016年9月22日
 */
public class CustomColParser implements Parser<CustomCol> {
	@Override
	public CustomCol parse(Element element) {
		CustomCol col=new CustomCol();
		col.setColNumber(Integer.parseInt(element.attributeValue("col-number")));
		col.setName(element.attributeValue("name"));
		col.setWidth(element.attributeValue("width"));
		return col;
	}
	@Override
	public boolean support(String name) {
		return name.equals("custom-col");
	}
}
