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

import com.bstek.urule.model.flow.Connection;
import com.bstek.urule.parse.CriterionParser;
import com.bstek.urule.parse.Parser;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public abstract class FlowNodeParser<T> implements ApplicationContextAware,Parser<T> {
	protected Collection<CriterionParser> criterionParsers;
	protected List<Connection> parseConnections(Element element){
		List<Connection> connections=new ArrayList<Connection>();
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(!ele.getName().equals("connection")){
				continue;
			}
			connections.add(buildConnection(ele));
		}
		return connections;
	}
	private Connection buildConnection(Element element){
		Connection conn=new Connection();
		conn.setName(element.attributeValue("name"));
		conn.setToName(element.attributeValue("to"));
		conn.setG(element.attributeValue("g"));
		String script=element.getStringValue();
		if(StringUtils.isNotEmpty(script)){
			conn.setScript(script);
		}
		return conn;
	}
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		criterionParsers=applicationContext.getBeansOfType(CriterionParser.class).values();
	}
}
