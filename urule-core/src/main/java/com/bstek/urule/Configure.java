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

import org.apache.commons.lang.StringUtils;

/**
 * @author Jacky.gao
 * @since 2015年2月16日
 */
public class Configure {
	private static String dateFormat;
	private static String tempStorePath;
	public void setDateFormat(String dateFormat) {
		if(StringUtils.isEmpty(dateFormat) || dateFormat.equals("${urule.dateFormat}")){
			Configure.dateFormat = "yyyy-MM-dd HH:mm:ss";
		}else{
			Configure.dateFormat = dateFormat;
		}
	}
	public void setTempStorePath(String tempStorePath) {
		if(!tempStorePath.equals("${urule.tempStorePath}")){
			Configure.tempStorePath = tempStorePath;
		}
	}
	public static String getTempStorePath() {
		return tempStorePath;
	}
	public static String getDateFormat() {
		return dateFormat;
	}
}
