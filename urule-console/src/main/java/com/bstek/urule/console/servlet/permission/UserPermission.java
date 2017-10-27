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
package com.bstek.urule.console.servlet.permission;

import java.util.List;

/**
 * @author Jacky.gao
 * @since 2016年8月30日
 */
public class UserPermission {
	private String username;
	private List<ProjectConfig> projectConfigs;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<ProjectConfig> getProjectConfigs() {
		return projectConfigs;
	}
	public void setProjectConfigs(List<ProjectConfig> projectConfigs) {
		this.projectConfigs = projectConfigs;
	}
}
