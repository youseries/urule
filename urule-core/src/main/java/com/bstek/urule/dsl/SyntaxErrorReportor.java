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
