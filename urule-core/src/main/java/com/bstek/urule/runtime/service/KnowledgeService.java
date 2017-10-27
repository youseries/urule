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
package com.bstek.urule.runtime.service;

import java.io.IOException;

import com.bstek.urule.runtime.KnowledgePackage;

/**
 * @author Jacky.gao
 * @since 2015年1月28日
 */
public interface KnowledgeService {
	public static final String BEAN_ID="urule.knowledgeService";
	/**
	 * 根据给定的资源包ID获取对应的KnowledgePackage对象
	 * @param packageId 资源包ID
	 * @return 返回与给定的资源包ID获取对应的KnowledgePackage对象
	 * @throws IOException 抛出IO异常
	 */
	KnowledgePackage getKnowledge(String packageId) throws IOException;
	/**
	 * 根据给定的一个或多个资源包ID获取对应的KnowledgePackage对象的集合
	 * @param packageIds 资源包ID数组
	 * @return 返回与给定的一个或多个资源包ID获取对应的KnowledgePackage对象集合
	 * @throws IOException 抛出IO异常
	 */
	KnowledgePackage[] getKnowledges(String[] packageIds) throws IOException;
}
