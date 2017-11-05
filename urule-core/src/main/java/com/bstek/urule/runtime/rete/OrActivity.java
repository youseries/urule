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
package com.bstek.urule.runtime.rete;

import java.util.List;
import java.util.Map;

/**
 * @author Jacky.gao
 * @since 2015年1月8日
 */
public class OrActivity extends JoinActivity {
	private boolean pass;
	public List<FactTracker> enter(EvaluationContext context, Object obj,FactTracker tracker,Map<String,Object> variableMap) {
		if(pass && variableMap.size()==0){
			return null;
		}
		context.setPrevActivity(this);
		pass=true;
		return visitPahs(context,obj,tracker,variableMap);
	}
	@Override
	public boolean orNodeIsPassed() {
		return pass;
	}
	@Override
	public void reset(){
		pass=false;
	}
}
