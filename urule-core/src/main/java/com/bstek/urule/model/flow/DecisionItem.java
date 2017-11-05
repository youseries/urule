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


/**
 * @author Jacky.gao
 * @since 2015年4月20日
 */
public class DecisionItem {
	public static final String RETURN_VALUE_KEY="return_to__";
	private String script;
	private int percent;//值为1-99
	private String to;
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public int getPercent() {
		return percent;
	}
	public void setPercent(int percent) {
		this.percent = percent;
	}
	public String buildDSLScript(int index){
		StringBuffer sb=new StringBuffer();
		sb.append("rule \"r"+index+"\"");
		sb.append(" ");
		sb.append("if");
		sb.append(" ");
		sb.append(script);
		sb.append(" ");
		sb.append("then");
		sb.append(" ");
		sb.append("parameter."+RETURN_VALUE_KEY+"=\""+to+"\"");
		sb.append(" ");
		sb.append("end");
		return sb.toString();
	}
}
