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
package com.bstek.urule.runtime.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.bstek.urule.RuleException;
import com.bstek.urule.Utils;
import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageWrapper;

/**
 * @author Jacky.gao
 * @since 2015年1月28日
 */
public class RemoteServiceImpl implements RemoteService{
	private String resporityServerUrl;
	private Logger log=Logger.getLogger(RemoteServiceImpl.class.getName());
	public KnowledgePackage getKnowledge(String packageId, String timestamp) {
		if(StringUtils.isEmpty(resporityServerUrl)){
			return null;
		}
		log.info("Load knowledgepackage from remote...");
		String content=sendRequest(packageId, timestamp);
		if(StringUtils.isEmpty(content)){
			return null;
		}
		KnowledgePackageWrapper wrapper=JsonUtils.parseKnowledgePackageWrapper(content);
		KnowledgePackage knowledgePackage=wrapper.getKnowledgePackage();
		Map<String, FlowDefinition> flowMap=knowledgePackage.getFlowMap();
		if(flowMap!=null && flowMap.size()>0){
			for(FlowDefinition fd:flowMap.values()){
				fd.buildConnectionToNode();
			}
		}
		return knowledgePackage;
	}		
	private String sendRequest(String packageId, String timestamp) {
		HttpURLConnection connection=null;
		OutputStreamWriter writer=null;
		InputStream inputStream=null;
		InputStreamReader inputStreamReader=null;
		BufferedReader bufferedReader=null;
		try {
			packageId=Utils.encodeURL(packageId);
			packageId=Utils.encodeURL(packageId);
			String content="packageId="+packageId+"";
			if(StringUtils.isNotEmpty(timestamp)){
				content+="&timestamp="+timestamp+"";
			}
			URL url=new URL(resporityServerUrl);
			connection=(HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept-Charset", "utf-8");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", String.valueOf(content.length()));
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.connect();
			writer=new OutputStreamWriter(connection.getOutputStream());
			writer.write(content);
			writer.flush();
			if (connection.getResponseCode() != 200) {
                throw new RuleException("Server request was failed, Response message : " + connection.getResponseMessage());
            }
			inputStream=connection.getInputStream();
			inputStreamReader=new InputStreamReader(inputStream,"utf-8");
			bufferedReader=new BufferedReader(inputStreamReader);
			String line=null;
			StringBuilder sb=new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
			return sb.toString();
		} catch (Exception e) {
			throw new RuleException(e);
		} finally {
			try {
				if(writer!=null){
					writer.close();
				}
				if(bufferedReader!=null){
					bufferedReader.close();
				}
				if(inputStreamReader!=null){
					inputStreamReader.close();
				}
				if(inputStream!=null){
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(connection!=null){
				connection.disconnect();
			}
		}
	}
	public void setResporityServerUrl(String resporityServerUrl) {
		if(StringUtils.isEmpty(resporityServerUrl) || resporityServerUrl.equals("urule.resporityServerUrl")){
			return;
		}
		if(resporityServerUrl.endsWith("/")){
			resporityServerUrl+="urule/loadknowledge";
		}else{
			resporityServerUrl+="/urule/loadknowledge";
		}
		this.resporityServerUrl = resporityServerUrl;
	}
}
