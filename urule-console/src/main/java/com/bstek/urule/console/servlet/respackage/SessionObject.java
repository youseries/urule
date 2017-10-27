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
package com.bstek.urule.console.servlet.respackage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jacky.gao
 * @since 2017年8月20日
 */
public class SessionObject {
	private long start;
	private static final long MILLISECOND=1200000;//default expired time is 20 minutes.
	private Map<String,Object> map=new HashMap<String,Object>();
	public SessionObject() {
		this.start=System.currentTimeMillis();
	}
	public void put(String key,Object obj){
		this.start=System.currentTimeMillis();
		if(map.containsKey(key)){
			map.remove(key);
		}
		map.put(key, obj);
	}
	
	public Object get(String key){
		this.start=System.currentTimeMillis();
		return map.get(key);
	}
	
	public void remove(String key){
		this.start=System.currentTimeMillis();
		map.remove(key);
	}
	
	public boolean isExpired(){
		long end=System.currentTimeMillis();
		long value=end-start;
		if(value>=MILLISECOND){
			return true;
		}
		return false;
	}
}
