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

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import com.bstek.urule.Utils;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;

/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public class EqualsAssertor implements Assertor {
	@SuppressWarnings("unchecked")
	public boolean eval(Object left, Object right,Datatype datatype) {
		if(left==null && right==null){
			return true;
		}else if(left==null || right==null){
			return false;
		}
		BigDecimal b1=null;
		BigDecimal b2=null;
		switch(datatype){
		case String:
			return left.toString().equals(right.toString());
		case Boolean:
			return left.toString().equals(right.toString());
		case Date:
			Date ld=(Date)datatype.convert(left);
			Date rd=(Date)datatype.convert(right);
			Calendar leftCalendar=Calendar.getInstance();
			leftCalendar.setTime(ld);
			Calendar rightCalendar=Calendar.getInstance();
			rightCalendar.setTime(rd);
			return leftCalendar.compareTo(rightCalendar)==0;
		case Double:
			b1=Utils.toBigDecimal(left);
			b2=Utils.toBigDecimal(right);
			return b1.compareTo(b2)==0;
		case Float:
			b1=Utils.toBigDecimal(left);
			b2=Utils.toBigDecimal(right);
			return b1.compareTo(b2)==0;
		case Integer:
			b1=Utils.toBigDecimal(left);
			b2=Utils.toBigDecimal(right);
			return b1.compareTo(b2)==0;
		case Long:
			b1=Utils.toBigDecimal(left);
			b2=Utils.toBigDecimal(right);
			return b1.compareTo(b2)==0;
		case BigDecimal:
			b1=Utils.toBigDecimal(left);
			b2=Utils.toBigDecimal(right);
			return b1.compareTo(b2)==0;
		case Enum:
			Enum<?> e1=(Enum<?>)left;
			if(right instanceof Enum){
				Enum<?> e2=(Enum<?>)right;
				return e1.equals(e2);
			}else{
				Enum<?> e2=Enum.valueOf(e1.getClass(), right.toString());
				return e1.equals(e2);
			}
		default :
			return right.toString().equals(left.toString());
		}
	}
	public boolean support(Op op) {
		return op.equals(Op.Equals);
	}
}
