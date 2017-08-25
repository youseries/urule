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
import java.util.List;

import javax.rules.Handle;
import javax.rules.InvalidHandleException;
import javax.rules.InvalidRuleSessionException;
import javax.rules.ObjectFilter;
import javax.rules.RuleExecutionSetMetadata;
import javax.rules.RuleRuntime;
import javax.rules.StatefulRuleSession;

import com.bstek.urule.runtime.KnowledgeSession;

/**
 * @author Jacky.gao
 * @since 2015年3月26日
 */
@SuppressWarnings("rawtypes")
public class StatefulRuleSessionImpl implements StatefulRuleSession {
	private static final long serialVersionUID = -1211044317720246486L;
	private KnowledgeSession knowledgeSession;
	private List<Handle> allHandles=new ArrayList<Handle>();
	public StatefulRuleSessionImpl(KnowledgeSession knowledgeSession){
		this.knowledgeSession=knowledgeSession;
	}
	@Override
	public RuleExecutionSetMetadata getRuleExecutionSetMetadata() throws InvalidRuleSessionException, RemoteException {
		return null;
	}

	@Override
	public int getType() throws RemoteException, InvalidRuleSessionException {
		return RuleRuntime.STATEFUL_SESSION_TYPE;
	}

	@Override
	public void release() throws RemoteException, InvalidRuleSessionException {
		allHandles.clear();
	}

	@Override
	public Handle addObject(Object fact) throws RemoteException,InvalidRuleSessionException {
		Handle handle=new HandleImpl(fact);
		allHandles.add(handle);
		knowledgeSession.insert(fact);
		return handle;
	}

	@Override
	public List addObjects(List facts) throws RemoteException,InvalidRuleSessionException {
		List<Handle> handles=new ArrayList<Handle>();
		for(Object fact:facts){
			knowledgeSession.insert(fact);
			Handle handle=new HandleImpl(fact);
			allHandles.add(handle);
			handles.add(handle);
		}
		return handles;
	}

	@Override
	public boolean containsObject(Handle handle) throws RemoteException,InvalidRuleSessionException, InvalidHandleException {
		return allHandles.contains(handle);
	}

	@Override
	public void executeRules() throws RemoteException,InvalidRuleSessionException {
		knowledgeSession.fireRules();
	}

	@Override
	public List getHandles() throws RemoteException,InvalidRuleSessionException {
		return allHandles;
	}

	@Override
	public Object getObject(Handle handle) throws RemoteException,InvalidHandleException, InvalidRuleSessionException {
		HandleImpl h=(HandleImpl)handle;
		return h.getFact();
	}

	@Override
	public List getObjects() throws RemoteException,InvalidRuleSessionException {
		return knowledgeSession.getAllFacts();
	}

	@Override
	public List getObjects(ObjectFilter filter) throws RemoteException,InvalidRuleSessionException {
		List<Object> objects=new ArrayList<Object>();
		for(Object obj:knowledgeSession.getAllFacts()){
			Object targetObj=filter.filter(obj);
			if(targetObj!=null){
				objects.add(targetObj);
			}
		}
		return objects;
	}

	@Override
	public void removeObject(Handle handle) throws RemoteException,InvalidHandleException, InvalidRuleSessionException {
		allHandles.remove(handle);
		Object obj=((HandleImpl)handle).getFact();
		knowledgeSession.retract(obj);
	}

	@Override
	public void reset() throws RemoteException, InvalidRuleSessionException {
	}

	@Override
	public void updateObject(Handle handle, Object obj) throws RemoteException,InvalidRuleSessionException, InvalidHandleException {
		HandleImpl h=(HandleImpl)handle;
		h.setFact(obj);
		knowledgeSession.update(obj);
	}
}
