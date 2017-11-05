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

import java.util.ArrayList;
import java.util.List;

import com.bstek.urule.console.servlet.RequestContext;

/**
 * @author Jacky.gao
 * @since 2016年5月25日
 */
public class DefaultEnvironmentProvider implements EnvironmentProvider {

	@Override
	public User getLoginUser(RequestContext context) {
		DefaultUser user=new DefaultUser();
		user.setCompanyId("bstek");
		user.setUsername("admin");
		user.setAdmin(true);
		return user;
	}

	@Override
	public List<User> getUsers() {
		DefaultUser user1=new DefaultUser();
		user1.setCompanyId("bstek");
		user1.setUsername("user1");
		DefaultUser user2=new DefaultUser();
		user2.setCompanyId("bstek");
		user2.setUsername("user2");
		DefaultUser user3=new DefaultUser();
		user3.setCompanyId("bstek");
		user3.setUsername("user3");
		List<User> users=new ArrayList<User>();
		users.add(user1);
		users.add(user2);
		users.add(user3);
		return users;
	}
}
