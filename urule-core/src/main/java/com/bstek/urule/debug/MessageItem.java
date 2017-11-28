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
package com.bstek.urule.debug;

/**
 * @author Jacky.gao
 * @since 2017年11月27日
 */
public class MessageItem {
	private String msg;
	private MsgType type;
	public MessageItem(String msg,MsgType type) {
		this.msg=msg;			
		this.type=type;
	}
	public String toHtml(){
		String color="#000";
		switch(type){
		case Condition:
			color="#6495ED";
			break;
		case ConsoleOutput:
			color="#000";
			break;
		case ExecuteBeanMethod:
			color="#8A2BE2";
			break;
		case ExecuteFunction:
			color="#008B8B";
			break;
		case RuleFlow:
			color="#9932CC";
			break;
		case VarAssign:
			color="#FF7F50";
			break;
		case ScoreCard:
			color="#40E0D0";
			break;
		case RuleMatch:
			color="#666600";
			break;
		}
		return "<div style=\"color:"+color+";margin:2px\">"+msg+"</div>";
	}
	public String getMsg() {
		return msg;
	}
	public MsgType getType() {
		return type;
	}
}
