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
package com.bstek.urule.dsl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2015年2月27日
 */
public class SyntaxErrorReportor {
	private List<String> errorList=new ArrayList<String>();
	public void addError(int line,int charPositionInLine,Object offendingSymbol,String msg){
		errorList.add(line+"行,"+charPositionInLine+"列，"+offendingSymbol+"字符处，存在语法错误:"+msg);
	}
	public String getSyntaxErrorMessage(){
		StringBuffer sb=new StringBuffer();
		for(String msg:errorList){
			sb.append(msg);
			sb.append("\n");
		}
		return sb.toString();
	}
}
