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
