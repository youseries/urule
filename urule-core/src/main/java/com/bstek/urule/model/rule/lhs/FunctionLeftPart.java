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
package com.bstek.urule.model.rule.lhs;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.bstek.urule.model.rule.Parameter;

/**
 * @author Jacky.gao
 * @since 2015年3月14日
 */
public class FunctionLeftPart implements LeftPart{
	@JsonIgnore
	private String id;
	private String name;
	private List<Parameter> parameters;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Parameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	@Override
	public String getId() {
		if(id==null){
			if(parameters!=null){
				String parametersId="";
				int i=0;
				for(Parameter parameter:parameters){
					if(i>0){
						parametersId+=",";
					}
					parametersId+=parameter.getId();
					i++;
				}
				id = "[function]."+name+"("+parametersId+")";				
			}else{
				id = "[function]."+name;								
			}
		}
		return id;
	}
}
