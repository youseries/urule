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
public class ScriptDecisionTableReferenceUpdater extends AbstractReferenceUpdater {
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
		return path.endsWith(FileType.ScriptDecisionTable.toString());
	}
}
