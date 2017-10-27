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
package com.bstek.urule.model.function;
/**
 * @author Jacky.gao
 * @since 2015年7月26日
 */
public class Argument {
	/**
	 * 函数第一个参数提示名称
	 */
	private String name;
	/**
	 * 当前函数是否需要第二个参数，也就是说是否需要选择属性名
	 */
	private boolean needProperty;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isNeedProperty() {
		return needProperty;
	}
	public void setNeedProperty(boolean needProperty) {
		this.needProperty = needProperty;
	}
}
