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
package com.bstek.urule.console.servlet.console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jacky.gao
 * @since 2017年11月28日
 */
public class DebugMessageHolder {
	private int num=0;
	private Map<String,DebugMessage> messageMap=new HashMap<String,DebugMessage>();
	public String getDebugMessage(String key){
		DebugMessage msg=messageMap.get(key);
		String info=null;
		if(msg==null){
			info="<h2 style='color:red'>Key为["+key+"]的调试信息已失效，不能查看，当前key值为:"+this.num+"。<br>调试消息有效期为3分钟，请在有效期内查看!</h2>";
		}else{
			info=msg.getMessage();
		}
		clean();
		return info;
	}
	public void putDebugMessage(String key,String msg){
		clean();
		DebugMessage m=new DebugMessage(msg);
		messageMap.put(key, m);
	}
	private void clean(){
		long current=System.currentTimeMillis();
		List<String> list=new ArrayList<String>();
		for(String key:messageMap.keySet()){
			DebugMessage msg=messageMap.get(key);
			long dif=current-msg.getTimestamp();
			if(dif>180000){
				list.add(key);
			}
		}
		for(String key:list){
			messageMap.remove(key);
		}
	}
	public synchronized String generateKey(){
		num++;
		return String.valueOf(num);
	}
}
