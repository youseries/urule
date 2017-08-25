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
