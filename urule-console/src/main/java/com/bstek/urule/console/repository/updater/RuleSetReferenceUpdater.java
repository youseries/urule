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
package com.bstek.urule.console.repository.updater;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.bstek.urule.RuleException;
import com.bstek.urule.console.repository.model.FileType;

/**
 * @author Jacky.gao
 * @since 2015年8月4日
 */
public class RuleSetReferenceUpdater extends AbstractReferenceUpdater {
	public boolean contain(String path, String xml) {
		try{
			Document doc=DocumentHelper.parseText(xml);
			Element element=doc.getRootElement();
			for(Object obj:element.elements()){
				if(!(obj instanceof Element)){
					continue;
				}
				Element ele=(Element)obj;
				String name=ele.getName();
				boolean match=false;
				if(name.equals("import-variable-library")){
					match=true;
				}else if(name.equals("import-constant-library")){
					match=true;
				}else if(name.equals("import-action-library")){
					match=true;
				}else if(name.equals("import-parameter-library")){
					match=true;
				}
				if(!match){
					continue;
				}
				String filePath=ele.attributeValue("path");
				if(filePath.endsWith(path)){
					return true;
				}
			}
			return false;
		}catch(Exception ex){
			throw new RuleException(ex);
		}
	}
	public String update(String oldPath, String newPath, String xml) {
		try{
			boolean modify=false;
			Document doc=DocumentHelper.parseText(xml);
			Element element=doc.getRootElement();
			for(Object obj:element.elements()){
				if(!(obj instanceof Element)){
					continue;
				}
				Element ele=(Element)obj;
				String name=ele.getName();
				boolean match=false;
				if(name.equals("import-variable-library")){
					match=true;
				}else if(name.equals("import-constant-library")){
					match=true;
				}else if(name.equals("import-action-library")){
					match=true;
				}else if(name.equals("import-parameter-library")){
					match=true;
				}
				if(!match){
					continue;
				}
				String path=ele.attributeValue("path");
				if(path.endsWith(oldPath)){
					ele.addAttribute("path", newPath);
					modify=true;
				}
			}
			if(modify){
				return xmlToString(doc);
			}
			return null;
		}catch(Exception ex){
			throw new RuleException(ex);
		}
	}

	public boolean support(String path) {
		return path.endsWith(FileType.Ruleset.toString());
	}
}
