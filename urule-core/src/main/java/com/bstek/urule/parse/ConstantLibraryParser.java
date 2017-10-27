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
package com.bstek.urule.parse;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.constant.Constant;
import com.bstek.urule.model.library.constant.ConstantCategory;
import com.bstek.urule.model.library.constant.ConstantLibrary;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class ConstantLibraryParser implements Parser<ConstantLibrary> {
	public static final String BEAN_ID="urule.constantLibraryParser";
	public ConstantLibrary parse(Element element) {
		List<ConstantCategory> categories=new ArrayList<ConstantCategory>();
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(ele.getName().equals("category")){
				categories.add(parseConstantCategory(ele));
			}
		}
		ConstantLibrary library=new ConstantLibrary();
		library.setCategories(categories);
		return library;
	}
	private ConstantCategory parseConstantCategory(Element element){
		ConstantCategory category=new ConstantCategory();
		category.setName(element.attributeValue("name"));
		category.setLabel(element.attributeValue("label"));
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(ele.getName().equals("constant")){
				category.addConstant(parseConstant(ele));
			}
		}
		return category;
	}
	private Constant parseConstant(Element element){
		Constant constant=new Constant();
		constant.setName(element.attributeValue("name"));
		constant.setLabel(element.attributeValue("label"));
		constant.setType(Datatype.valueOf(element.attributeValue("type")));
		return constant;
	}
	public boolean support(String name) {
		return name.equals("constant-library");
	}
}
