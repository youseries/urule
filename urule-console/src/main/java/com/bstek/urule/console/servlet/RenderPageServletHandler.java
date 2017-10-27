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
package com.bstek.urule.console.servlet;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.urule.Utils;


/**
 * @author Jacky.gao
 * @since 2016年6月6日
 */
public abstract class RenderPageServletHandler extends WriteJsonServletHandler implements ApplicationContextAware{
	protected VelocityEngine ve;
	protected ApplicationContext applicationContext;
	protected String buildProjectNameFromFile(String file) {
		String project=null;
		if(StringUtils.isNotBlank(file)){
			file=Utils.decodeURL(file);
			if(file.startsWith("/")){
				file=file.substring(1,file.length());
				int pos=file.indexOf("/");
				project=file.substring(0,pos);
			}
		}
		return project;
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
		ve = new VelocityEngine();
		ve.setProperty(Velocity.RESOURCE_LOADER, "class");
		ve.setProperty("class.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM,new NullLogChute());
		ve.init();	
	}
}
