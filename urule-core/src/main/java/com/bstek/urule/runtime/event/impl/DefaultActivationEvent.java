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
package com.bstek.urule.runtime.event.impl;

import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.agenda.Activation;
import com.bstek.urule.runtime.event.ActivationEvent;

/**
 * @author Jacky.gao
 * @since 2015年7月20日
 */
public class DefaultActivationEvent implements ActivationEvent {
	private Activation activation;
	private KnowledgeSession knowledgeSession;
	public DefaultActivationEvent(Activation activation,KnowledgeSession knowledgeSession) {
		this.activation = activation;
		this.knowledgeSession = knowledgeSession;
	}

	@Override
	public Activation getActivation() {
		return activation;
	}

	@Override
	public KnowledgeSession getKnowledgeSession() {
		return knowledgeSession;
	}
}
