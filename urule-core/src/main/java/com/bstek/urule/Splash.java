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
		sb.append(" licensed under GNU GENERAL PUBLIC LICENSE Version 3,       .");
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
