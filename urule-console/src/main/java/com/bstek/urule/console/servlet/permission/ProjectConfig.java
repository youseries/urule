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
package com.bstek.urule.console.servlet.permission;
/**
 * @author Jacky.gao
 * @since 2016年8月30日
 */
public class ProjectConfig {
	private String project;
	private boolean readProject;
	
	private boolean readPackage;
	private boolean writePackage;
	
	private boolean readVariableFile;
	private boolean writeVariableFile;
	
	private boolean readParameterFile;
	private boolean writeParameterFile;
	
	private boolean readConstantFile;
	private boolean writeConstantFile;
	
	private boolean readActionFile;
	private boolean writeActionFile;
		
	private boolean readRuleFile;
	private boolean writeRuleFile;
	
	private boolean readScorecardFile;
	private boolean writeScorecardFile;
	
	private boolean readDecisionTableFile;
	private boolean writeDecisionTableFile;
		
	private boolean readDecisionTreeFile;
	private boolean writeDecisionTreeFile;
	
	private boolean readFlowFile;
	private boolean writeFlowFile;
	
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	
	public boolean isReadProject() {
		return readProject;
	}
	public void setReadProject(boolean readProject) {
		this.readProject = readProject;
	}
	public boolean isReadPackage() {
		return readPackage;
	}
	public void setReadPackage(boolean readPackage) {
		this.readPackage = readPackage;
	}
	public boolean isWritePackage() {
		return writePackage;
	}
	public void setWritePackage(boolean writePackage) {
		this.writePackage = writePackage;
	}
	public boolean isReadVariableFile() {
		return readVariableFile;
	}
	public void setReadVariableFile(boolean readVariableFile) {
		this.readVariableFile = readVariableFile;
	}
	public boolean isWriteVariableFile() {
		return writeVariableFile;
	}
	public void setWriteVariableFile(boolean writeVariableFile) {
		this.writeVariableFile = writeVariableFile;
	}
	public boolean isReadParameterFile() {
		return readParameterFile;
	}
	public void setReadParameterFile(boolean readParameterFile) {
		this.readParameterFile = readParameterFile;
	}
	public boolean isWriteParameterFile() {
		return writeParameterFile;
	}
	public void setWriteParameterFile(boolean writeParameterFile) {
		this.writeParameterFile = writeParameterFile;
	}
	public boolean isReadConstantFile() {
		return readConstantFile;
	}
	public void setReadConstantFile(boolean readConstantFile) {
		this.readConstantFile = readConstantFile;
	}
	public boolean isWriteConstantFile() {
		return writeConstantFile;
	}
	public void setWriteConstantFile(boolean writeConstantFile) {
		this.writeConstantFile = writeConstantFile;
	}
	public boolean isReadActionFile() {
		return readActionFile;
	}
	public void setReadActionFile(boolean readActionFile) {
		this.readActionFile = readActionFile;
	}
	public boolean isWriteActionFile() {
		return writeActionFile;
	}
	public void setWriteActionFile(boolean writeActionFile) {
		this.writeActionFile = writeActionFile;
	}
	public boolean isReadRuleFile() {
		return readRuleFile;
	}
	public void setReadRuleFile(boolean readRuleFile) {
		this.readRuleFile = readRuleFile;
	}
	public boolean isWriteRuleFile() {
		return writeRuleFile;
	}
	public void setWriteRuleFile(boolean writeRuleFile) {
		this.writeRuleFile = writeRuleFile;
	}
	public boolean isReadDecisionTableFile() {
		return readDecisionTableFile;
	}
	public void setReadDecisionTableFile(boolean readDecisionTableFile) {
		this.readDecisionTableFile = readDecisionTableFile;
	}
	public boolean isWriteDecisionTableFile() {
		return writeDecisionTableFile;
	}
	public void setWriteDecisionTableFile(boolean writeDecisionTableFile) {
		this.writeDecisionTableFile = writeDecisionTableFile;
	}
	public boolean isReadDecisionTreeFile() {
		return readDecisionTreeFile;
	}
	public void setReadDecisionTreeFile(boolean readDecisionTreeFile) {
		this.readDecisionTreeFile = readDecisionTreeFile;
	}
	public boolean isWriteDecisionTreeFile() {
		return writeDecisionTreeFile;
	}
	public void setWriteDecisionTreeFile(boolean writeDecisionTreeFile) {
		this.writeDecisionTreeFile = writeDecisionTreeFile;
	}
	public boolean isReadFlowFile() {
		return readFlowFile;
	}
	public void setReadFlowFile(boolean readFlowFile) {
		this.readFlowFile = readFlowFile;
	}
	public boolean isWriteFlowFile() {
		return writeFlowFile;
	}
	public void setWriteFlowFile(boolean writeFlowFile) {
		this.writeFlowFile = writeFlowFile;
	}
	public boolean isReadScorecardFile() {
		return readScorecardFile;
	}
	public void setReadScorecardFile(boolean readScorecardFile) {
		this.readScorecardFile = readScorecardFile;
	}
	public boolean isWriteScorecardFile() {
		return writeScorecardFile;
	}
	public void setWriteScorecardFile(boolean writeScorecardFile) {
		this.writeScorecardFile = writeScorecardFile;
	}
}
