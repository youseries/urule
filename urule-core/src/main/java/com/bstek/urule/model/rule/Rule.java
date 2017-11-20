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
package com.bstek.urule.model.rule;

import java.util.Date;

import com.bstek.urule.model.rule.lhs.Lhs;

/**
 * @author Jacky.gao
 * @since 2014年12月25日
 */
public class Rule implements RuleInfo{
	private String name;
	private Integer salience;
	private Date effectiveDate;
	private Date expiresDate;
	private Boolean enabled;
	private Boolean debug;
	private String activationGroup;
	private String agendaGroup;
	private Boolean autoFocus;
	private String ruleflowGroup;
	private Lhs lhs;
	private Rhs rhs;
	private Other other;
	private Boolean loop;
	private Boolean loopRule=false;
	private String remark;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public Integer getSalience() {
		return salience;
	}
	public void setSalience(Integer salience) {
		this.salience = salience;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public Boolean getDebug() {
		return debug;
	}
	public void setDebug(Boolean debug) {
		this.debug = debug;
	}
	public Boolean getAutoFocus() {
		return autoFocus;
	}
	public void setAutoFocus(Boolean autoFocus) {
		this.autoFocus = autoFocus;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Date getExpiresDate() {
		return expiresDate;
	}
	public void setExpiresDate(Date expiresDate) {
		this.expiresDate = expiresDate;
	}
	public String getActivationGroup() {
		return activationGroup;
	}
	public void setActivationGroup(String activationGroup) {
		this.activationGroup = activationGroup;
	}
	public String getAgendaGroup() {
		return agendaGroup;
	}
	public void setAgendaGroup(String agendaGroup) {
		this.agendaGroup = agendaGroup;
	}
	public String getRuleflowGroup() {
		return ruleflowGroup;
	}
	public void setRuleflowGroup(String ruleflowGroup) {
		this.ruleflowGroup = ruleflowGroup;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Lhs getLhs() {
		return lhs;
	}
	public void setLhs(Lhs lhs) {
		this.lhs = lhs;
	}
	public Rhs getRhs() {
		return rhs;
	}
	public void setRhs(Rhs rhs) {
		this.rhs = rhs;
	}
	public Other getOther() {
		return other;
	}
	public void setOther(Other other) {
		this.other = other;
	}
	
	public Boolean getLoop() {
		return loop;
	}
	public void setLoop(Boolean loop) {
		this.loop = loop;
	}
	public Boolean isLoopRule() {
		return loopRule;
	}
	public void setLoopRule(Boolean loopRule) {
		this.loopRule = loopRule;
	}
}
