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
package com.bstek.urule.console.repository.permission;

import java.util.List;

import com.bstek.urule.RuleException;
import com.bstek.urule.console.EnvironmentUtils;
import com.bstek.urule.console.User;
import com.bstek.urule.console.repository.RepositoryService;
import com.bstek.urule.console.repository.model.FileType;
import com.bstek.urule.console.servlet.RequestHolder;
import com.bstek.urule.console.servlet.permission.ProjectConfig;
import com.bstek.urule.console.servlet.permission.UserPermission;

/**
 * @author Jacky.gao
 * @since 2016年9月1日
 */
public class PermissionServiceImpl implements PermissionStore,PermissionService {
	private RepositoryService repositoryService;
	
	@Override
	public boolean projectHasPermission(String path) {
		if(isAdmin()){
			return true;
		}
		path=processPath(path);
		int slashPos=path.indexOf("/");
		if(slashPos==-1){
			slashPos=path.length();
		}
		String project=path.substring(0,slashPos);
		ProjectConfig config=loadProjectPermission(project);
		if(config==null){
			return false;
		}
		return config.isReadProject();
	}
	
	
	@Override
	public boolean projectPackageHasReadPermission(String path) {
		return projectPackagePermission(path, 0);
	}
	@Override
	public boolean projectPackageHasWritePermission(String path) {
		return projectPackagePermission(path, 1);
	}
	
	private boolean projectPackagePermission(String path,int type){
		if(isAdmin()){
			return true;
		}
		path=processPath(path);
		int slashPos=path.indexOf("/");
		if(slashPos==-1){
			slashPos=path.length();
		}
		String project=path.substring(0,slashPos);
		ProjectConfig config=loadProjectPermission(project);
		if(config==null){
			return false;
		}
		if(type==0){
			return config.isReadPackage();
		}else{
			return config.isWritePackage();
		}
	}
	
	@Override
	public boolean fileHasReadPermission(String path) {
		return fileHasPermission(path, 0);
	}
	
	@Override
	public boolean fileHasWritePermission(String path) {
		return fileHasPermission(path, 1);
	}
	
	private boolean fileHasPermission(String path,int permissionType){
		if(isAdmin()){
			return true;
		}
		path=processPath(path);
		int slashPos=path.indexOf("/");
		if(slashPos==-1){
			throw new RuleException("Invalid file ["+path+"] for permission check.");
		}
		String project=path.substring(0,slashPos);
		int pointPos=path.indexOf(".");
		if(pointPos==-1){
			return true;
		}
		ProjectConfig config=loadProjectPermission(project);
		if(config==null){
			return false;
		}
		String extName=path.substring(pointPos+1,path.length());
		FileType type=FileType.parse(extName);
		switch(type){
		case VariableLibrary:
			if(permissionType==0){
				return config.isReadVariableFile();	
			}else{
				return config.isWriteVariableFile();			
			}
		case ActionLibrary:
			if(permissionType==0){
				return config.isReadActionFile();	
			}else{
				return config.isWriteActionFile();			
			}
		case ConstantLibrary:
			if(permissionType==0){
				return config.isReadConstantFile();	
			}else{
				return config.isWriteConstantFile();			
			}
		case DecisionTable:
			if(permissionType==0){
				return config.isReadDecisionTableFile();
			}else{
				return config.isWriteDecisionTableFile();
			}
		case DecisionTree:
			if(permissionType==0){
				return config.isReadDecisionTreeFile();
			}else{
				return config.isWriteDecisionTreeFile();
			}
		case ParameterLibrary:
			if(permissionType==0){
				return config.isReadParameterFile();
			}else{
				return config.isWriteParameterFile();
			}
		case RuleFlow:
			if(permissionType==0){
				return config.isReadFlowFile();
			}else{
				return config.isWriteFlowFile();
			}
		case Ruleset:
			if(permissionType==0){
				return config.isReadRuleFile();
			}else{
				return config.isWriteRuleFile();
			}
		case ScriptDecisionTable:
			if(permissionType==0){
				return config.isReadDecisionTableFile();
			}else{
				return config.isWriteDecisionTableFile();
			}
		case UL:
			if(permissionType==0){
				return config.isReadRuleFile();
			}else{
				return config.isWriteRuleFile();
			}
		case Scorecard:
			if(permissionType==0){
				return config.isReadScorecardFile();
			}else{
				return config.isWriteScorecardFile();
			}
		case DIR:
			return true;
		}
		return false;
	}
	
	private String processPath(String path) {
		if (path.startsWith("/")) {
			return path.substring(1, path.length());
		}
		return path;
	}
	@Override
	public boolean isAdmin(){
		User user=EnvironmentUtils.getLoginUser(RequestHolder.newRequestContext());
		return user.isAdmin();
	}
	private ProjectConfig loadProjectPermission(String project){
		User user=EnvironmentUtils.getLoginUser(RequestHolder.newRequestContext());
		String companyId=user.getCompanyId();
		List<UserPermission> permissions=repositoryService.loadResourceSecurityConfigs(companyId);
		ProjectConfig target=null;
		for(UserPermission p:permissions){
			if(p.getUsername().equals(user.getUsername())){
				for(ProjectConfig pc:p.getProjectConfigs()){
					if(pc.getProject().equals(project)){
						target=pc;
						break;
					}
				}
				break;
			}
		}
		return target;
	}
	@Override
	public void refreshPermissionStore() {
		//do nothing...
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
}
