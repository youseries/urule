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
package com.bstek.urule.parse.deserializer;

import org.dom4j.Element;

import com.bstek.urule.model.decisiontree.DecisionTree;
import com.bstek.urule.parse.decisiontree.DecisionTreeParser;

/**
 * @author Jacky.gao
 * @since 2016年2月29日
 */
public class DecisionTreeDeserializer implements Deserializer<DecisionTree> {
	public static final String BEAN_ID="urule.decisionTreeDeserializer";
	private DecisionTreeParser decisionTreeParser;
	@Override
	public DecisionTree deserialize(Element root) {
		return decisionTreeParser.parse(root);
	}
	public void setDecisionTreeParser(DecisionTreeParser decisionTreeParser) {
		this.decisionTreeParser = decisionTreeParser;
	}
	@Override
	public boolean support(Element root) {
		return decisionTreeParser.support(root.getName());
	}
}
