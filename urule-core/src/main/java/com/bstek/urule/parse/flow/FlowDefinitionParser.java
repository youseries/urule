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
package com.bstek.urule.parse.flow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
