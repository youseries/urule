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
package com.bstek.urule.model.rete.builder;

import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.rete.ObjectTypeNode;
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
}
