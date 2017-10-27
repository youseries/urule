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
package com.bstek.urule.console.servlet.diagram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jacky.gao
 * @since 2015年1月29日
 */
public class ReteNodeLayout {
	private final int leftMargin=50;
	private final int topMargin=50;
	private final int nodeWidth=30;
	private final int nodeHeight=30;
	public Box layout(NodeInfo rootNode){
		List<NodeInfo> children=rootNode.getChildren();
		if(children==null){
			return null;
		}
		Map<Integer,Integer> levelXPositionMap=new HashMap<Integer,Integer>();
		Map<Integer,List<NodeInfo>> nodeLevelMap=new HashMap<Integer,List<NodeInfo>>();
		buildNodesLevelMap(children, nodeLevelMap);
		int maxSize = buildMaxSize(nodeLevelMap)-1;
		int maxWidth=maxSize*nodeWidth+maxSize*leftMargin;
		int x=maxWidth/2+leftMargin+nodeWidth;
		rootNode.setX(x);
		rootNode.setY(5);
		buildChildrenNodeLocation(children,rootNode,nodeLevelMap,levelXPositionMap);
		Box box=new Box();
		box.setWidth(maxWidth+leftMargin*2+nodeWidth);
		int maxHeight=nodeLevelMap.size()*nodeHeight*3+topMargin*2;
		box.setHeight(maxHeight);
		return box;
	}
	private void buildChildrenNodeLocation(List<NodeInfo> nodes,NodeInfo rootNode,Map<Integer,List<NodeInfo>> nodeLevelMap,Map<Integer,Integer> levelXPositionMap){
		for(int i=0;i<nodes.size();i++){
			NodeInfo node=nodes.get(i);
			int level=node.getLevel();
			node.setY(level*topMargin+level*nodeHeight);
			List<NodeInfo> children=node.getChildren();
			int xposition=0;
			if(levelXPositionMap.containsKey(level)){
				xposition=levelXPositionMap.get(level);
			}
			int rootX=rootNode.getX();
			int levelSize=nodeLevelMap.get(level).size();
			if(xposition==0){
				if(levelSize>1){
					int levelWidth=levelSize*nodeWidth+levelSize*leftMargin;
					xposition=rootX-levelWidth/2-leftMargin;					
				}else{
					xposition=rootX;
				}
			}
			int x=leftMargin+nodeWidth+xposition;
			if(levelSize==1){
				x=xposition;
			}
			node.setX(x);
			levelXPositionMap.put(level, x);
			if(children!=null){
				buildChildrenNodeLocation(children,rootNode,nodeLevelMap,levelXPositionMap);
			}
		}
	}
	private int buildMaxSize(Map<Integer, List<NodeInfo>> nodeLevelMap) {
		int maxSize=1;
		for(List<NodeInfo> list:nodeLevelMap.values()){
			if(list.size()>maxSize){
				maxSize=list.size();
			}
		}
		return maxSize;
	}
	private void buildNodesLevelMap(List<NodeInfo> nodes,Map<Integer,List<NodeInfo>> nodeLevelMap){
		for(NodeInfo node:nodes){
			int level=node.getLevel();
			if(nodeLevelMap.containsKey(level)){
				List<NodeInfo> levelNodes=nodeLevelMap.get(level);
				levelNodes.add(node);
			}else{
				List<NodeInfo> levelNodes=new ArrayList<NodeInfo>();
				levelNodes.add(node);
				nodeLevelMap.put(level, levelNodes);
			}
			List<NodeInfo> children=node.getChildren();
			if(children==null){
				continue;
			}
			buildNodesLevelMap(children, nodeLevelMap);
		}
	}
}
