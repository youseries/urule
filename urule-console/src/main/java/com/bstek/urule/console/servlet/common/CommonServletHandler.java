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
package com.bstek.urule.console.servlet.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.bstek.urule.RuleException;
import com.bstek.urule.Utils;
import com.bstek.urule.console.EnvironmentUtils;
import com.bstek.urule.console.User;
import com.bstek.urule.console.repository.Repository;
import com.bstek.urule.console.repository.RepositoryResourceProvider;
import com.bstek.urule.console.repository.RepositoryService;
import com.bstek.urule.console.repository.model.FileType;
import com.bstek.urule.console.servlet.RenderPageServletHandler;
import com.bstek.urule.console.servlet.RequestContext;
import com.bstek.urule.dsl.RuleParserLexer;
import com.bstek.urule.dsl.RuleParserParser;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.model.library.action.ActionLibrary;
import com.bstek.urule.model.library.action.SpringBean;
import com.bstek.urule.parse.deserializer.ActionLibraryDeserializer;
import com.bstek.urule.parse.deserializer.ConstantLibraryDeserializer;
import com.bstek.urule.parse.deserializer.DecisionTableDeserializer;
import com.bstek.urule.parse.deserializer.DecisionTreeDeserializer;
import com.bstek.urule.parse.deserializer.Deserializer;
import com.bstek.urule.parse.deserializer.ParameterLibraryDeserializer;
import com.bstek.urule.parse.deserializer.RuleSetDeserializer;
import com.bstek.urule.parse.deserializer.ScorecardDeserializer;
import com.bstek.urule.parse.deserializer.ScriptDecisionTableDeserializer;
import com.bstek.urule.parse.deserializer.VariableLibraryDeserializer;
import com.bstek.urule.runtime.BuiltInActionLibraryBuilder;

/**
 * @author Jacky.gao
 * @since 2016年7月25日
 */
public class CommonServletHandler extends RenderPageServletHandler{
	private RepositoryService repositoryService;
	private BuiltInActionLibraryBuilder builtInActionLibraryBuilder;
	private List<Deserializer<?>> deserializers=new ArrayList<Deserializer<?>>();
	private List<FunctionDescriptor> functionDescriptors=new ArrayList<FunctionDescriptor>();
	
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method=retriveMethod(req);
		if(method!=null){
			invokeMethod(method, req, resp);
		}else{
			throw new ServletException("Unsupport this operation.");
		}
	}
	public void saveFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String file=req.getParameter("file");
		String content=req.getParameter("content");
		content=Utils.decodeContent(content);
		String versionComment=req.getParameter("versionComment");
		Boolean newVersion = Boolean.valueOf(req.getParameter("newVersion"));
		User user=EnvironmentUtils.getLoginUser(new RequestContext(req, resp));
		try{
			repositoryService.saveFile(file,content,newVersion,versionComment,user);			
		}catch(Exception ex){
			throw new RuleException(ex);
		}
	}
	public void loadReferenceFiles(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path=req.getParameter("path");
		path=Utils.decodeURL(path);
		String searchText=buildSearchText(path,req,false);
		try{
			List<String> files=repositoryService.getReferenceFiles(path,searchText);
			searchText=buildSearchText(path,req,true);
			List<String> scriptFiles=repositoryService.getReferenceFiles(path,searchText);
			if(scriptFiles.size()>0){
				files.addAll(scriptFiles);
			}
			List<RefFile> refFiles=new ArrayList<RefFile>();
			for(String file:files){
				RefFile ref=new RefFile();
				refFiles.add(ref);
				ref.setPath(file);
				if(file.endsWith(FileType.Ruleset.toString())){
					ref.setEditor("/ruleseteditor");
					ref.setType("决策集");
				}else if(file.endsWith(FileType.UL.toString())){
					ref.setEditor("/uleditor");
					ref.setType("脚本决策集");
				}else if(file.endsWith(FileType.DecisionTable.toString())){
					ref.setEditor("/decisiontableeditor");
					ref.setType("决策表");
				}else if(file.endsWith(FileType.ScriptDecisionTable.toString())){
					ref.setEditor("/scriptdecisiontableeditor");
					ref.setType("脚本决策表");
				}else if(file.endsWith(FileType.DecisionTree.toString())){
					ref.setEditor("/decisiontreeeditor");			
					ref.setType("决策树");
				}else if(file.endsWith(FileType.RuleFlow.toString())){
					ref.setEditor("/ruleflowdesigner");			
					ref.setType("决策流");
				}
				int pos=file.lastIndexOf("/");
				String name=file;
				if(pos>-1){
					name=file.substring(pos+1,file.length());
				}
				ref.setName(name);
			}
			writeObjectToJson(resp, refFiles);
		}catch(Exception ex){
			throw new RuleException(ex);
		}
	}
	
	private String buildSearchText(String path,HttpServletRequest req,boolean isScript){
		StringBuilder sb=new StringBuilder();
		if(path.endsWith(FileType.ActionLibrary.toString())){
			if(isScript){
				sb.append(req.getParameter("beanLabel"));
				sb.append(".");
				sb.append(req.getParameter("methodLabel"));
			}else{				
				sb.append("bean=\""+req.getParameter("beanName")+"\"");
				sb.append(" bean-label=\""+req.getParameter("beanLabel")+"\"");
				sb.append(" method-label=\""+req.getParameter("methodLabel")+"\"");
				sb.append(" method-name=\""+req.getParameter("methodName")+"\"");
			}
			return sb.toString();
		}else if(path.endsWith(FileType.ConstantLibrary.toString())){
			if(isScript){
				sb.append(req.getParameter("constCategoryLabel"));
				sb.append(".");
				sb.append(req.getParameter("constLabel"));
			}else{
				sb.append("const-category=\""+req.getParameter("constCategoryLabel")+"\"");
				sb.append(" const=\""+req.getParameter("constName")+"\"");
			}
			return sb.toString();
		}else if(path.endsWith(FileType.ParameterLibrary.toString())){
			if(isScript){
				sb.append("参数.");
				sb.append(req.getParameter("varLabel"));
			}else{
				sb.append("var-category=\"参数\"");
				sb.append(" var=\""+req.getParameter("varName")+"\"");
			}
			return sb.toString();
		}else if(path.endsWith(FileType.VariableLibrary.toString())){
			if(isScript){
				sb.append(req.getParameter("varCategory"));
				sb.append(".");
				sb.append(req.getParameter("varLabel"));
			}else{
				sb.append("var-category=\""+req.getParameter("varCategory")+"\"");
				sb.append(" var=\""+req.getParameter("varName")+"\"");
			}
			return sb.toString();
			
		}else{
			throw new RuleException("Unknow file : "+ path);
		}
	}
	
	public void loadResourceTreeData(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String project=req.getParameter("project");
		project=Utils.decodeURL(project); 
		String forLib=req.getParameter("forLib");
		String fileType=req.getParameter("fileType");
		String searchFileName=req.getParameter("searchFileName");
		
		User user=EnvironmentUtils.getLoginUser(new RequestContext(req,resp));
		FileType[] types=null;
		if(StringUtils.isNotBlank(forLib) && forLib.equals("true")){
			types=new FileType[]{FileType.ActionLibrary,FileType.ConstantLibrary,FileType.VariableLibrary,FileType.ParameterLibrary};
		}else if(StringUtils.isNotBlank(fileType)){
			String[] fileTypes=fileType.split(",");
			types=new FileType[fileTypes.length];
			for(int i=0;i<fileTypes.length;i++){
				types[i]=FileType.valueOf(fileTypes[i]);
			}
		}else{
			types=new FileType[]{FileType.UL,FileType.Ruleset,FileType.RuleFlow,FileType.DecisionTable,FileType.ScriptDecisionTable,FileType.DecisionTree,FileType.Scorecard};
		}
		try{
			Repository repo=repositoryService.loadRepository(project,user,false,types,searchFileName);	
			writeObjectToJson(resp, repo.getRootFile());			
		}catch(Exception ex){
			throw new RuleException(ex);
		}
	}
	
	public void loadFunctions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		writeObjectToJson(resp, functionDescriptors);
	}
	
	public void scriptValidation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String content=req.getParameter("content");
		if(StringUtils.isNotBlank(content)){
			ScriptType type=ScriptType.valueOf(req.getParameter("type"));
			ANTLRInputStream antlrInputStream=new ANTLRInputStream(content);
			RuleParserLexer lexer=new RuleParserLexer(antlrInputStream);
			CommonTokenStream steam=new CommonTokenStream(lexer);
			RuleParserParser parser=new RuleParserParser(steam);
			parser.removeErrorListeners();
			ScriptErrorListener errorListener=new ScriptErrorListener();
			parser.addErrorListener(errorListener);
			switch(type){
			case Script:
				parser.ruleSet();
				break;
			case DecisionNode:
				parser.condition();
				break;
			case ScriptNode:
				parser.actions();
				
			}
			List<ErrorInfo> infos=errorListener.getInfos();
			writeObjectToJson(resp, infos);
		}
	}
	
	
	public void loadXml(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Object> result=new ArrayList<Object>();
		String files=req.getParameter("files");
		files=Utils.decodeURL(files);
		boolean isaction=false;
		if(files!=null){
			if(files.startsWith("builtinactions")){
				isaction=true;
			}else{
				String[] paths=files.split(";");
				for(String path:paths){
					if(path.startsWith(RepositoryResourceProvider.JCR)){
						path=path.substring(4,path.length());					
					}
					String[] subpaths=path.split(",");
					path=subpaths[0];
					String version=null;
					if(subpaths.length==2){
						version=subpaths[1];
					}
					try{
						InputStream inputStream=null;
						if(StringUtils.isEmpty(version)){
							inputStream=repositoryService.readFile(path,null);
						}else{
							inputStream=repositoryService.readFile(path,version);			
						}
						Element element=parseXml(inputStream);
						for(Deserializer<?> des:deserializers){
							if(des.support(element)){
								result.add(des.deserialize(element));
								if(des instanceof ActionLibraryDeserializer){
									isaction=true;
								}
								break;
							}
						}
						inputStream.close();
					}catch(Exception ex){
						throw new RuleException(ex);
					}
				}
			}
		}
		if(isaction){
			List<SpringBean> beans=builtInActionLibraryBuilder.getBuiltInActions();
			if(beans.size()>0){
				ActionLibrary al=new ActionLibrary();
				al.setSpringBeans(beans);
				result.add(al);
			}
		}
		writeObjectToJson(resp, result);
	}
	
	protected Element parseXml(InputStream stream){
		SAXReader reader=new SAXReader();
		Document document;
		try {
			document = reader.read(stream);
			Element root=document.getRootElement();
			return root;
		} catch (DocumentException e) {
			throw new RuleException(e);
		}
	}
	
	public void setBuiltInActionLibraryBuilder(BuiltInActionLibraryBuilder builtInActionLibraryBuilder) {
		this.builtInActionLibraryBuilder = builtInActionLibraryBuilder;
	}
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	@Override
	public String url() {
		return "/common";
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		super.setApplicationContext(applicationContext);
		ActionLibraryDeserializer actionLibraryDeserializer=(ActionLibraryDeserializer)applicationContext.getBean(ActionLibraryDeserializer.BEAN_ID);
		VariableLibraryDeserializer variableLibraryDeserializer=(VariableLibraryDeserializer)applicationContext.getBean(VariableLibraryDeserializer.BEAN_ID);
		ConstantLibraryDeserializer constantLibraryDeserializer=(ConstantLibraryDeserializer)applicationContext.getBean(ConstantLibraryDeserializer.BEAN_ID);
		RuleSetDeserializer ruleSetDeserializer=(RuleSetDeserializer)applicationContext.getBean(RuleSetDeserializer.BEAN_ID);
		DecisionTableDeserializer decisionTableDeserializer=(DecisionTableDeserializer)applicationContext.getBean(DecisionTableDeserializer.BEAN_ID);
		ScriptDecisionTableDeserializer scriptDecisionTableDeserializer=(ScriptDecisionTableDeserializer)applicationContext.getBean(ScriptDecisionTableDeserializer.BEAN_ID);
		DecisionTreeDeserializer decisionTreeDeserializer=(DecisionTreeDeserializer)applicationContext.getBean(DecisionTreeDeserializer.BEAN_ID);
		ScorecardDeserializer scorecardDeserializer=(ScorecardDeserializer)applicationContext.getBean(ScorecardDeserializer.BEAN_ID);

		ParameterLibraryDeserializer parameterLibraryDeserializer=(ParameterLibraryDeserializer)applicationContext.getBean(ParameterLibraryDeserializer.BEAN_ID);
		deserializers.add(actionLibraryDeserializer);
		deserializers.add(variableLibraryDeserializer);
		deserializers.add(constantLibraryDeserializer);
		deserializers.add(ruleSetDeserializer);
		deserializers.add(decisionTableDeserializer);
		deserializers.add(scriptDecisionTableDeserializer);
		deserializers.add(decisionTreeDeserializer);
		deserializers.add(parameterLibraryDeserializer);
		deserializers.add(scorecardDeserializer);
		
		Collection<FunctionDescriptor> coll=applicationContext.getBeansOfType(FunctionDescriptor.class).values();
		for(FunctionDescriptor fun:coll){
			if(fun.isDisabled()){
				continue;
			}
			functionDescriptors.add(fun);
		}
	}
}
