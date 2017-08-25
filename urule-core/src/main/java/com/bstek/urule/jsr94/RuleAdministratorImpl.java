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
import java.util.Map;

import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetDeregistrationException;
import javax.rules.admin.RuleExecutionSetProvider;
import javax.rules.admin.RuleExecutionSetRegisterException;

/**
 * @author Jacky.gao
 * @since 2015年3月26日
 */
@SuppressWarnings("rawtypes")
public class RuleAdministratorImpl implements RuleAdministrator {

	@Override
	public void deregisterRuleExecutionSet(String arg0, Map arg1) throws RuleExecutionSetDeregistrationException, RemoteException {

	}

	@Override
	public LocalRuleExecutionSetProvider getLocalRuleExecutionSetProvider(Map arg0) throws RemoteException {
		return null;
	}

	@Override
	public RuleExecutionSetProvider getRuleExecutionSetProvider(Map arg0) throws RemoteException {
		return null;
	}

	@Override
	public void registerRuleExecutionSet(String arg0, RuleExecutionSet arg1,Map arg2) throws RuleExecutionSetRegisterException, RemoteException {

	}
}
