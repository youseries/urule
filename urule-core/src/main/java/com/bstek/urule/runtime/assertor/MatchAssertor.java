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
package com.bstek.urule.runtime.assertor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;

/**
 * @author Jacky.gao
 * @since 2015年1月6日
 */
public class MatchAssertor implements Assertor {
	public boolean eval(Object left, Object right,Datatype datatype) {
		if(left==null || right==null){
			return false;
		}
		Pattern pattern=Pattern.compile(right.toString());
		Matcher matcher=pattern.matcher(left.toString());
		return matcher.matches();
	}

	public boolean support(Op op) {
		return op.equals(Op.Match);
	}

}
