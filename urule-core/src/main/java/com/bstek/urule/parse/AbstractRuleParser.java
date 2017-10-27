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
package com.bstek.urule.parse;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.bstek.urule.Configure;
import com.bstek.urule.RuleException;
import com.bstek.urule.model.rule.Rule;

/**
 * @author Jacky.gao
 * @since 2016年5月31日
 */
public abstract class AbstractRuleParser<T> implements Parser<T> {
	protected LhsParser lhsParser;
	protected RhsParser rhsParser;
	private OtherParser otherParser;
	public void parseRule(Rule rule,Element element) {
		rule.setName(element.attributeValue("name"));
		String salience=element.attributeValue("salience");
		if(StringUtils.isNotEmpty(salience)){
			rule.setSalience(Integer.valueOf(salience));
		}
		String effectiveDate=element.attributeValue("effective-date");
		SimpleDateFormat sd=new SimpleDateFormat(Configure.getDateFormat());
		if(StringUtils.isNotEmpty(effectiveDate)){
			try {
				rule.setEffectiveDate(sd.parse(effectiveDate));
			} catch (ParseException e) {
				throw new RuleException(e);
			}
		}
		String expiresDate=element.attributeValue("expires-date");
		if(StringUtils.isNotEmpty(expiresDate)){
			try {
				rule.setExpiresDate(sd.parse(expiresDate));
			} catch (ParseException e) {
				throw new RuleException(e);
			}
		}
		String enabled=element.attributeValue("enabled");
		if(StringUtils.isNotEmpty(enabled)){
			rule.setEnabled(Boolean.valueOf(enabled));
		}
		String loop=element.attributeValue("loop");
		if(StringUtils.isNotEmpty(loop)){
			rule.setLoop(Boolean.valueOf(loop));
		}
		rule.setActivationGroup(element.attributeValue("activation-group"));
		rule.setAgendaGroup(element.attributeValue("agenda-group"));
		String autoFocus=element.attributeValue("auto-focus");
		if(StringUtils.isNotEmpty(autoFocus)){
			rule.setAutoFocus(Boolean.valueOf(autoFocus));
		}
		rule.setRuleflowGroup(element.attributeValue("ruleflow-group"));
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(lhsParser.support(ele.getName())){
				rule.setLhs(lhsParser.parse(ele));				
			}else if(rhsParser.support(ele.getName())){
				rule.setRhs(rhsParser.parse(ele));				
			}else if(otherParser.support(ele.getName())){
				rule.setOther(otherParser.parse(ele));
			}else if(ele.getName().equals("remark")){
				rule.setRemark(ele.getText());
			}
		}
	}
	
	public void setLhsParser(LhsParser lhsParser) {
		this.lhsParser = lhsParser;
	}
	public void setRhsParser(RhsParser rhsParser) {
		this.rhsParser = rhsParser;
	}
	public void setOtherParser(OtherParser otherParser) {
		this.otherParser = otherParser;
	}
}
