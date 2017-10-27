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

import java.util.List;

import org.dom4j.Element;

import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.parse.ParameterLibraryParser;

/**
 * @author Jacky.gao
 * @since 2015年3月10日
 */
public class ParameterLibraryDeserializer implements Deserializer<List<Variable>> {
	public static final String BEAN_ID="urule.parameterLibraryDeserializer";
	private ParameterLibraryParser parameterLibraryParser;
	@Override
	public List<Variable> deserialize(Element root) {
		return parameterLibraryParser.parse(root);
	}
	@Override
	public boolean support(Element root) {
		return parameterLibraryParser.support(root.getName());
	}
	public void setParameterLibraryParser(
			ParameterLibraryParser parameterLibraryParser) {
		this.parameterLibraryParser = parameterLibraryParser;
	}
}
