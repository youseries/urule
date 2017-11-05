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
package com.bstek.urule;

/**
 * @author Jacky.gao
 * @since 2017年8月28日
 */
public class Splash {
	public void print(){
		StringBuilder sb=new StringBuilder();
		sb.append("\n");
		sb.append("_____  __________ _____  ________ ________________ ");
		sb.append("\n");
		sb.append("__  / / /___  __ \\__  / / /___  / ___  ____/__|__ \\");
		sb.append("\n");
		sb.append("_  / / / __  /_/ /_  / / / __  /  __  __/   ____/ /");
		sb.append("\n");
		sb.append("/ /_/ /  _  _, _/ / /_/ /  _  /____  /___   _  __/ ");
		sb.append("\n");
		sb.append("\\____/   /_/ |_|  \\____/   /_____//_____/   /____/ ");
		sb.append("\n");
		sb.append(".....................................................................................................");
		sb.append("\n");
		sb.append(".  uRule, is a Chinese style rule engine");
		sb.append(" licensed under the Apache License 2.0,                     .");
		sb.append("\n");
		sb.append(".  which is opensource, easy to use,");
		sb.append("high-performance, with browser-based-designer.                  .");
		sb.append("\n");
		sb.append(".....................................................................................................");
		sb.append("\n");
		System.out.println(sb.toString());
	}
	public static void main(String[] args) {
		Splash s=new Splash();
		s.print();
	}
}
