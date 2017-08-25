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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.rules.InvalidRuleSessionException;
import javax.rules.ObjectFilter;
import javax.rules.RuleExecutionSetMetadata;
import javax.rules.RuleRuntime;
import javax.rules.StatelessRuleSession;

import com.bstek.urule.action.ActionValue;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.response.RuleExecutionResponse;

/**
 * @author Jacky.gao
 * @since 2015年3月26日
 */
@SuppressWarnings("rawtypes")
public class StatelessRuleSessionImpl implements StatelessRuleSession {
	private KnowledgeSession knowledgeSession;
	
	public StatelessRuleSessionImpl(KnowledgeSession knowledgeSession){
		this.knowledgeSession=knowledgeSession;
	}
	@Override
	public RuleExecutionSetMetadata getRuleExecutionSetMetadata() throws InvalidRuleSessionException, RemoteException {
		RuleExecutionSetMetadataImpl metadata=new RuleExecutionSetMetadataImpl();
		return metadata;
	}

	@Override
	public int getType() throws RemoteException, InvalidRuleSessionException {
		return RuleRuntime.STATELESS_SESSION_TYPE;
	}

	@Override
	public void release() throws RemoteException, InvalidRuleSessionException {
	}

	@Override
	public List executeRules(List facts) throws InvalidRuleSessionException,RemoteException {
		for(Object fact:facts){
			knowledgeSession.insert(fact);
		}
		RuleExecutionResponse response=knowledgeSession.fireRules();
		List<Object> result=new ArrayList<Object>();
		Collection<ActionValue> responseObjects=response.getActionValues();
		result.addAll(responseObjects);
		return result;
	}

	@Override
	public List executeRules(List facts, ObjectFilter filter) throws InvalidRuleSessionException, RemoteException {
		return executeRules(facts);
	}
}
