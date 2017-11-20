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
package com.bstek.urule.model.rete.builder;

import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.rete.ObjectTypeNode;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.lhs.BaseCriteria;

/**
 * @author Jacky.gao
 * @since 2015年1月8日
 */
public interface BuildContext {
	String getObjectType(BaseCriteria criteria);
	ObjectTypeNode getObjectTypeNode(BaseCriteria criteria);
	boolean assertSameType(BaseCriteria left,BaseCriteria right);
	ResourceLibrary getResourceLibrary();
	ObjectTypeNode buildObjectTypeNode(String className);
	int nextId();
	void setCurrentRule(Rule rule);
	boolean currentRuleIsDebug();
}
