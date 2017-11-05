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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.bstek.urule.RuleException;
import com.bstek.urule.Utils;
import com.bstek.urule.model.library.action.annotation.ActionBean;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;

/**
 * @author Jacky.gao
 * @since 2016年6月21日
 */
@ActionBean(name="List集合")
public class ListAction {
	@ActionMethod(name="求List大小")
	@ActionMethodParameter(names={"集合对象"})
	public int size(List<Object> list){
		return list.size();
	}
	
	@ActionMethod(name="求List中所有的数字最大值")
	@ActionMethodParameter(names={"包含所有数字的集合对象"})
	public Number max(List<Object> list){
		double max=Double.MIN_VALUE;
		for(Object obj:list){
			BigDecimal v=Utils.toBigDecimal(obj);
			max=Math.max(max, v.doubleValue());
		}
		return max;
	}
	
	@ActionMethod(name="求List中所有的数字最小值")
	@ActionMethodParameter(names={"包含所有数字的集合对象"})
	public Number min(List<Object> list){
		if(list.size()==0){
			throw new RuleException("Number list can not be null when compute min value from list.");
		}
		double min=Double.MAX_VALUE;
		for(Object obj:list){
			BigDecimal v=Utils.toBigDecimal(obj);
			min=Math.min(min, v.doubleValue());
		}
		return min;
	}
	
	@ActionMethod(name="向List中添加对象")
	@ActionMethodParameter(names={"集合对象","要添加的对象"})
	public void add(List<Object> list,Object obj){
		list.add(obj);
	}
	
	@ActionMethod(name="集合排序")
	@ActionMethodParameter(names={"集合对象","属性名","排序方式"})
	public List<Object> sort(List<Object> list,final String propertyName,String type){
		final boolean asc=asc(type);
		Collections.sort(list, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				int result=0;
				String[] props=propertyName.split(",");
				for(String prop:props){
					result = objectCompare(prop, asc, o1, o2);
					if(result!=0){
						break;
					}
				}
				return result;
			}

			private int objectCompare(final String propertyName,final boolean asc, Object o1, Object o2) {
				Object v1=Utils.getObjectProperty(o1, propertyName);
				Object v2=Utils.getObjectProperty(o2, propertyName);
				if(v1==null){
					if(asc){
						return 0;
					}else{
						return 1;
					}
				}
				if(v2==null){
					if(asc){
						return 1;						
					}else{
						return 0;						
					}
				}
				if(v1 instanceof String){
					if(asc){
						return ((String) v1).compareTo(v2.toString());						
					}else{
						return ((String) v2).compareTo(v1.toString());						
					}
				}
				if(v1 instanceof Date){
					if(asc){
						return ((Date)v1).compareTo((Date)v2);						
					}else{
						return ((Date)v2).compareTo((Date)v1);						
					}
				}
				if(v1 instanceof Number){
					BigDecimal b1=Utils.toBigDecimal(v1);
					BigDecimal b2=Utils.toBigDecimal(v2);
					if(asc){
						return b1.compareTo(b2);
					}else{
						return b2.compareTo(b1);
					}
				}
				return 0;
			}
		});
		return list;
	}
	
	private boolean asc(String type){
		if(type==null){
			return true;
		}
		if(type.equals("1") || type.equals("true") || type.equals("正序")){
			return true;
		}
		return false;
	}
	
	@ActionMethod(name="抽取集合属性")
	@ActionMethodParameter(names={"集合对象","属性名"})
	public List<Object> retrive(List<Object> list,String propertyName){
		List<Object> result=new ArrayList<Object>();
		if(list==null){
			return result;
		}
		for(Object obj:list){
			Object value=Utils.getObjectProperty(obj, propertyName);
			result.add(value);
		}
		return result;
	}
	
	@ActionMethod(name="从List中删除对象")
	@ActionMethodParameter(names={"集合对象","要删除的对象"})
	public void remove(List<Object> list,Object obj){
		list.remove(obj);
	}
	@ActionMethod(name="指定对象是否存在")
	@ActionMethodParameter(names={"集合对象","要判断的对象"})
	public boolean contains(List<Object> list,Object obj){
		return list.contains(obj);
	}
	@ActionMethod(name="List是否为空")
	@ActionMethodParameter(names={"集合对象"})
	public boolean isEmpty(List<Object> list){
		return list.isEmpty();
	}
}
