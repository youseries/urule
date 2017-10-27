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
