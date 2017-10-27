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
package com.bstek.urule.model.flow;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.LibraryType;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;

/**
 * @author Jacky.gao
 * @since 2015年2月28日
 */
public class Connection {
	public static final String RETURN_VALUE_KEY="return_value__";
	private String name;
	private String toName;
	private String script;
	private String g;
	private KnowledgePackageWrapper knowledgePackageWrapper;
	@JsonIgnore
	private FlowNode to;
	public boolean evaluate(FlowContext context){
		if(knowledgePackageWrapper==null){
			return true;
		}
		KnowledgeSession parentSession=(KnowledgeSession)context.getWorkingMemory();
		List<Object> facts=parentSession.getAllFacts();
		KnowledgeSession session=KnowledgeSessionFactory.newKnowledgeSession(knowledgePackageWrapper.getKnowledgePackage());
		for(Object fact:facts){
			session.insert(fact);
		}
		session.fireRules(context.getVariables());
		Object result=session.getParameter(Connection.RETURN_VALUE_KEY);
		if(result==null){
			return false;
		}
		return Boolean.valueOf(result.toString());
	}
	
	public void buildDeserialize(){
		if(knowledgePackageWrapper!=null){
			knowledgePackageWrapper.buildDeserialize();
		}
	}
	
	public void execute(FlowContext context,FlowInstance instance){
		to.enter(context, instance);
	}
	
	public String buildDSLScript(List<Library> libraries){
		StringBuffer sb=new StringBuffer();
		if(libraries!=null){
			for(Library lib:libraries){
				String path=lib.getPath();
				if(lib.getVersion()!=null){
					path+=":"+lib.getVersion();
				}
				LibraryType type=lib.getType();
				switch(type){
				case Action:
					sb.append("importActionLibrary \""+path+"\"");
					sb.append("\r\n");
					break;
				case Constant:
					sb.append("importConstantLibrary \""+path+"\"");
					sb.append("\r\n");
					break;
				case Parameter:
					sb.append("importParameterLibrary \""+path+"\"");
					sb.append("\r\n");
					break;
				case Variable:
					sb.append("importVariableLibrary \""+path+"\"");
					sb.append("\r\n");
					break;
				}
			}
		}
		sb.append("rule \"conn\"");
		sb.append("\r\n");
		sb.append("if");
		sb.append("\r\n");
		sb.append(script);
		sb.append("\r\n");
		sb.append("then");
		sb.append("\r\n");
		sb.append("parameter."+RETURN_VALUE_KEY+"=true");
		sb.append("\r\n");
		sb.append("end");
		return sb.toString();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public FlowNode getTo() {
		return to;
	}

	public void setTo(FlowNode to) {
		this.to = to;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public KnowledgePackageWrapper getKnowledgePackageWrapper() {
		return knowledgePackageWrapper;
	}
	public void setKnowledgePackageWrapper(KnowledgePackageWrapper knowledgePackageWrapper) {
		this.knowledgePackageWrapper = knowledgePackageWrapper;
	}

	public String getG() {
		return g;
	}

	public void setG(String g) {
		this.g = g;
	}
}
