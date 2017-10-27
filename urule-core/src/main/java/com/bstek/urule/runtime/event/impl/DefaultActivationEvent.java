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
