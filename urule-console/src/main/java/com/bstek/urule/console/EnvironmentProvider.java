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
