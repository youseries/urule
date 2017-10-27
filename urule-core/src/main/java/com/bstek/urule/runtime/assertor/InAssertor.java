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
