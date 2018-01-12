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
package com.bstek.urule.runtime.rete;

import java.util.Collection;
import java.util.Map;

import com.bstek.urule.model.GeneralEntity;

/**
 * @author Jacky.gao
 * @since 2015年1月9日
 */
public class ObjectTypeActivity extends AbstractActivity {
	private Class<?> typeClass;
	private String clazz;
	public ObjectTypeActivity(String clazz){
		this.clazz=clazz;
	}
	public ObjectTypeActivity(Class<?> typeClass){
		this.typeClass=typeClass;
	}
	public Collection<FactTracker> enter(EvaluationContext context, Object obj,FactTracker tracker,Map<String,Object> variableMap) {
		return visitPahs(context,obj,tracker,variableMap);
	}
	public boolean support(Object object){
		if(typeClass==null && clazz==null){
			return true;
		}
		if(object instanceof GeneralEntity){
			GeneralEntity generalEntity=(GeneralEntity)object;
			String targetClass=generalEntity.getTargetClass();
			if(clazz!=null){
				if(targetClass.equals(clazz)){
					return true;
				}				
			}else{
				if(targetClass.equals(typeClass.getName())){
					return true;
				}
			}
		}else if(typeClass!=null){
			Class<?> c=object.getClass();
			if(typeClass.isAssignableFrom(c) || typeClass.getName().equals(c.getName())){
				return true;				
			}
		}
		return false;
	}
	@Override
	public boolean orNodeIsPassed() {
		return false;
	}
	@Override
	public void reset() {
	}
}
