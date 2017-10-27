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

import com.bstek.urule.model.library.constant.ConstantLibrary;
import com.bstek.urule.parse.ConstantLibraryParser;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class ConstantLibraryDeserializer implements Deserializer<ConstantLibrary> {
	public static final String BEAN_ID="urule.constantLibraryDeserializer";
	private ConstantLibraryParser constantLibraryParser;
	public ConstantLibrary deserialize(Element root) {
		return constantLibraryParser.parse(root);
	}
	public boolean support(Element root) {
		return constantLibraryParser.support(root.getName());
	}
	public void setConstantLibraryParser(
			ConstantLibraryParser constantLibraryParser) {
		this.constantLibraryParser = constantLibraryParser;
	}
}
