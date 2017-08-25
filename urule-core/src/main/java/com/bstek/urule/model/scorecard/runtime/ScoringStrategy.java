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

import com.bstek.urule.runtime.rete.Context;

/**
 * @author Jacky.gao
 * @since 2016年9月26日
 */
public interface ScoringStrategy {
	/**
	 * 计算得分方法
	 * @param scorecard 当前评分卡对象
	 * @param context 运行时上下文对象
	 * @return 返回最终的得分值
	 */
	Object calculate(Scorecard scorecard,Context context);
}
