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
package com.bstek.urule.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.urule.RuleException;
import com.bstek.urule.action.Action;
import com.bstek.urule.action.ConsolePrintAction;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.action.VariableAssignAction;
import com.bstek.urule.model.flow.Connection;
import com.bstek.urule.model.flow.DecisionItem;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.library.action.ActionLibrary;
import com.bstek.urule.model.library.action.Method;
import com.bstek.urule.model.library.action.SpringBean;
import com.bstek.urule.model.library.constant.ConstantCategory;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rule.CommonFunctionValue;
import com.bstek.urule.model.rule.ComplexArithmetic;
import com.bstek.urule.model.rule.ConstantValue;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.MethodValue;
import com.bstek.urule.model.rule.NamedReferenceValue;
import com.bstek.urule.model.rule.Other;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.ParameterValue;
import com.bstek.urule.model.rule.ParenValue;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.VariableCategoryValue;
import com.bstek.urule.model.rule.VariableValue;
import com.bstek.urule.model.rule.lhs.AbstractLeftPart;
import com.bstek.urule.model.rule.lhs.CommonFunctionLeftPart;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.CriteriaUnit;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.FunctionLeftPart;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.JunctionType;
import com.bstek.urule.model.rule.lhs.Left;
import com.bstek.urule.model.rule.lhs.LeftPart;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.model.rule.lhs.MethodLeftPart;
import com.bstek.urule.model.rule.lhs.NamedCriteria;
import com.bstek.urule.model.rule.lhs.NamedItem;
import com.bstek.urule.model.rule.lhs.NamedJunction;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;
import com.bstek.urule.model.rule.loop.LoopEnd;
import com.bstek.urule.model.rule.loop.LoopRule;
import com.bstek.urule.model.rule.loop.LoopStart;
import com.bstek.urule.model.rule.loop.LoopTarget;

/**
 * @author Jacky.gao
 * @since 2015年8月19日
 */
public class RulesRebuilder {
	private ResourceLibraryBuilder resourceLibraryBuilder;
	public void rebuildRules(List<Library> libraries, List<Rule> rules) {
		if(libraries==null){
			return;
		}
		if(rules==null){
			return;
		}
		ResourceLibrary resLibraries=resourceLibraryBuilder.buildResourceLibrary(libraries);
		for(Rule rule:rules){
			Map<String,String> namedMap=new HashMap<String,String>();
			if(rule.getLhs()!=null){
				Criterion criterion=rule.getLhs().getCriterion();
				rebuildCriterion(criterion, resLibraries,namedMap,false);
			}
			Rhs rhs=rule.getRhs();
			List<Action> actions=rhs.getActions();
			if(actions!=null){
				for(Action action:actions){
					rebuildAction(action,resLibraries,namedMap,false);
				}
			}
			Other other=rule.getOther();
			if(other!=null){				
				List<Action> otherActions=other.getActions();
				if(otherActions!=null){
					for(Action action:otherActions){
						rebuildAction(action,resLibraries,namedMap,false);
					}
				}
			}
			if(rule instanceof LoopRule){
				LoopRule loopRule=(LoopRule)rule;
				LoopTarget target=loopRule.getLoopTarget();
				if(target!=null){
					Value value=target.getValue();
					rebuildValue(value, resLibraries,namedMap,false);
				}
				
				LoopStart start=loopRule.getLoopStart();
				if(start!=null && start.getActions()!=null){
					for(Action action:start.getActions()){
						rebuildAction(action,resLibraries,namedMap,false);
					}
				}
				LoopEnd end=loopRule.getLoopEnd();
				if(end!=null && end.getActions()!=null){
					for(Action action:end.getActions()){
						rebuildAction(action,resLibraries,namedMap,false);
					}
				}
			}
		}
	}
	public void rebuildRulesForDSL(List<Library> libraries, List<Rule> rules) {
		if(libraries==null){
			return;
		}
		if(rules==null){
			return;
		}
		ResourceLibrary resLibraries=resourceLibraryBuilder.buildResourceLibrary(libraries);
		for(Rule rule:rules){
			Map<String,String> namedMap=new HashMap<String,String>();
			if(rule.getLhs()!=null){
				Criterion criterion=rule.getLhs().getCriterion();
				rebuildCriterion(criterion, resLibraries,namedMap,true);
			}
			Rhs rhs=rule.getRhs();
			List<Action> actions=rhs.getActions();
			if(actions!=null){
				for(Action action:actions){
					rebuildAction(action,resLibraries,namedMap,true);
				}
			}
			Other other=rule.getOther();
			if(other!=null){				
				List<Action> otherActions=other.getActions();
				if(otherActions!=null){
					for(Action action:otherActions){
						rebuildAction(action,resLibraries,namedMap,true);
					}
				}
			}
			if(rule instanceof LoopRule){
				LoopRule loopRule=(LoopRule)rule;
				LoopTarget target=loopRule.getLoopTarget();
				if(target!=null){
					Value value=target.getValue();
					rebuildValue(value, resLibraries,namedMap,true);
				}
				
				LoopStart start=loopRule.getLoopStart();
				if(start!=null && start.getActions()!=null){
					for(Action action:start.getActions()){
						rebuildAction(action,resLibraries,namedMap,true);
					}
				}
				LoopEnd end=loopRule.getLoopEnd();
				if(end!=null && end.getActions()!=null){
					for(Action action:end.getActions()){
						rebuildAction(action,resLibraries,namedMap,true);
					}
				}
			}
		}
	}
	
	public void convertNamedJunctions(List<Rule> rules){
		for(Rule rule:rules){
			if(rule.getLhs()==null){
				continue;
			}
			Criterion criterion=rule.getLhs().getCriterion();
			Criterion newCriterion=buildCriterion(criterion);
			rule.getLhs().setCriterion(newCriterion);
		}
	}

	private Criterion buildCriterion(Criterion criterion) {
		if(!(criterion instanceof NamedJunction) && !(criterion instanceof Junction)){
			return criterion;
		}
		if(criterion instanceof NamedJunction){
			NamedJunction jun=(NamedJunction)criterion;
			NamedCriteria criteria=buildNamedJunction(jun);
			return criteria;
		}else if(criterion instanceof Junction){
			buildJunction((Junction)criterion);
		}
		return criterion;
	}

	private void buildJunction(Junction jun) {
		List<Criterion> criterions=jun.getCriterions();
		List<Criterion> newCriterions=new ArrayList<Criterion>();
		for(Criterion c:criterions){
			NamedCriteria namedCriteria=buildNamedJunction(c);
			if(namedCriteria!=null){
				newCriterions.add(namedCriteria);
			}else if(c instanceof Junction){
				buildJunction((Junction)c);
			}
			newCriterions.add(c);
		}
		jun.setCriterions(newCriterions);
	}

	private NamedCriteria buildNamedJunction(Criterion criterion) {
		if(!(criterion instanceof NamedJunction)){
			return null;
		}
		NamedJunction jun=(NamedJunction)criterion;
		NamedCriteria criteria=new NamedCriteria();
		criteria.setReferenceName(jun.getReferenceName());
		criteria.setParent(jun.getParent());
		criteria.setVariableCategory(jun.getVariableCategory());
		JunctionType junctionType=jun.getJunctionType();
		List<NamedItem> items=jun.getItems();
		List<CriteriaUnit> nextUnits=null;
		for(NamedItem item:items){
			CriteriaUnit unit=new CriteriaUnit();
			unit.setJunctionType(junctionType);
			Criteria c=new Criteria();
			unit.setCriteria(c);
			c.setOp(item.getOp());
			Left left=new Left();
			left.setType(LeftType.NamedReference);
			VariableLeftPart leftPart=new VariableLeftPart();
			leftPart.setDatatype(item.getDatatype());
			leftPart.setVariableCategory(jun.getVariableCategory());
			leftPart.setVariableLabel(item.getVariableLabel());
			leftPart.setVariableName(item.getVariableName());
			left.setLeftPart(leftPart);
			c.setLeft(left);
			c.setValue(item.getValue());
			if(nextUnits==null){
				criteria.setUnit(unit);
				nextUnits=new ArrayList<CriteriaUnit>();
				unit.setNextUnits(nextUnits);
			}else{
				nextUnits.add(unit);
			}
		}
		return criteria;
	}

	public void rebuildAction(Action action,ResourceLibrary resLibraries,Map<String,String> namedMap,boolean forDSL){
		if(action==null){
			return;
		}
		if(action instanceof VariableAssignAction){
			List<VariableCategory> variableCategories=resLibraries.getVariableCategories();
			if(variableCategories==null){
				return;
			}
			VariableAssignAction varAction=(VariableAssignAction)action;
			LeftType type=varAction.getType();
			if(type==null || !type.equals(LeftType.NamedReference)){
				String variableCategory=varAction.getVariableCategory();
				String variableLabel=varAction.getVariableLabel();
				if(variableLabel.equals(Connection.RETURN_VALUE_KEY)){
					varAction.setVariableName(variableLabel);
					varAction.setDatatype(Datatype.Boolean);	
				}else if(variableLabel.equals(DecisionItem.RETURN_VALUE_KEY)){
					varAction.setVariableName(variableLabel);
					varAction.setDatatype(Datatype.String);	
				}else{
					String variableName=varAction.getVariableName();
					if(forDSL){
						Variable var=getVariableByLabel(variableCategories, variableCategory, variableLabel,namedMap);
						varAction.setVariableName(var.getName());
						varAction.setDatatype(var.getType());																			
					}else{
						if(StringUtils.isNotBlank(variableName)){
							Variable var=getVariableByName(variableCategories, variableCategory, variableName,namedMap);
							varAction.setVariableLabel(var.getLabel());
							varAction.setDatatype(var.getType());													
						}else{
							Variable var=getVariableByLabel(variableCategories, variableCategory, variableLabel,namedMap);
							varAction.setVariableName(var.getName());
							varAction.setDatatype(var.getType());													
						}						
					}
				}
			}
			if(type!=null && type.equals(LeftType.NamedReference)){
				String refName=varAction.getReferenceName();
				String variableCategory=namedMap.get(refName);
				if(variableCategory==null){
					refName=refName.substring(1,refName.length());
					variableCategory=namedMap.get(refName);
				}
				if(variableCategory==null){
					throw new RuleException("Reference ["+refName+"] not define.");
				}
				if(forDSL){
					Variable var=getVariableByLabel(variableCategories, variableCategory, varAction.getVariableLabel(),namedMap);
					varAction.setVariableName(var.getName());
					varAction.setVariableCategory(variableCategory);
					varAction.setDatatype(var.getType());					
				}else{
					String variableName=varAction.getVariableName();
					if(StringUtils.isNotBlank(variableName)){
						Variable var=getVariableByLabel(variableCategories, variableCategory, variableName,namedMap);
						varAction.setVariableLabel(var.getLabel());
						varAction.setVariableCategory(variableCategory);
						varAction.setDatatype(var.getType());	
					}else{
						Variable var=getVariableByLabel(variableCategories, variableCategory, varAction.getVariableLabel(),namedMap);
						varAction.setVariableName(var.getName());
						varAction.setVariableCategory(variableCategory);
						varAction.setDatatype(var.getType());
					}
				}
			}
			Value value=((VariableAssignAction) action).getValue();
			rebuildValue(value,resLibraries,namedMap,forDSL);
		}else if(action instanceof ConsolePrintAction){
			ConsolePrintAction consoleAction=(ConsolePrintAction)action;
			Value value=consoleAction.getValue();
			rebuildValue(value, resLibraries,namedMap,forDSL);
		}else if(action instanceof ExecuteMethodAction){
			List<ActionLibrary> actionLibraries=resLibraries.getActionLibraries();
			if(actionLibraries==null){
				return;
			}
			ExecuteMethodAction methodAction=(ExecuteMethodAction)action;
			String beanLabel=methodAction.getBeanLabel();
			String methodLabel=methodAction.getMethodLabel();
			SpringBean targetBean=null;
			for(ActionLibrary al:actionLibraries){
				List<SpringBean> beans=al.getSpringBeans();
				if(beans==null){
					continue;
				}
				for(SpringBean bean:beans){
					if(beanLabel.equals(bean.getName())){
						targetBean=bean;
						break;
					}
				}
				if(targetBean!=null)break;
			}
			Method targetMethod=null;
			if(targetBean!=null){
				methodAction.setBeanId(targetBean.getId());
				List<Method> methods=targetBean.getMethods();
				if(methods==null){
					throw new RuleException("Bean ["+beanLabel+"] not define methods.");
				}
				for(Method method:methods){
					if(method.getName().equals(methodLabel)){
						targetMethod=method;
						break;
					}
				}
				if(targetMethod==null){
					throw new RuleException("Bean ["+beanLabel+"] method["+methodLabel+"] not define.");
				}
				methodAction.setMethodName(targetMethod.getMethodName());
			}
			List<Parameter> parameters=methodAction.getParameters();
			rebuildParameters(resLibraries, parameters,targetMethod.getParameters(),namedMap,forDSL);
		}
	}
	
	private void rebuildCommonFunctionParameter(CommonFunctionParameter parameter,ResourceLibrary resLibraries,Map<String,String> namedMap,boolean forDSL){
		String property=parameter.getProperty();
		if(StringUtils.isEmpty(property)){
			return;
		}
		Value value=parameter.getObjectParameter();
		rebuildValue(value, resLibraries,namedMap,forDSL);
		String category=null;
		if(value instanceof VariableValue){
			VariableValue vv=(VariableValue)value;
			category=vv.getVariableCategory();
		}else if(value instanceof VariableCategoryValue){
			VariableCategoryValue vc=(VariableCategoryValue)value;
			category=vc.getVariableCategory();
		}else{
			throw new RuleException("Function parameter is invalid.");
		}
		List<VariableCategory> variableCategories=resLibraries.getVariableCategories();
		for(VariableCategory vc:variableCategories){
			if(!category.equals(vc.getName())){
				continue;
			}
			for(Variable v:vc.getVariables()){
				if(v.getName().equals(property) || v.getLabel().equals(property)){
					parameter.setProperty(v.getName());
					parameter.setPropertyLabel(v.getLabel());
					break;
				}
			}
		}
	}
	
	private void rebuildParameters(ResourceLibrary resLibraries,List<Parameter> parameters,List<com.bstek.urule.model.library.action.Parameter> targetParameters,Map<String,String> namedMap,boolean forDSL) {
		if(parameters!=null && targetParameters!=null){
			for(int i=0;i<parameters.size();i++){
				if(i>targetParameters.size()-1){
					break;
				}
				Parameter parameter=parameters.get(i);
				com.bstek.urule.model.library.action.Parameter p=targetParameters.get(i);
				parameter.setType(p.getType());
				Value value=parameter.getValue();
				rebuildValue(value, resLibraries,namedMap,forDSL);
			}
		}
	}
	public void rebuildCriterion(Criterion criterion,ResourceLibrary resLibraries,Map<String,String> namedMap,boolean forDSL){
		if(criterion==null){
			return;
		}
		if(criterion instanceof Criteria){
			Criteria criteria=(Criteria)criterion;
			rebuildCriteria(resLibraries,criteria,namedMap,forDSL);
		}else if(criterion instanceof Junction){
			Junction junction=(Junction)criterion;
			Collection<Criterion> criterionList=junction.getCriterions();
			if(criterionList!=null){
				for(Criterion c:criterionList){
					rebuildCriterion(c, resLibraries,namedMap,forDSL);
				}
			}
		}else if(criterion instanceof NamedCriteria){
			NamedCriteria namedCriteria=(NamedCriteria)criterion;
			namedMap.put(namedCriteria.getReferenceName(), namedCriteria.getVariableCategory());
			CriteriaUnit unit=namedCriteria.getUnit();
			buildCriteriaUnit(unit,resLibraries,namedMap,forDSL);
		}else if(criterion instanceof NamedJunction){
			NamedJunction jun=(NamedJunction)criterion;
			namedMap.put(jun.getReferenceName(), jun.getVariableCategory());
		}
	}
	
	private void buildCriteriaUnit(CriteriaUnit unit,ResourceLibrary resLibraries,Map<String,String> namedMap,boolean forDSL){
		Criteria criteria=unit.getCriteria();
		if(criteria!=null){
			rebuildCriteria(resLibraries,criteria,namedMap,forDSL);				
		}
		List<CriteriaUnit> units=unit.getNextUnits();
		if(units!=null){
			for(CriteriaUnit nextUnit:units){
				buildCriteriaUnit(nextUnit, resLibraries,namedMap,forDSL);
			}
		}
	}

	private void rebuildCriteria(ResourceLibrary resLibraries,Criteria criteria,Map<String,String> namedMap,boolean forDSL) {
		List<VariableCategory> variableCategories=resLibraries.getVariableCategories();
		Left left=criteria.getLeft();
		LeftPart leftPart=left.getLeftPart();
		if(leftPart instanceof VariableLeftPart){
			VariableLeftPart part=(VariableLeftPart)leftPart;
			String variableLabel=part.getVariableLabel();
			String variableName=part.getVariableName();
			if(StringUtils.isNotBlank(variableLabel)){
				String variableCategory=part.getVariableCategory();
				if(forDSL){
					Variable var=getVariableByLabel(variableCategories, variableCategory, variableLabel,namedMap);
					part.setVariableName(var.getName());
					part.setDatatype(var.getType());									
				}else{					
					Variable var=getVariableByName(variableCategories, variableCategory, variableName,namedMap);
					part.setVariableLabel(var.getLabel());
					part.setDatatype(var.getType());									
				}
			}
		}else if(leftPart instanceof AbstractLeftPart){
			AbstractLeftPart part=(AbstractLeftPart)leftPart; 
			String variableCategory=part.getVariableCategory();
			String variableLabel=part.getVariableLabel();
			String variableName=part.getVariableName();
			if(forDSL){
				Variable var=getVariableByLabel(variableCategories, variableCategory, variableLabel,namedMap);
				part.setVariableName(var.getName());
			}else{
				Variable var=getVariableByName(variableCategories, variableCategory, variableName,namedMap);
				part.setVariableLabel(var.getLabel());								
			}
		}else if(leftPart instanceof CommonFunctionLeftPart){
			CommonFunctionLeftPart p=(CommonFunctionLeftPart)leftPart;
			CommonFunctionParameter parameter=p.getParameter();
			rebuildCommonFunctionParameter(parameter,resLibraries,namedMap,forDSL);
		}else if(leftPart instanceof MethodLeftPart){
			MethodLeftPart part=(MethodLeftPart)leftPart;
			String beanLabel=part.getBeanLabel();
			String methodLabel=part.getMethodLabel();
			List<ActionLibrary> actionLibraries=resLibraries.getActionLibraries();
			SpringBean targetBean=null;
			for(ActionLibrary al:actionLibraries){
				List<SpringBean> beans=al.getSpringBeans();
				for(SpringBean bean:beans){
					if(beanLabel.equals(bean.getName())){
						part.setBeanId(bean.getId());
						targetBean=bean;
						break;
					}
				}
				if(targetBean!=null){
					break;
				}
			}
			if(targetBean==null){
				throw new RuleException("Bean["+beanLabel+"] not exist.");
			}
			Method targetMethod=null;
			for(Method method:targetBean.getMethods()){
				if(methodLabel.equals(method.getName())){
					targetMethod=method;
					part.setMethodName(method.getMethodName());
					break;
				}
			}
			if(targetMethod==null){
				throw new RuleException("Bean["+beanLabel+"] method["+targetMethod+"] not exist.");
			}
			List<Parameter> parameters=part.getParameters();
			rebuildParameters(resLibraries, parameters,targetMethod.getParameters(),namedMap,forDSL);
		}else if(leftPart instanceof FunctionLeftPart){
			FunctionLeftPart part=(FunctionLeftPart)leftPart;
			List<Parameter> parameters=part.getParameters();
			if(parameters!=null && parameters.size()>0){
				for(Parameter param:parameters){
					Value pv=param.getValue();
					if(pv==null){
						continue;
					}
					rebuildValue(pv,resLibraries,namedMap,forDSL);
				}				
			}
		}
		Value value=criteria.getValue();
		rebuildValue(value, resLibraries,namedMap,forDSL);
	}
	
	 public void rebuildValue(Value value,ResourceLibrary resLibraries,Map<String,String> namedMap,boolean forDSL){
		if(value==null){
			return;
		}
		if(value instanceof ParenValue){
			ParenValue pv=(ParenValue)value;
			Value v=pv.getValue();
			rebuildValue(v,resLibraries,namedMap,forDSL);
		}else if(value instanceof ConstantValue){
			ConstantValue cv=(ConstantValue)value;
			String category=cv.getConstantCategory();
			if(forDSL){
				String label=cv.getConstantLabel();
				com.bstek.urule.model.library.constant.Constant constant=getConstantByLabel(resLibraries.getConstantCategories(), category, label);
				cv.setConstantName(constant.getName());				
			}else{
				String name=cv.getConstantName();
				com.bstek.urule.model.library.constant.Constant constant=getConstantByName(resLibraries.getConstantCategories(), category, name);
				cv.setConstantLabel(constant.getLabel());				
			}
		}else if(value instanceof VariableValue){
			VariableValue variableValue=(VariableValue)value;
				if(forDSL){
					if(StringUtils.isNotBlank(variableValue.getVariableLabel())){
						Variable var=getVariableByLabel(resLibraries.getVariableCategories(), variableValue.getVariableCategory(), variableValue.getVariableLabel(),namedMap);
						variableValue.setVariableName(var.getName());
						variableValue.setDatatype(var.getType());					
					}
				}else{
					if(StringUtils.isNotBlank(variableValue.getVariableName())){
						Variable var=getVariableByName(resLibraries.getVariableCategories(), variableValue.getVariableCategory(), variableValue.getVariableName(),namedMap);
						variableValue.setVariableLabel(var.getLabel());
						variableValue.setDatatype(var.getType());					
					}
				}
		}else if(value instanceof ParameterValue){
			ParameterValue parameterValue=(ParameterValue)value;
			if(forDSL){
				String variableLabel=parameterValue.getVariableLabel();
				Variable var=getVariableByLabel(resLibraries.getVariableCategories(), VariableCategory.PARAM_CATEGORY, variableLabel,namedMap);
				parameterValue.setVariableName(var.getName());				
			}else{
				String variableName=parameterValue.getVariableName();
				Variable var=getVariableByName(resLibraries.getVariableCategories(), VariableCategory.PARAM_CATEGORY, variableName,namedMap);
				parameterValue.setVariableLabel(var.getLabel());				
			}
		}else if(value instanceof NamedReferenceValue){
			NamedReferenceValue refValue=(NamedReferenceValue)value;
			String propertyLabel=refValue.getPropertyLabel();
			String propertyName=refValue.getPropertyName();
			String refName=refValue.getReferenceName();
			String variableCategory=namedMap.get(refName);
			if(variableCategory==null){
				refName=refName.substring(1,refName.length());
				variableCategory=namedMap.get(refName);
			}
			if(variableCategory==null){
				throw new RuleException("Reference ["+refName+"] not define.");
			}
			if(forDSL){
				Variable var=getVariableByLabel(resLibraries.getVariableCategories(), variableCategory, propertyLabel,namedMap);
				refValue.setPropertyName(var.getName());
				refValue.setDatatype(var.getType());				
			}else{
				Variable var=getVariableByName(resLibraries.getVariableCategories(), variableCategory, propertyName,namedMap);
				refValue.setPropertyLabel(var.getLabel());
				refValue.setDatatype(var.getType());				
			}
		}else if(value instanceof CommonFunctionValue){
			CommonFunctionValue cfv=(CommonFunctionValue)value;
			CommonFunctionParameter parameter=cfv.getParameter();
			rebuildCommonFunctionParameter(parameter,resLibraries,namedMap,forDSL);
		}else if(value instanceof MethodValue){
			MethodValue methodValue=(MethodValue)value;
			String beanLabel=methodValue.getBeanLabel();
			String methodLabel=methodValue.getMethodLabel();
			List<ActionLibrary> actionLibraries=resLibraries.getActionLibraries();
			SpringBean targetBean=null;
			for(ActionLibrary al:actionLibraries){
				List<SpringBean> beans=al.getSpringBeans();
				for(SpringBean bean:beans){
					if(beanLabel.equals(bean.getName())){
						methodValue.setBeanId(bean.getId());
						targetBean=bean;
						break;
					}
				}
				if(targetBean!=null)break;
			}
			if(targetBean==null){
				throw new RuleException("Bean["+beanLabel+"] not exist.");
			}
			Method targetMethod=null;
			for(Method method:targetBean.getMethods()){
				if(methodLabel.equals(method.getName())){
					targetMethod=method;
					methodValue.setMethodName(method.getMethodName());
					break;
				}
			}
			if(targetMethod==null){
				throw new RuleException("Bean["+beanLabel+"] method["+targetMethod+"] not exist.");
			}
			List<Parameter> parameters=methodValue.getParameters();
			rebuildParameters(resLibraries, parameters,targetMethod.getParameters(),namedMap,forDSL);
		}
		ComplexArithmetic complexArithmetic=value.getArithmetic();
		if(complexArithmetic==null){
			return;
		}
		Value subValue=complexArithmetic.getValue();
		rebuildValue(subValue, resLibraries,namedMap,forDSL);
	}
	
	private com.bstek.urule.model.library.constant.Constant getConstantByName(List<ConstantCategory> constantCategories,String category,String name){
		for(ConstantCategory c:constantCategories){
			if(!c.getLabel().equals(category)){
				continue;
			}
			for(com.bstek.urule.model.library.constant.Constant constant:c.getConstants()){
				if(constant.getName().equals(name)){
					return constant;
				}
			}
		}
		throw new RuleException("Constant ["+category+"."+name+"] was not found.");
	}
	private com.bstek.urule.model.library.constant.Constant getConstantByLabel(List<ConstantCategory> constantCategories,String category,String label){
		for(ConstantCategory c:constantCategories){
			if(!c.getLabel().equals(category)){
				continue;
			}
			for(com.bstek.urule.model.library.constant.Constant constant:c.getConstants()){
				if(constant.getLabel().equals(label)){
					return constant;
				}
			}
		}
		throw new RuleException("Constant ["+category+"."+label+"] was not found.");
	}
	
	public Variable getVariableByName(List<VariableCategory> variableCategories,String category,String name,Map<String,String> namedMap){
		if(namedMap!=null){			
			if(namedMap.containsKey(category)){
				category=namedMap.get(category);
			}
		}
		for(VariableCategory c:variableCategories){
			if(!c.getName().equals(category)){
				continue;
			}
			for(Variable var:c.getVariables()){
				if(var.getName().equals(name)){
					return var;
				}
			}
		}
		throw new RuleException("Variable ["+category+"."+name+"] was not found.");
	}
	public Variable getVariableByLabel(List<VariableCategory> variableCategories,String category,String label,Map<String,String> namedMap){
		if(namedMap!=null){			
			if(namedMap.containsKey(category)){
				category=namedMap.get(category);
			}
		}
		for(VariableCategory c:variableCategories){
			if(!c.getName().equals(category)){
				continue;
			}
			for(Variable var:c.getVariables()){
				if(var.getLabel().equals(label)){
					return var;
				}
			}
		}
		throw new RuleException("Variable ["+category+"."+label+"] was not found.");
	}
	public void setResourceLibraryBuilder(ResourceLibraryBuilder resourceLibraryBuilder) {
		this.resourceLibraryBuilder = resourceLibraryBuilder;
	}
	public ResourceLibraryBuilder getResourceLibraryBuilder() {
		return resourceLibraryBuilder;
	}
}
