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
