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
package com.bstek.urule.runtime.builtinaction;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

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
		if(str==null){
			return null;
		}
		return str.substring(start, end);
	}
	
	@ActionMethod(name="指定开始的字符串截取")
	@ActionMethodParameter(names={"目标字符串","开始位置"})
	public String substringForStart(String str,int start){
		if(str==null){
			return null;
		}
		return str.substring(start);
	}
	@ActionMethod(name="指定结束的字符串截取")
	@ActionMethodParameter(names={"目标字符串","结束位置"})
	public String substringForEnd(String str,int end){
		if(str==null){
			return null;
		}
		return str.substring(0,end);
	}
	
	@ActionMethod(name="转小写")
	@ActionMethodParameter(names={"目标字符串"})
	public String toLowerCase(String str){
		if(str==null){
			return null;
		}
		return str.toLowerCase();
	}
	
	@ActionMethod(name="转大写")
	@ActionMethodParameter(names={"目标字符串"})
	public String toUpperCase(String str){
		if(str==null){
			return null;
		}
		return str.toUpperCase();
	}
	
	@ActionMethod(name="获取长度")
	@ActionMethodParameter(names={"目标字符串"})
	public Object length(String str){
		if(str==null){
			return null;
		}
		return str.length();
	}
	
	@ActionMethod(name="获取字符")
	@ActionMethodParameter(names={"目标字符串","位置"})
	public Object charAt(String str,int index){
		if(str==null){
			return null;
		}
		return str.charAt(index);
	}
	
	@ActionMethod(name="字符首次出现位置")
	@ActionMethodParameter(names={"目标字符串","要查找的字符串"})
	public Object indexOf(String str,String targetStr){
		if(str==null){
			return null;
		}
		return str.indexOf(targetStr);
	}
	
	@ActionMethod(name="字符最后出现位置")
	@ActionMethodParameter(names={"目标字符串","要查找的字符串"})
	public Object lastIndexOf(String str,String targetStr){
		if(str==null){
			return null;
		}
		return str.lastIndexOf(targetStr);
	}
	
	@ActionMethod(name="替换字符串")
	@ActionMethodParameter(names={"目标字符串","原字符串","新字符串"})
	public String replace(String str,String oldStr,String newStr){
		if(str==null){
			return null;
		}
		return str.replace(oldStr, newStr);
	}
	@ActionMethod(name="拆分字符串为集合")
	@ActionMethodParameter(names={"目标字符串","原字符串","新字符串"})
	public List<String> split(String str,String regex){
		if(StringUtils.isBlank(str)){
			return new ArrayList<String>();
		}
		String[] arr=str.split(regex);
		List<String> list=new ArrayList<String>();
		for(String item:arr){
			list.add(item);
		}
		return list;
	}
}
