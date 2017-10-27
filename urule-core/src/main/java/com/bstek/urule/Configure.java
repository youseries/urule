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
