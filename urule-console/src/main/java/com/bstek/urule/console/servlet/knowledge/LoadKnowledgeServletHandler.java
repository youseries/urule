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
package com.bstek.urule.console.servlet.knowledge;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.bstek.urule.Utils;
import com.bstek.urule.console.servlet.RenderPageServletHandler;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.cache.CacheUtils;
import com.bstek.urule.runtime.service.KnowledgePackageService;

/**
 * @author Jacky.gao
 * @since 2016年8月17日
 */
public class LoadKnowledgeServletHandler  extends RenderPageServletHandler{
	private KnowledgePackageService knowledgePackageService;
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String packageId=req.getParameter("packageId");
		if(StringUtils.isEmpty(packageId)){
			resp.setContentType("text/html");
			PrintWriter pw=resp.getWriter();
			pw.write("<html>");
			pw.write("<header>");
			pw.write("</header>");
			pw.write("<body>");
			pw.write("<h1>packageId can not be null<h1>");
			pw.write("</body>");
			pw.write("</html>");
			pw.flush();
			pw.close();
			return;
		}
		packageId=Utils.decodeURL(packageId);
		String timestamp=req.getParameter("timestamp");
		KnowledgePackage knowledgePackage=CacheUtils.getKnowledgeCache().getKnowledge(packageId);
		if(knowledgePackage==null){
			knowledgePackage=knowledgePackageService.buildKnowledgePackage(packageId);
			CacheUtils.getKnowledgeCache().putKnowledge(packageId, knowledgePackage);
		}
		if(StringUtils.isNotEmpty(timestamp)){
			long remoteTimestamp=Long.valueOf(timestamp);
			long localTimestamp=knowledgePackage.getTimestamp();
			if(localTimestamp>remoteTimestamp){
				writeObjectToJson(resp, new KnowledgePackageWrapper(knowledgePackage));
			}
		}else{
			writeObjectToJson(resp, new KnowledgePackageWrapper(knowledgePackage));
		}
	}
	@Override
	public String url() {
		return "/loadknowledge";
	}
	public void setKnowledgePackageService(KnowledgePackageService knowledgePackageService) {
		this.knowledgePackageService = knowledgePackageService;
	}
}
