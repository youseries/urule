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
package com.bstek.urule.runtime.builtinaction;

import java.math.BigDecimal;

import com.bstek.urule.Utils;
import com.bstek.urule.model.library.action.annotation.ActionBean;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;
/**
 * @author Jacky.gao
 * @since 2015年11月27日
 */
@ActionBean(name="数学函数")
public class MathAction {
	@ActionMethod(name="求绝对值")
	@ActionMethodParameter(names={"数字"})
	public Number abs(Object obj){
		BigDecimal v1=Utils.toBigDecimal(obj);
		return Math.abs(v1.doubleValue());
	}
	@ActionMethod(name="求最大值")
	@ActionMethodParameter(names={"数字1","数字2"})
	public Number max(Object obj,Object obj1){
		BigDecimal v1=Utils.toBigDecimal(obj);
		BigDecimal v2=Utils.toBigDecimal(obj1);
		return Math.max(v1.doubleValue(), v2.doubleValue());
	}
	@ActionMethod(name="求最小值")
	@ActionMethodParameter(names={"数字1","数字2"})
	public Number min(Object obj,Object obj1){
		BigDecimal v1=Utils.toBigDecimal(obj);
		BigDecimal v2=Utils.toBigDecimal(obj1);
		return Math.min(v1.doubleValue(), v2.doubleValue());
	}
	
	@ActionMethod(name="求正弦")
	@ActionMethodParameter(names={"数字"})
	public Number in(Object obj){
		BigDecimal v1=Utils.toBigDecimal(obj);
		return Math.sin(v1.doubleValue());
	}
	@ActionMethod(name="求余弦")
	@ActionMethodParameter(names={"数字"})
	public Number cos(Object obj){
		BigDecimal v1=Utils.toBigDecimal(obj);
		return Math.cos(v1.doubleValue());
	}
	@ActionMethod(name="求正切")
	@ActionMethodParameter(names={"数字"})
	public Number tan(Object obj){
		BigDecimal v1=Utils.toBigDecimal(obj);
		return Math.tan(v1.doubleValue());
	}
	@ActionMethod(name="求余切")
	@ActionMethodParameter(names={"数字"})
	public Number cot(Object obj){
		BigDecimal v1=Utils.toBigDecimal(obj);
		return 1/Math.tan(v1.doubleValue());
	}
	@ActionMethod(name="求e为底的对数")
	@ActionMethodParameter(names={"数字"})
	public Number log(Object obj){
		BigDecimal v1=Utils.toBigDecimal(obj);
		return Math.log(v1.doubleValue());
	}
	@ActionMethod(name="求10为底的对数")
	@ActionMethodParameter(names={"数字"})
	public Number log10(Object obj){
		BigDecimal v1=Utils.toBigDecimal(obj);
		return Math.log10(v1.doubleValue());
	}
	
	@ActionMethod(name="四舍五入")
	@ActionMethodParameter(names={"数字"})
	public Number round(Object obj){
		BigDecimal v1=Utils.toBigDecimal(obj);
		return Math.round(v1.doubleValue());
	}
}
