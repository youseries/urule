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
package com.bstek.urule;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.cache.CacheUtils;

/**
 * @author Jacky.gao
 * @since 2016年2月27日
 */
public class KnowledgePackageReceiverServlet extends HttpServlet {
	private static final long serialVersionUID = -4342175088856372588L;
	public static final String URL="/knowledgepackagereceiver";
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String packageId=req.getParameter("packageId");
		if(StringUtils.isEmpty(packageId)){
			return;
		}
		packageId=URLDecoder.decode(packageId, "utf-8");
		if(packageId.startsWith("/")){
			packageId=packageId.substring(1,packageId.length());
		}
		String content=req.getParameter("content");
		if(StringUtils.isEmpty(content)){
			return;
		}
		content=URLDecoder.decode(content, "utf-8");
		ObjectMapper mapper=new ObjectMapper();
		mapper.getDeserializationConfig().withDateFormat(new SimpleDateFormat(Configure.getDateFormat()));
		KnowledgePackageWrapper wrapper=mapper.readValue(content, KnowledgePackageWrapper.class);
		wrapper.buildDeserialize();
		KnowledgePackage knowledgePackage=wrapper.getKnowledgePackage();
		Map<String, FlowDefinition> flowMap=knowledgePackage.getFlowMap();
		if(flowMap!=null && flowMap.size()>0){
			for(FlowDefinition fd:flowMap.values()){
				fd.buildConnectionToNode();
			}
		}
		CacheUtils.getKnowledgeCache().putKnowledge(packageId, knowledgePackage);
		SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("["+sd.format(new Date())+"] "+"Successfully receive the server side to pushed package:"+packageId);
		resp.setContentType("text/plain");
		PrintWriter pw=resp.getWriter();
		pw.write("ok");
		pw.flush();
		pw.close();
	}
}
