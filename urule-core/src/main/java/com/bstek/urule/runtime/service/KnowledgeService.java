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
