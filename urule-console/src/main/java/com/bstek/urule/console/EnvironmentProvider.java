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

import java.util.List;

import com.bstek.urule.console.servlet.RequestContext;

/**
 * @author Jacky.gao
 * @since 2015年3月27日
 */
public interface EnvironmentProvider {
	/**
	 * @param context 请求上下文对象
	 * @return 返回当前登录用户
	 */
	User getLoginUser(RequestContext context);
	
	/**
	 * @return 返回当前系统当中用户集合 ，供配置资源库权限使用
	 */
	List<User> getUsers();
}
