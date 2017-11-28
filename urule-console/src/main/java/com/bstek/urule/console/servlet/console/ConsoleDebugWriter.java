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

import java.io.IOException;
import java.util.List;

import com.bstek.urule.debug.DebugWriter;
import com.bstek.urule.debug.MessageItem;

/**
 * @author Jacky.gao
 * @since 2017年11月28日
 */
public class ConsoleDebugWriter implements DebugWriter {
	private DebugMessageHolder debugMessageHolder;
	@Override
	public void write(List<MessageItem> items) throws IOException {
		StringBuilder sb=new StringBuilder();
		for(MessageItem item:items){
			sb.append(item.toHtml());
		}
		String key=debugMessageHolder.generateKey();
		System.out.println("Console key : "+key);
		debugMessageHolder.putDebugMessage(key, sb.toString());
	}
	public void setDebugMessageHolder(DebugMessageHolder debugMessageHolder) {
		this.debugMessageHolder = debugMessageHolder;
	}
}
