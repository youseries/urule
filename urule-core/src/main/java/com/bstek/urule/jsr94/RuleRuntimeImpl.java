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
package com.bstek.urule.jsr94;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import javax.rules.RuleExecutionSetNotFoundException;
import javax.rules.RuleRuntime;
import javax.rules.RuleSession;
import javax.rules.RuleSessionCreateException;
import javax.rules.RuleSessionTypeUnsupportedException;

import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import com.bstek.urule.runtime.service.KnowledgePackageService;

/**
 * @author Jacky.gao
 * @since 2015年3月26日
 */
@SuppressWarnings("rawtypes")
public class RuleRuntimeImpl implements RuleRuntime {
	private static final long serialVersionUID = 3464001902505591929L;
	private KnowledgePackageService knowledgePackageService;
	public RuleRuntimeImpl(KnowledgePackageService knowledgePackageService) {
		this.knowledgePackageService=knowledgePackageService;
	}
	@Override
	public RuleSession createRuleSession(String uri, Map properties, int ruleSessionType) throws RuleSessionTypeUnsupportedException,RuleSessionCreateException, RuleExecutionSetNotFoundException,RemoteException {
		KnowledgePackage knowledgePackage;
		try {
			knowledgePackage = knowledgePackageService.buildKnowledgePackage(uri);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuleSessionCreateException(e.getMessage());
		}
		KnowledgeSession knowledgeSession=KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage);
		RuleSession ruleSession=null;
		if(ruleSessionType==RuleRuntime.STATEFUL_SESSION_TYPE){
			ruleSession=new StatefulRuleSessionImpl(knowledgeSession);
		}else if(ruleSessionType==RuleRuntime.STATELESS_SESSION_TYPE){
			ruleSession=new StatelessRuleSessionImpl(knowledgeSession);			
		}else{
			throw new RuleSessionTypeUnsupportedException("Unsupport rule session type ["+ruleSessionType+"]");
		}
		return ruleSession;
	}

	@Override
	public List getRegistrations() throws RemoteException {
		return null;
	}
}
