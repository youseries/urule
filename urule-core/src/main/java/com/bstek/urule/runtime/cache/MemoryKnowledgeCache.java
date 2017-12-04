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
package com.bstek.urule.runtime.cache;

import com.bstek.urule.runtime.KnowledgePackage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jacky.gao
 * @since 2015年1月28日
 */
public class MemoryKnowledgeCache implements KnowledgeCache {

    private Map<String, KnowledgePackage> map = new ConcurrentHashMap<String, KnowledgePackage>();

    @Override
    public KnowledgePackage getKnowledge(String packageId) {
        if (packageId.startsWith("/")) {
            packageId = packageId.substring(1, packageId.length());
        }
        return map.get(packageId);
    }

    @Override
    public void putKnowledge(String packageId, KnowledgePackage knowledgePackage) {
        if (packageId.startsWith("/")) {
            packageId = packageId.substring(1, packageId.length());
        }
        map.put(packageId, knowledgePackage);
    }

    @Override
    public void removeKnowledge(String packageId) {
        map.remove(packageId);
    }
}
