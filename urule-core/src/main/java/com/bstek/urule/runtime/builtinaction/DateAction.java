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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.bstek.urule.RuleException;
import com.bstek.urule.model.library.action.annotation.ActionBean;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;

/**
 * @author Jacky.gao
 * @since 2015年11月27日
 */
@ActionBean(name="日期")
public class DateAction {
	
	@ActionMethod(name="解析字符串为日期")
	@ActionMethodParameter(names={"日期字符串","格式"})
	public Date formatString(String dateStr,String pattern){
		if(StringUtils.isBlank(dateStr)){
			return null;
		}
		SimpleDateFormat sd=new SimpleDateFormat(pattern);
		try {
			return sd.parse(dateStr);
		} catch (ParseException e) {
			throw new RuleException(e);
		}
	}
	
	@ActionMethod(name="当前日期")
	@ActionMethodParameter(names={})
	public Date getDate(){
		return new Date();
	}
	
	@ActionMethod(name="格式化日期")
	@ActionMethodParameter(names={"目标日期","格式"})
	public String format(Date date,String pattern){
		if(date==null){
			return null;
		}
		SimpleDateFormat sd=new SimpleDateFormat(pattern);
		return sd.format(date);
	}
	@ActionMethod(name="加日期")
	@ActionMethodParameter(names={"目标日期","年数","月数","天数","小时","分钟","秒数"})
	public Date addDate(Date date,int years,int months,int days,int hours,int minutes,int seconds){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, years);
		c.add(Calendar.MONTH, months);
		c.add(Calendar.DAY_OF_MONTH, days);
		c.add(Calendar.HOUR_OF_DAY, hours);
		c.add(Calendar.MINUTE, minutes);
		c.add(Calendar.SECOND, seconds);
		return c.getTime();
	}
	@ActionMethod(name="日期加年")
	@ActionMethodParameter(names={"目标日期","年数"})
	public Date addDateForYear(Date date,int years){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, years);
		return c.getTime();
	}
	
	@ActionMethod(name="日期加月")
	@ActionMethodParameter(names={"目标日期","月数"})
	public Date addDateForMonth(Date date,int months){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, months);
		return c.getTime();
	}
	@ActionMethod(name="日期加天")
	@ActionMethodParameter(names={"目标日期","天数"})
	public Date addDateForDay(Date date,int days){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTime();
	}
	
	@ActionMethod(name="日期加小时")
	@ActionMethodParameter(names={"目标日期","小时数"})
	public Date addDateForHour(Date date,int hours){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, hours);
		return c.getTime();
	}
	
	@ActionMethod(name="日期加分钟")
	@ActionMethodParameter(names={"目标日期","分钟数"})
	public Date addDateForMinute(Date date,int minutes){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, minutes);
		return c.getTime();
	}
	@ActionMethod(name="日期加秒")
	@ActionMethodParameter(names={"目标日期","秒数"})
	public Date addDateForSecond(Date date,int seconds){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.SECOND, seconds);
		return c.getTime();
	}
	@ActionMethod(name="减日期")
	@ActionMethodParameter(names={"目标日期","年数","月数","天数","小时","分钟","秒数"})
	public Date subDate(Date date,int years,int months,int days,int hours,int minutes,int seconds){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, -years);
		c.add(Calendar.MONTH, -months);
		c.add(Calendar.DAY_OF_MONTH, -days);
		c.add(Calendar.HOUR_OF_DAY, -hours);
		c.add(Calendar.MINUTE, -minutes);
		c.add(Calendar.SECOND, -seconds);
		return c.getTime();
	}
	@ActionMethod(name="减日期减年")
	@ActionMethodParameter(names={"目标日期","年数"})
	public Date subDateForYear(Date date,int years){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, -years);
		return c.getTime();
	}
	@ActionMethod(name="减日期减月")
	@ActionMethodParameter(names={"目标日期","月数"})
	public Date subDateForMonth(Date date,int months){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, -months);
		return c.getTime();
	}
	@ActionMethod(name="减日期减天")
	@ActionMethodParameter(names={"目标日期","天数"})
	public Date subDateForDay(Date date,int days){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, -days);
		return c.getTime();
	}
	@ActionMethod(name="减日期减小时")
	@ActionMethodParameter(names={"目标日期","小时"})
	public Date subDateForHour(Date date,int hours){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, -hours);
		return c.getTime();
	}
	@ActionMethod(name="减日期减分钟")
	@ActionMethodParameter(names={"目标日期","分钟"})
	public Date subDateForMinute(Date date,int minutes){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, -minutes);
		return c.getTime();
	}
	@ActionMethod(name="减日期减秒")
	@ActionMethodParameter(names={"目标日期","秒数"})
	public Date subDateForSecond(Date date,int seconds){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.SECOND, -seconds);
		return c.getTime();
	}
	
	@ActionMethod(name="取年份")
	@ActionMethodParameter(names={"目标日期"})
	public Object getYear(Date date){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}
	
	@ActionMethod(name="取月份")
	@ActionMethodParameter(names={"目标日期"})
	public Object getMonth(Date date){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH);
	}
	
	@ActionMethod(name="取星期")
	@ActionMethodParameter(names={"目标日期"})
	public Object getWeek(Date date){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_WEEK);
	}
	@ActionMethod(name="取天")
	@ActionMethodParameter(names={"目标日期"})
	public Object getay(Date date){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}
	@ActionMethod(name="取小时")
	@ActionMethodParameter(names={"目标日期"})
	public Object getHour(Date date){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}
	@ActionMethod(name="取分钟")
	@ActionMethodParameter(names={"目标日期"})
	public Object getMinute(Date date){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MINUTE);
	}
	@ActionMethod(name="取秒")
	@ActionMethodParameter(names={"目标日期"})
	public Object getSecond(Date date){
		if(date==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.SECOND);
	}
	@ActionMethod(name="日期相减返回毫秒")
	@ActionMethodParameter(names={"日期","减去的日期"})
	public Object dateDifMillSecond(Date d1,Date d2){
		if(d1==null || d2==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(d1);
		Calendar c1=Calendar.getInstance();
		c1.setTime(d2);
		long a=c.getTimeInMillis();
		long b=c1.getTimeInMillis();
		return a-b;
	}
	@ActionMethod(name="日期相减返回秒")
	@ActionMethodParameter(names={"日期","减去的日期"})
	public Object dateDifSecond(Date d1,Date d2){
		if(d1==null || d2==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(d1);
		Calendar c1=Calendar.getInstance();
		c1.setTime(d2);
		long a=c.getTimeInMillis();
		long b=c1.getTimeInMillis();
		return (a-b)/1000;
	}
	@ActionMethod(name="日期相减返回分钟")
	@ActionMethodParameter(names={"日期","减去的日期"})
	public Object dateDifMinute(Date d1,Date d2){
		if(d1==null || d2==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(d1);
		Calendar c1=Calendar.getInstance();
		c1.setTime(d2);
		long a=c.getTimeInMillis();
		long b=c1.getTimeInMillis();
		return (a-b)/(1000*60);
	}
	@ActionMethod(name="日期相减返回小时")
	@ActionMethodParameter(names={"日期","减去的日期"})
	public Object dateDifHour(Date d1,Date d2){
		if(d1==null || d2==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(d1);
		Calendar c1=Calendar.getInstance();
		c1.setTime(d2);
		long a=c.getTimeInMillis();
		long b=c1.getTimeInMillis();
		return (a-b)/(1000*60*60);
	}
	@ActionMethod(name="日期相减返回天")
	@ActionMethodParameter(names={"日期","减去的日期"})
	public Object dateDifDay(Date d1,Date d2){
		if(d1==null || d2==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(d1);
		Calendar c1=Calendar.getInstance();
		c1.setTime(d2);
		long a=c.getTimeInMillis();
		long b=c1.getTimeInMillis();
		return (a-b)/(1000*60*60*24);
	}
	@ActionMethod(name="日期相减返回星期")
	@ActionMethodParameter(names={"日期","减去的日期"})
	public Object dateDifWeek(Date d1,Date d2){
		if(d1==null || d2==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(d1);
		Calendar c1=Calendar.getInstance();
		c1.setTime(d2);
		long a=c.getTimeInMillis();
		long b=c1.getTimeInMillis();
		return (a-b)/(1000*60*60*24*7);
	}
	@ActionMethod(name="日期相减返回月")
	@ActionMethodParameter(names={"日期","减去的日期"})
	public Object dateDifMonth(Date d1,Date d2){
		if(d1==null || d2==null){
			return null;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(d1);
		Calendar c1=Calendar.getInstance();
		c1.setTime(d2);
		
		int year1=c.get(Calendar.YEAR);
		int year2=c1.get(Calendar.YEAR);
		
		int month1=c.get(Calendar.MONTH);
		int month2=c1.get(Calendar.MONTH);
		int result=12*(year1-year2)+(month1-month2);
		return result;
	}
}
