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

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.AbstractValue;
import com.bstek.urule.model.rule.CommonFunctionValue;
import com.bstek.urule.model.rule.ConstantValue;
import com.bstek.urule.model.rule.MethodValue;
import com.bstek.urule.model.rule.NamedReferenceValue;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.ParameterValue;
import com.bstek.urule.model.rule.SimpleValue;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.ValueType;
import com.bstek.urule.model.rule.VariableCategoryValue;
import com.bstek.urule.model.rule.VariableValue;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
/**
 * @author Jacky.gao
 * @since 2014年12月23日
 */
public class ValueParser extends AbstractParser<Value> {
	private ComplexArithmeticParser arithmeticParser;
	public Value parse(Element element) {
		AbstractValue av=null;
		ValueType type=ValueType.valueOf(element.attributeValue("type"));
		if(type.equals(ValueType.Input)){
			SimpleValue sv=new SimpleValue();
			String content=element.attributeValue("content");
			if(StringUtils.isNotEmpty(content)){
				sv.setContent(StringEscapeUtils.unescapeXml(content));
			}
			av=sv;
		}else if(type.equals(ValueType.Parameter)){
			ParameterValue pv=new ParameterValue();
			pv.setVariableName(element.attributeValue("var"));
			pv.setVariableLabel(element.attributeValue("var-label"));
			av=pv;
		}else if(type.equals(ValueType.Variable)){
			VariableValue vv=new VariableValue();
			String variable=element.attributeValue("var");
			if(StringUtils.isNotEmpty(variable)){
				vv.setVariableName(variable);
			}
			String variableLabel=element.attributeValue("var-label");
			if(StringUtils.isNotEmpty(variableLabel)){
				vv.setVariableLabel(variableLabel);
			}
			String datatype=element.attributeValue("datatype");
			if(StringUtils.isNotEmpty(datatype)){
				vv.setDatatype(Datatype.valueOf(datatype));
			}
			String variableCategory=element.attributeValue("var-category");
			if(StringUtils.isNotEmpty(variableCategory)){
				vv.setVariableCategory(variableCategory);
			}
			av=vv;
		}else if(type.equals(ValueType.VariableCategory)){
			String variableCategory=element.attributeValue("var-category");
			av=new VariableCategoryValue(variableCategory);
		}else if(type.equals(ValueType.Method)){
			MethodValue mv=new MethodValue();
			String beanName=element.attributeValue("bean-name");
			mv.setBeanId(beanName);
			String beanLabel=element.attributeValue("bean-label");
			mv.setBeanLabel(beanLabel);
			String methodName=element.attributeValue("method-name");
			mv.setMethodName(methodName);
			String methodLabel=element.attributeValue("method-label");
			mv.setMethodLabel(methodLabel);
			List<Parameter> parameters = parseParameters(element,this);
			mv.setParameters(parameters);
			av=mv;
		}else if(type.equals(ValueType.CommonFunction)){
			CommonFunctionValue value=new CommonFunctionValue();
			value.setName(element.attributeValue("function-name"));
			value.setLabel(element.attributeValue("function-label"));
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
				p.setPropertyLabel(ele.attributeValue("property-name"));
				for(Object object:ele.elements()){
					if(!(object instanceof Element)){
						continue;
					}
					Element e=(Element)object;
					if(!e.getName().equals("value")){
						continue;
					}
					p.setObjectParameter(this.parse(e));
				}
				value.setParameter(p);
			}
			av=value;
		}else if(type.equals(ValueType.NamedReference)){
			NamedReferenceValue value=new NamedReferenceValue();
			value.setReferenceName(element.attributeValue("reference-name"));
			value.setPropertyName(element.attributeValue("property-name"));
			value.setPropertyLabel(element.attributeValue("property-label"));
			value.setDatatype(Datatype.valueOf(element.attributeValue("datatype")));
			av=value;
		}else{
			ConstantValue cv=new ConstantValue();
			String constant=element.attributeValue("const");
			if(StringUtils.isNotEmpty(constant)){
				cv.setConstantName(constant);
			}
			String constantLabel=element.attributeValue("const-label");
			if(StringUtils.isNotEmpty(constantLabel)){
				cv.setConstantLabel(constantLabel);
			}
			String constantCategory=element.attributeValue("const-category");
			if(StringUtils.isNotEmpty(constantCategory)){
				cv.setConstantCategory(constantCategory);
			}
			av=cv;
		}
		for(Object obj:element.elements()){
			if(obj==null || !(obj instanceof Element)){
				continue;
			}
			Element ele=(Element)obj;
			String name=ele.getName();
			if(arithmeticParser.support(name)){
				av.setArithmetic(arithmeticParser.parse(ele));
				break;
			}
		}
		return av;
	}
	public boolean support(String name) {
		return name.equals("value");
	}
	public void setArithmeticParser(ComplexArithmeticParser arithmeticParser) {
		this.arithmeticParser = arithmeticParser;
	}
}
