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
package com.bstek.urule.console.repository.updater;

import java.io.IOException;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.bstek.urule.RuleException;

/**
 * @author Jacky.gao
 * @since 2015年8月4日
 */
public abstract class AbstractReferenceUpdater implements ReferenceUpdater {
	protected String xmlToString(Document doc){
		StringWriter stringWriter = new StringWriter();
		OutputFormat xmlFormat = new OutputFormat();
		xmlFormat.setEncoding("UTF-8");
		XMLWriter xmlWriter = new XMLWriter(stringWriter, xmlFormat);
		try {
			xmlWriter.write(doc);
			xmlWriter.close();
			return stringWriter.toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuleException(e);
		}
	}
}
