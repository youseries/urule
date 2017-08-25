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
package com.bstek.urule.model.rete;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public abstract class JunctionNode extends BaseReteNode{
	protected int toLineCount;
	@JsonIgnore
	protected List<Line> toConnections=new ArrayList<Line>();
	public JunctionNode(int id) {
		super(id);
	}
	public List<Line> getToConnections() {
		return toConnections;
	}
	public void addToConnection(Line connection){
		this.toConnections.add(connection);
		toLineCount=this.toConnections.size();
	}
	public int getToLineCount() {
		return toLineCount;
	}
}
