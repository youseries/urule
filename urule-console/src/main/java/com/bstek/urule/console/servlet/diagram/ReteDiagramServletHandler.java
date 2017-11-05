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
package com.bstek.urule.console.servlet.diagram;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import com.bstek.urule.Utils;
import com.bstek.urule.builder.KnowledgeBase;
import com.bstek.urule.builder.KnowledgeBuilder;
import com.bstek.urule.builder.ResourceBase;
import com.bstek.urule.console.servlet.RenderPageServletHandler;
import com.bstek.urule.console.servlet.respackage.HttpSessionKnowledgeCache;
import com.bstek.urule.console.servlet.respackage.PackageServletHandler;
import com.bstek.urule.model.Node;
import com.bstek.urule.model.rete.AndNode;
import com.bstek.urule.model.rete.CriteriaNode;
import com.bstek.urule.model.rete.Line;
import com.bstek.urule.model.rete.NamedCriteriaNode;
import com.bstek.urule.model.rete.ObjectTypeNode;
import com.bstek.urule.model.rete.OrNode;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rete.TerminalNode;

/**
 * @author Jacky.gao
 * @since 2016年6月23日
 */
public class ReteDiagramServletHandler extends RenderPageServletHandler {
	private KnowledgeBuilder knowledgeBuilder;
	private ReteNodeLayout nodeLayout;
	private final int nodeWidth=30;
	private final int nodeHeight=30;
	private HttpSessionKnowledgeCache httpSessionKnowledgeCache;
	public ReteDiagramServletHandler() {
		nodeLayout=new ReteNodeLayout();
	}
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method=retriveMethod(req);
		if(method!=null){
			invokeMethod(method, req, resp);
		}else{
			VelocityContext context = new VelocityContext();
			context.put("contextPath", req.getContextPath());
			context.put("files", req.getParameter("files"));
			resp.setContentType("text/html");
			resp.setCharacterEncoding("utf-8");
			Template template=ve.getTemplate("html/rete-diagram.html","utf-8");
			PrintWriter writer=resp.getWriter();
			template.merge(context, writer);
			writer.close();
		}
	}
	public void loadReteDiagramData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String files=req.getParameter("files");
		files=Utils.decodeURL(files);
		KnowledgeBase knowledgeBase=(KnowledgeBase)httpSessionKnowledgeCache.get(req,PackageServletHandler.KB_KEY);
		if(knowledgeBase==null){
			ResourceBase resourceBase=knowledgeBuilder.newResourceBase();
			String[] paths=files.split(";");
			for(String path:paths){
				String[] subpaths=path.split(",");
				path=subpaths[0];
				String version=subpaths[1];
				path=Utils.toUTF8(path);
				resourceBase.addResource(path,version);
			}
			knowledgeBase=knowledgeBuilder.buildKnowledgeBase(resourceBase);
			httpSessionKnowledgeCache.put(req,PackageServletHandler.KB_KEY, knowledgeBase);
		}
		Rete rete=knowledgeBase.getRete();
		Diagram diagram=buildReteDiagram(rete);
		writeObjectToJson(resp,diagram);
	}

	private Diagram buildReteDiagram(Rete rete){
		Map<Node,NodeInfo> nodeMap=new HashMap<Node,NodeInfo>();
		List<Edge> edges=new ArrayList<Edge>();
		NodeInfo root=new NodeInfo();
		DiagramContext context=new DiagramContext(edges,nodeMap);
		root.setId(context.nextId());
		root.setLabel("Enter");
		root.setColor("#98AFC7");
		root.setWidth(nodeWidth);
		root.setHeight(nodeHeight);
		root.setRoundCorner(10);
		List<ObjectTypeNode> typeNodes=rete.getObjectTypeNodes();
		int level=1;
		for(ObjectTypeNode typeNode:typeNodes){
			NodeInfo node=new NodeInfo();
			node.setId(context.nextId());
			node.setLabel("T");
			node.setTitle(typeNode.getObjectTypeClass());
			node.setColor("#97CBFF");
			node.setLevel(level);
			node.setWidth(nodeWidth);
			node.setHeight(nodeHeight);
			node.setRoundCorner(5);
			root.addChild(node);
			List<Line> lines=typeNode.getLines();
			if(lines==null){
				continue;
			}
			int nextLevel=level+1;
			for(Line line:lines){
				Edge edge=new Edge(root.getId(),node.getId());
				edges.add(edge);
				buildLine(line,context,node,nextLevel);
			}
		}
		Box box=nodeLayout.layout(root);
		Diagram diagram = new Diagram(edges,root);
		if(box!=null){
			diagram.setWidth(box.getWidth()+500);
			diagram.setHeight(box.getHeight()+300);
		}
		return diagram;
	}

	private void buildLine(Line line,DiagramContext context,NodeInfo parentNode,int level){
		Node toNode=line.getTo();
		if(toNode==null){
			return;
		}
		Map<Node,NodeInfo> nodeMap=context.getNodeMap();
		NodeInfo newNodeInfo=null;
		if(nodeMap.containsKey(toNode)){
			newNodeInfo=nodeMap.get(toNode);
			context.addEdge(new Edge(parentNode.getId(),newNodeInfo.getId()));
			return;
		}
		List<Line> lines=null;
		newNodeInfo=new NodeInfo();
		newNodeInfo.setLevel(level);
		newNodeInfo.setId(context.nextId());
		newNodeInfo.setWidth(nodeWidth);
		newNodeInfo.setHeight(nodeHeight);
		if(toNode instanceof CriteriaNode){
			CriteriaNode cnode=(CriteriaNode)toNode;
			newNodeInfo.setColor("#B3D9D9");
			newNodeInfo.setLabel("C");
			newNodeInfo.setTitle(cnode.getCriteriaInfo());
			newNodeInfo.setRoundCorner(nodeHeight);
			lines=cnode.getLines();
		}else if(toNode instanceof NamedCriteriaNode){
			NamedCriteriaNode node=(NamedCriteriaNode)toNode;
			newNodeInfo.setColor("#B3E9D9");
			newNodeInfo.setLabel("C");
			newNodeInfo.setTitle(node.getCriteriaInfo());
			newNodeInfo.setRoundCorner(nodeHeight);
			lines=node.getLines();
		}else if(toNode instanceof AndNode){
			AndNode andNode=(AndNode)toNode;
			lines=andNode.getLines();
			newNodeInfo.setColor("#DAB1D5");
			newNodeInfo.setLabel("AND");
			newNodeInfo.setRoundCorner(nodeHeight/2);
		}else if(toNode instanceof OrNode){
			OrNode orNode=(OrNode)toNode;
			lines=orNode.getLines();
			newNodeInfo.setColor("#82D900");
			newNodeInfo.setLabel("OR");
			newNodeInfo.setRoundCorner(nodeHeight/2);
		}else if(toNode instanceof TerminalNode){
			TerminalNode terminalNode=(TerminalNode)toNode;
			newNodeInfo.setColor("orange");
			newNodeInfo.setLabel(terminalNode.getRule().getName());
			newNodeInfo.setTitle(terminalNode.getRule().getName());
			newNodeInfo.setRoundCorner(0);
		}
		nodeMap.put(toNode, newNodeInfo);
		parentNode.addChild(newNodeInfo);
		context.addEdge(new Edge(parentNode.getId(),newNodeInfo.getId()));
		if(lines==null){
			return;
		}
		int nextLevel=level+1;
		for(Line nextLine:lines){
			buildLine(nextLine, context, newNodeInfo,nextLevel);
		}
	}
	public void setKnowledgeBuilder(KnowledgeBuilder knowledgeBuilder) {
		this.knowledgeBuilder = knowledgeBuilder;
	}
	
	public void setHttpSessionKnowledgeCache(
			HttpSessionKnowledgeCache httpSessionKnowledgeCache) {
		this.httpSessionKnowledgeCache = httpSessionKnowledgeCache;
	}

	@Override
	public String url() {
		return "/retediagram";
	}
}
