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
package com.bstek.urule.model.scorecard.runtime;

import java.util.List;

/**
 * @author Jacky.gao
 * @since 2016年9月27日
 */
public interface Scorecard {
	/**
	 * @return 评分卡名称
	 */
	String getName();
	/**
	 * @return 评分卡表格的所有的行信息
	 */
	List<RowItem> getRowItems();
}
