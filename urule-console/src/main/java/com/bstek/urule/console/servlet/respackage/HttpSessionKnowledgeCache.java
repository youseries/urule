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
