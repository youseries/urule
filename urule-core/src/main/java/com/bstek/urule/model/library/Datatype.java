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
package com.bstek.urule.model.library;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.bstek.urule.RuleException;
import com.bstek.urule.Utils;

/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public enum Datatype {
	String,Integer,Char,Double,Long,Float,BigDecimal,Boolean,Date,List,Set,Map,Enum,Object;
	public String convertObjectToString(Object value){
		if(value==null){
			return "";
		}
		if(value instanceof String){
			return value.toString();
		}
		switch(this){
		case Object:
			return value.toString();
		case Date:
			SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sd.format((Date)value);
		case List:
			List<?> list=(List<?>)value;
			String a="";
			for(int i=0;i<list.size();i++){
				Object obj=list.get(i);
				if(i>0){
					a+=",";
				}
				a+=obj;
			}
			return a;
		case Set:
			Set<?> set=(Set<?>)value;
			String b="";
			int i=0;
			for(Object obj:set){
				if(i>0){
					b+=",";
				}
				b+=obj;
				i++;
			}
			return b;
		case BigDecimal:
			BigDecimal bb=Utils.toBigDecimal(value);
			return bb.floatValue()+"";
		case Double:
			Double d=Utils.toBigDecimal(value).doubleValue();
			return d.floatValue()+"";
		default:
			return value.toString();
		}
	}
	@SuppressWarnings("rawtypes")
	public Object convert(Object value){
		switch(this){
		case Object:
			return value;
		case String:
			if(value==null)return value;
			return value.toString();
		case Integer:
			if(value==null || value.toString().equals("")){
				value="0";
			}
			return Utils.toBigDecimal(value).intValue();
		case Char:
			if(value==null){
				return '\u0000';
			}
			if(value instanceof Character){
				return (Character)value;
			}
			String str=value.toString();
			if(str.length()==1){
				return str.toCharArray()[0];
			}else{
				int intValue=Utils.toBigDecimal(value).intValue();
				return (char)intValue;
			}
		case Double:
			if(value==null){
				value="0";
			}
			return Utils.toBigDecimal(value).doubleValue();
		case Long:
			if(value==null){
				value="0";
			}
			return Utils.toBigDecimal(value).longValue();
		case Float:
			if(value==null){
				value="0";
			}
			return Utils.toBigDecimal(value).floatValue();
		case BigDecimal:
			if(value==null){
				value="0";
			}
			return Utils.toBigDecimal(value);
		case Boolean:
			if(value==null){
				value="false";
			}
			return java.lang.Boolean.valueOf(value.toString());
		case Date:
			if(value==null){
				return null;
			}
			try {
				if(value instanceof Date){
					return (Date)value;
				}
				if(value.toString().equals("")){
					return null;
				}
				try{
					SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					return sd.parse(value.toString());
				}catch(Exception ex){
					SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
					return sd.parse(value.toString());
				}
					
			} catch (ParseException e) {
				throw new RuleException(e);
			}
		case List:
			if(value==null){
				return null;
			}
			if(value instanceof List){
				return (List)value;
			}
			List<Object> list=new ArrayList<Object>();
			String[] values=value.toString().split(",");
			for(String v:values){
				list.add(v);
			}
			return list;
		case Set:
			if(value==null){
				return null;
			}
			if(value instanceof Set){
				return (Set)value;
			}
			Set<Object> set=new TreeSet<Object>();
			for(String v:value.toString().split(",")){
				set.add(v);
			}
			return set;
		case Map:
			if(value==null){
				return null;
			}
			if(value instanceof Map){
				return (Map)value;
			}
			ObjectMapper mapper = new ObjectMapper();
			try {
				Map<String,Object> map=mapper.readValue(value.toString(), new HashMapTypeReference());
				return map;
			} catch (Exception e) {
				throw new RuleException(e);
			}
		case Enum:
			return value;
		}
		return null;
	};
	
}

class HashMapTypeReference extends TypeReference<HashMap<String,Object>>{
}
