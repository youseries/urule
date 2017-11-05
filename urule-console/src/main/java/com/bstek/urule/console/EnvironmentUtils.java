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
package com.bstek.urule.console;

import java.util.Collection;

import org.springframework.context.ApplicationContext;

import com.bstek.urule.Utils;
import com.bstek.urule.console.servlet.RequestContext;


/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public class EnvironmentUtils{
	private static EnvironmentProvider environmentProvider;
	public static User getLoginUser(RequestContext context){
		if(environmentProvider==null){
			initEnvironmentProvider();
		}
		return environmentProvider.getLoginUser(context);
	}
	public static void initEnvironmentProvider(){
		ApplicationContext context=Utils.getApplicationContext();
		Collection<EnvironmentProvider> providers=context.getBeansOfType(EnvironmentProvider.class).values();
		if(providers.size()==0){
			environmentProvider=new DefaultEnvironmentProvider();
		}else{
			environmentProvider = providers.iterator().next();			
		}
	}
	public static EnvironmentProvider getEnvironmentProvider(){
		if(environmentProvider==null){
			initEnvironmentProvider();
		}
		return environmentProvider;
	}
}
