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
package com.bstek.urule.runtime.builtinaction;

import com.bstek.urule.model.library.action.annotation.ActionBean;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;

/**
 * @author Jacky.gao
 * @since 2015年11月27日
 */
@ActionBean(name="字符串")
public class StringAction {
	@ActionMethod(name="去空格")
	@ActionMethodParameter(names={"目标字符串"})
	public String trim(String str){
		if(str==null){
			return str;
		}
		return str.trim();
	}
	
	@ActionMethod(name="指定起始的字符串截取")
	@ActionMethodParameter(names={"目标字符串","开始位置","结束位置"})
	public String substring(String str,int start,int end){
		return str.substring(start, end);
	}
	
	@ActionMethod(name="指定开始的字符串截取")
	@ActionMethodParameter(names={"目标字符串","开始位置"})
	public String substringForStart(String str,int start){
		return str.substring(start);
	}
	@ActionMethod(name="指定结束的字符串截取")
	@ActionMethodParameter(names={"目标字符串","结束位置"})
	public String substringForEnd(String str,int end){
		return str.substring(0,end);
	}
	
	@ActionMethod(name="转小写")
	@ActionMethodParameter(names={"目标字符串"})
	public String toLowerCase(String str){
		return str.toLowerCase();
	}
	
	@ActionMethod(name="转大写")
	@ActionMethodParameter(names={"目标字符串"})
	public String toUpperCase(String str){
		return str.toUpperCase();
	}
	
	@ActionMethod(name="获取长度")
	@ActionMethodParameter(names={"目标字符串"})
	public int length(String str){
		return str.length();
	}
	
	@ActionMethod(name="获取字符")
	@ActionMethodParameter(names={"目标字符串","位置"})
	public char charAt(String str,int index){
		return str.charAt(index);
	}
	
	@ActionMethod(name="字符首次出现位置")
	@ActionMethodParameter(names={"目标字符串","要查找的字符串"})
	public int indexOf(String str,String targetStr){
		return str.indexOf(targetStr);
	}
	
	@ActionMethod(name="字符最后出现位置")
	@ActionMethodParameter(names={"目标字符串","要查找的字符串"})
	public int lastIndexOf(String str,String targetStr){
		return str.lastIndexOf(targetStr);
	}
	
	@ActionMethod(name="替换字符串")
	@ActionMethodParameter(names={"目标字符串","原字符串","新字符串"})
	public String replace(String str,String oldStr,String newStr){
		return str.replace(oldStr, newStr);
	}
}
