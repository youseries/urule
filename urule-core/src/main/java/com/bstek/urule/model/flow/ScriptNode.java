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

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.LibraryType;

/**
 * @author Jacky.gao
 * @since 2015年4月22日
 */
public class ScriptNode extends BindingNode {
	private String script;
	private FlowNodeType type=FlowNodeType.Script;
	@Override
	public void enterNode(FlowContext context, FlowInstance instance) {
		instance.setCurrentNode(this);
		executeNodeEvent(EventType.enter, context, instance);
		executeKnowledgePackage(context, instance);
		executeNodeEvent(EventType.leave, context, instance);
		leave(null, context, instance);
	}

	@Override
	public FlowNodeType getType() {
		return type;
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
		sb.append("rule \"sr\" ");
		sb.append(" ");
		sb.append("if");
		sb.append(" ");
		sb.append("then");
		sb.append(" ");
		if(script!=null){
			sb.append(script);
		}
		sb.append(" ");
		sb.append("end");
		sb.append(" ");
		return sb.toString();
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
}
