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
