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
package com.bstek.urule.model;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.bstek.urule.RuleException;

/**
 * @author Jacky.gao
 * @since 2016年6月2日
 */
public class GeneralEntity extends HashMap<String, Object>{
	private static final long serialVersionUID = 2778576006420277518L;
	private String targetClass;
	
	public GeneralEntity(String targetClass) {
		if(StringUtils.isBlank(targetClass)){
			throw new RuleException("Target class cannot be null.");
		}
		this.targetClass = targetClass;
	}

	public String getTargetClass() {
		return targetClass;
	}
	
	@Override
	public boolean equals(Object other) {
		boolean classEquals=false;
		if(other instanceof GeneralEntity){
			GeneralEntity entity=(GeneralEntity)other;
			if(targetClass.equals(entity.getTargetClass())){
				classEquals=true;
			}
		}
		if(classEquals){
			return super.equals(other);			
		}
		return false;
	}
}
