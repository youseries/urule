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
package com.bstek.urule.builder.resource;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.urule.RuleException;

/**
 * @author Jacky.gao
 * @since 2014年12月22日
 */
public class FileResourceProvider implements ResourceProvider,ApplicationContextAware {
	private ApplicationContext applicationContext;
	public Resource provide(String path,String version) {
		try {
			InputStream inputStream=applicationContext.getResource(path).getInputStream();
			String content=IOUtils.toString(inputStream,"utf-8");
			IOUtils.closeQuietly(inputStream);
			return new Resource(content,path);
		} catch (IOException e) {
			throw new RuleException(e);
		}
	}

	public boolean support(String path) {
		return path.startsWith("classpath:") || path.startsWith("file:") || path.startsWith("WEB-INF/");
	}
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}
}
