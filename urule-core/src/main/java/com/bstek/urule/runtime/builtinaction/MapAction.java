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
