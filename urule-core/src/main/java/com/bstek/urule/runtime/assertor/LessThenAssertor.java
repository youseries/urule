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
public class LessThenAssertor implements Assertor {
	public boolean eval(Object left, Object right,Datatype datatype) {
		if(left==null || right==null){
			return false;
		}
		if(datatype.equals(Datatype.Date)){
			Date leftDate=(Date)datatype.convert(left);
			Date rightDate=(Date)datatype.convert(right);
			Calendar leftCalendar=Calendar.getInstance();
			leftCalendar.setTime(leftDate);
			Calendar rightCalendar=Calendar.getInstance();
			rightCalendar.setTime(rightDate);
			int result=leftCalendar.compareTo(rightCalendar);
			if(result==-1){
				return true;
			}
		}else{
			BigDecimal leftNumber=Utils.toBigDecimal(left);
			BigDecimal rightNumber=Utils.toBigDecimal(right);
			int result=leftNumber.compareTo(rightNumber);
			if(result==-1){
				return true;
			}
		}
		return false;
	}

	public boolean support(Op op) {
		return op.equals(Op.LessThen);
	}
}
