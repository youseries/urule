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

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;

/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public class InAssertor implements Assertor {
	public boolean eval(Object left, Object right,Datatype datatype) {
		if(left==null || right==null){
			return false;
		}
		if(right instanceof Collection){
			Collection<?> coll=(Collection<?>)right;
			if(left instanceof Collection){
				Collection<?> leftColl=(Collection<?>)left;
				boolean match=false;
				for(Object leftObj:leftColl){
					for(Object obj:coll){
						if(obj.toString().equals(leftObj.toString())){
							match=true;
							break;
						}else{
							match=false;							
						}
					}
				}
				return match;
			}else{
				String[] leftStrings=left.toString().split(",");
				boolean match=false;
				for(String str:leftStrings){
					for(Object obj:coll){
						if(obj.toString().equals(str)){
							match=true;
							break;
						}else{
							match=false;							
						}
					}					
				}
				return match;
			}
		}else if(right instanceof String){
			String str=(String)right;
			String[] array=str.split(",");
			if(left instanceof Collection){
				Collection<?> leftColl=(Collection<?>)left;
				boolean match=false;
				for(Object leftObj:leftColl){
					for(String rightStr:array){
						if(leftObj.toString().equals(rightStr)){
							match=true;
							break;
						}else{
							match=false;							
						}
					}
				}
				return match;
			}else{
				String[] leftStrings=left.toString().split(",");
				boolean match=false;
				for(String leftStr:leftStrings){
					for(String righStr:array){
						if(righStr.equals(leftStr)){
							match=true;
							break;
						}else{
							match=false;							
						}
					}				
				}
				return match;
			}
		}
		return false;
	}
	
	public boolean support(Op op) {
		return op.equals(Op.In);
	}
}
