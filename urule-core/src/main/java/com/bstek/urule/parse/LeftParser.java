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
package com.bstek.urule.parse;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.bstek.urule.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.lhs.CommonFunctionLeftPart;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import com.bstek.urule.model.rule.lhs.FunctionLeftPart;
import com.bstek.urule.model.rule.lhs.Left;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.model.rule.lhs.MethodLeftPart;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;

/**
 * @author Jacky.gao
 * @since 2016年2月26日
 */
public class LeftParser extends AbstractParser<Left> {
	private SimpleArithmeticParser arithmeticParser;
	private ValueParser valueParser;
	@Override
	public Left parse(Element element) {
		Left left=new Left();
		String type=element.attributeValue("type");
		if(StringUtils.isNotEmpty(type)){
			left.setType(LeftType.valueOf(type));
		}else{
			left.setType(LeftType.variable);
		}
		switch(left.getType()){
		case variable:
			left.setLeftPart(buildVariableLeftPart(element));
			break;
		case function:
			left.setLeftPart(buildFunctionLeftPart(element));
			break;
		case method:
			left.setLeftPart(buildMethodLeftPart(element));
			break;
		case parameter:
			left.setLeftPart(buildVariableLeftPart(element));
			break;
		case commonfunction:
			left.setLeftPart(buildCommonFunctionLeftPart(element));
			break;
		case NamedReference:
			throw new RuleException("Not support reference type.");
		case all:
			throw new RuleException("Not support all type.");
		case exist:
			throw new RuleException("Not support exist type.");
		case collect:
			throw new RuleException("Not support collect type.");
		case eval:
			throw new RuleException("Not support eval type.");
		}
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(arithmeticParser.support(ele.getName())){
				left.setArithmetic(arithmeticParser.parse(ele));
			}
		}
		return left;
	}

	
	private CommonFunctionLeftPart buildCommonFunctionLeftPart(Element element){
		CommonFunctionLeftPart part=new CommonFunctionLeftPart();
		part.setName(element.attributeValue("function-name"));
		part.setLabel(element.attributeValue("function-label"));
		for(Object obj:element.elements()){
			if(!(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			if(!ele.getName().equals("function-parameter")){
				continue;
			}
			CommonFunctionParameter p=new CommonFunctionParameter();
			p.setName(ele.attributeValue("name"));
			p.setProperty(ele.attributeValue("property-name"));
			p.setPropertyLabel(ele.attributeValue("property-label"));
			for(Object object:ele.elements()){
				if(!(object instanceof Element)){
					continue;
				}
				Element e=(Element)object;
				if(!e.getName().equals("value")){
					continue;
				}
				p.setObjectParameter(valueParser.parse(e));
			}
			part.setParameter(p);
		}
		return part;
	}
	
	private MethodLeftPart buildMethodLeftPart(Element element){
		MethodLeftPart part=new MethodLeftPart();
		part.setBeanId(element.attributeValue("bean-name"));
		part.setBeanLabel(element.attributeValue("bean-label"));
		part.setMethodLabel(element.attributeValue("method-label"));
		part.setMethodName(element.attributeValue("method-name"));
		part.setParameters(parseParameters(element, valueParser));
		return part;
	}
	
	private FunctionLeftPart buildFunctionLeftPart(Element element){
		FunctionLeftPart part=new FunctionLeftPart();
		part.setName(element.attributeValue("name"));
		part.setParameters(parseParameters(element, valueParser));
		return part;
	}
	
	private VariableLeftPart buildVariableLeftPart(Element element){
		VariableLeftPart part=new VariableLeftPart();
		String variable=element.attributeValue("var");
		if(StringUtils.isNotEmpty(variable)){
			part.setVariableName(variable);
		}
		String variableLabel=element.attributeValue("var-label");
		if(StringUtils.isNotEmpty(variableLabel)){
			part.setVariableLabel(variableLabel);
		}
		String variableCategory=element.attributeValue("var-category");
		if(StringUtils.isNotEmpty(variableCategory)){
			part.setVariableCategory(variableCategory);
		}
		String datatype=element.attributeValue("datatype");
		if(StringUtils.isNotEmpty(datatype)){
			part.setDatatype(Datatype.valueOf(datatype));
		}
		return part;
	}
	public void setValueParser(ValueParser valueParser) {
		this.valueParser = valueParser;
	}
	public void setArithmeticParser(SimpleArithmeticParser arithmeticParser) {
		this.arithmeticParser = arithmeticParser;
	}
	@Override
	public boolean support(String name) {
		return name.equals("left");
	}
}
