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
