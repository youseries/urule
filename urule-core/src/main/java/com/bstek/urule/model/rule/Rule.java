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
