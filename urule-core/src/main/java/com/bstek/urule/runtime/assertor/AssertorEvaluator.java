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
