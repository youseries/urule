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
package com.bstek.urule.model.rule;
/**
 * @author Jacky.gao
 * @since 2014年12月30日
 */
public class Library {
	private String path;
	private String version;
	private LibraryType type;
	public Library() {
	}
	public Library(String path,String version,LibraryType type) {
		this.path = path;
		this.version=version;
		this.type = type;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPath() {
		return path;
	}
	public LibraryType getType() {
		return type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setType(LibraryType type) {
		this.type = type;
	}
	
}
