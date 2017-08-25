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
