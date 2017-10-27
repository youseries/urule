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
