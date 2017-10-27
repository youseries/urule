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

import java.util.Map;

import com.bstek.urule.model.library.action.annotation.ActionBean;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;

/**
 * @author Jacky.gao
 * @since 2016年6月21日
 */
@ActionBean(name="Map集合")
public class MapAction {
	
	@ActionMethod(name="添加到Map")
	@ActionMethodParameter(names={"Map对象","key","value"})
	public void put(Map<String,Object> map,String key,Object value){
		map.put(key, value);
	}
	@ActionMethod(name="从Map中删除")
	@ActionMethodParameter(names={"Map对象","key"})
	public void remove(Map<String,Object> map,String key){
		map.remove(key);
	}
	@ActionMethod(name="指定Key是否存在")
	@ActionMethodParameter(names={"Map对象","key"})
	public boolean containsKey(Map<String,Object> map,String key){
		return map.containsKey(key);
	}
	@ActionMethod(name="从Map中取值")
	@ActionMethodParameter(names={"Map对象","key"})
	public Object get(Map<String,Object> map,String key){
		return map.get(key);
	}
	@ActionMethod(name="返回Map大小")
	@ActionMethodParameter(names={"Map对象"})
	public int size(Map<String,Object> map){
		return map.size();
	}
}
