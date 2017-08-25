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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Jacky.gao
 * @since 2017年8月20日
 */
public class HttpSessionKnowledgeCache {
	private Map<String,SessionObject> sessionMap=new HashMap<String,SessionObject>();
	public Object get(HttpServletRequest req,String key){
		SessionObject sessionObject = getSessionObject(req);
		return sessionObject.get(key);
	}
	public void put(HttpServletRequest req,String key,Object obj){
		SessionObject sessionObject = getSessionObject(req);
		sessionObject.put(key, obj);
	}
	public void remove(HttpServletRequest req,String key){
		SessionObject sessionObject = getSessionObject(req);
		sessionObject.remove(key);
	}
	private SessionObject getSessionObject(HttpServletRequest req) {
		clean();
		String sessionId=req.getSession().getId();
		SessionObject sessionObject=null;
		if(sessionMap.containsKey(sessionId)){
			sessionObject=sessionMap.get(sessionId);
		}else{
			sessionObject=new SessionObject();
			sessionMap.put(sessionId, sessionObject);
		}
		return sessionObject;
	}
	
	private void clean(){
		List<String> list=new ArrayList<String>();
		for(String key:sessionMap.keySet()){
			SessionObject obj=sessionMap.get(key);
			if(obj.isExpired()){
				list.add(key);
			}
		}
		for(String key:list){
			sessionMap.remove(key);
		}
	}
}
