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
