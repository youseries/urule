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
package com.bstek.urule.parse.flow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.flow.FlowNode;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.LibraryType;
import com.bstek.urule.parse.Parser;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
@SuppressWarnings("rawtypes")
public class FlowDefinitionParser implements Parser<FlowDefinition>,ApplicationContextAware {
	private Collection<FlowNodeParser> nodeParsers;
	public FlowDefinition parse(Element element) {
		FlowDefinition flow=new FlowDefinition();
		flow.setId(element.attributeValue("id"));
		String debug=element.attributeValue("debug");
		if(StringUtils.isNotBlank(debug)){
			flow.setDebug(Boolean.valueOf(debug));
		}
		List<FlowNode> nodes=new ArrayList<FlowNode>();
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			String name=ele.getName();
			if(name.equals("import-variable-library")){
				flow.addLibrary(buildLibrary(ele, LibraryType.Variable));
			}else if(name.equals("import-constant-library")){
				flow.addLibrary(buildLibrary(ele, LibraryType.Constant));
			}else if(name.equals("import-action-library")){
				flow.addLibrary(buildLibrary(ele, LibraryType.Action));
			}else if(name.equals("import-parameter-library")){
				flow.addLibrary(buildLibrary(ele, LibraryType.Parameter));
			}else{
				for(FlowNodeParser parser:nodeParsers){
					if(parser.support(ele.getName())){
						nodes.add((FlowNode)parser.parse(ele));
						break;
					}
				}
			}
		}
		flow.setNodes(nodes);
		flow.buildConnectionToNode();
		return flow;
	}
	private Library buildLibrary(Element ele, LibraryType type) {
		String path=ele.attributeValue("path");
		if(path.endsWith(".xml")){
			Library lib=new Library(path,null,type);
			return lib;
		}else{
			int versionPos=path.lastIndexOf(":");
			String version=path.substring(versionPos+1,path.length());
			if(version.equals("LATEST"))version=null;
			path=path.substring(0,versionPos);
			Library lib=new Library(path,version,type);
			return lib;
		}
	}
	public boolean support(String name) {
		return name.equals("rule-flow");
	}
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		nodeParsers=applicationContext.getBeansOfType(FlowNodeParser.class).values();
	}
}
