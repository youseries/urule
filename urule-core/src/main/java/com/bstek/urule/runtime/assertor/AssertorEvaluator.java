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
package com.bstek.urule.runtime.assertor;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.urule.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;

/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public class AssertorEvaluator implements ApplicationContextAware{
	public static final String BEAN_ID="urule.assertorEvaluator";
	private Collection<Assertor> assertors;
	public boolean evaluate(Object left,Object right,Datatype datatype,Op op){
		Assertor targetAssertor=null;
		for(Assertor assertor:assertors){
			if(assertor.support(op)){
				targetAssertor=assertor;
				break;
			}
		}
		if(targetAssertor==null){
			throw new RuleException("Unsupport op:"+op);
		}
		return targetAssertor.eval(left, right,datatype);
	}
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		assertors=applicationContext.getBeansOfType(Assertor.class).values();
	}
}
