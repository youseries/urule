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
public class EqualsIgnoreCaseAssertor implements Assertor {
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
			return left.toString().equalsIgnoreCase(right.toString());
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
			return right.toString().equalsIgnoreCase(left.toString());
		}
	}
	public boolean support(Op op) {
		return op.equals(Op.EqualsIgnoreCase);
	}
}
