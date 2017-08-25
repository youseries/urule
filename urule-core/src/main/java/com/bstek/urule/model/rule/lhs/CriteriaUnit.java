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
package com.bstek.urule.model.rule.lhs;

import java.util.List;

import com.bstek.urule.runtime.rete.EvaluationContext;

/**
 * @author Jacky.gao
 * @since 2016年8月15日
 */
public class CriteriaUnit {
	private Criteria criteria;
	private JunctionType junctionType;
	private List<CriteriaUnit> nextUnits;
	public boolean evaluate(EvaluationContext context,Object obj,List<Object> allMatchedObjects){
		if(criteria==null){
			for(CriteriaUnit nextUnit:nextUnits){
				boolean nextResult=nextUnit.evaluate(context, obj, allMatchedObjects);
				JunctionType junctionType=nextUnit.getJunctionType();
				if(junctionType==null){
					return nextResult;
				}
				if(junctionType.equals(JunctionType.or)){
					if(nextResult){
						return true;
					}
				}else{
					if(!nextResult){
						return false;
					}
				}
			}
			return true;
		}else{
			boolean result=criteria.evaluate(context, obj, allMatchedObjects);
			if(junctionType==null){
				return result;
			}
			if(result){
				if(junctionType.equals(JunctionType.or)){
					return true;
				}
			}else{
				if(junctionType.equals(JunctionType.and)){
					return false;
				}
			}
			if(nextUnits==null){
				return result;
			}
			if(junctionType.equals(JunctionType.and)){
				for(CriteriaUnit nextUnit:nextUnits){
					boolean nextResult=nextUnit.evaluate(context, obj, allMatchedObjects);
					if(!nextResult){
						return false;
					}
					JunctionType type=nextUnit.getJunctionType();
					if(type==null){
						return nextResult;
					}
					if(type.equals(JunctionType.or)){
						if(nextResult){
							return true;
						}
					}else{
						if(!nextResult){
							return false;
						}
					}
				}
				return true;
			}else{
				for(CriteriaUnit nextUnit:nextUnits){
					boolean nextResult=nextUnit.evaluate(context, obj, allMatchedObjects);
					if(nextResult){
						return true;
					}
					JunctionType type=nextUnit.getJunctionType();
					if(type==null){
						return nextResult;
					}
					if(type.equals(JunctionType.or)){
						if(nextResult){
							return true;
						}
					}else{
						if(!nextResult){
							return false;
						}
					}
				}
			}
		}
		return false;
	}
	public Criteria getCriteria() {
		return criteria;
	}
	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}
	public JunctionType getJunctionType() {
		return junctionType;
	}
	public void setJunctionType(JunctionType junctionType) {
		this.junctionType = junctionType;
	}
	public List<CriteriaUnit> getNextUnits() {
		return nextUnits;
	}
	public void setNextUnits(List<CriteriaUnit> nextUnits) {
		this.nextUnits = nextUnits;
	}
	public String getId(){
		StringBuffer sb=new StringBuffer();
		if(criteria!=null){
			sb.append(criteria.getId());
		}
		if(junctionType!=null){
			sb.append("-");
			sb.append(junctionType.name());
		}
		if(nextUnits!=null){
			int i=0;
			for(CriteriaUnit unit:nextUnits){
				if(i>0){
					sb.append(",");
				}
				sb.append(unit.getId());
				i++;
			}
		}
		return sb.toString();
	}
}
