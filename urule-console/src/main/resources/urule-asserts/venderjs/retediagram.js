/**
 * @author Jacky.gao
 */
var connections = [];
var nodes=[];
var r=null;
function _dragger() {
	this.ox = this.attr("x");
	this.oy = this.attr("y");
};
function _move(dx, dy) {
	var att = {x: this.ox + dx, y: this.oy + dy};
	this.attr(att);
	if(this.type == "rect"){
		var w=this.attr("width");
		var h=this.attr("height");
		att = {x: this.ox + dx + w/2, y: this.oy + dy + h/2};
		this.text.attr(att);
	}else{
		var w=this.rect.attr("width");
		var h=this.rect.attr("height");
		att = {x: this.ox + dx - w/2, y: this.oy + dy -h/2};
		this.rect.attr(att);		
	}
	for (var i = connections.length; i--;) {
		r.connection(connections[i]);
	}
	r.safari();
};

Raphael.fn.connection = function (obj1, obj2) {
	var line=null;
    if (obj1.line && obj1.from && obj1.to) {
        line = obj1;
        obj1 = line.from;
        obj2 = line.to;
    }
    var bb1 = obj1.getBBox(),bb2 = obj2.getBBox();
    var x1=bb1.x+bb1.width/2;
    var y1=bb1.y+bb1.height/2;
    var x2=bb2.x+bb2.width/2;
    var y2=bb2.y+bb2.height/2;
    var newPath=["M"];
    var path="M "+x1+","+y1+" L "+x2+","+y2;
    var objPath=this.elementPath(obj1);
    var dot=Raphael.pathIntersection(path,objPath);
    newPath.push(dot[0].x);
    newPath.push(dot[0].y);
    objPath=this.elementPath(obj2);
    dot=Raphael.pathIntersection(path,objPath);
    newPath.push(dot[0].x);
    newPath.push(dot[0].y);
    if (line && line.line) {
        line.bg && line.bg.attr({path: newPath});
        line.line.attr({path: newPath});
    } else {
    	var newLine= this.path(newPath).attr({stroke: "#2B547E", fill: "none","arrow-end":"block-wide-long","stroke-width":1.5});
    	newLine.toBack();
    	return {
    		line:newLine,
    		from: obj1,
    		to: obj2
    	};
    }
};
Raphael.fn.elementPath = function(element) {
	var p1 = element.attr("x");
	var p2 = element.attr("y");
	var p3 = element.attr("x")+element.attr("width");
	var p4 = element.attr("y");
	var p5 = element.attr("x")+element.attr("width");
	var p6 = element.attr("y")+element.attr("height");
	var p7 = element.attr("x");
	var p8 = element.attr("y")+element.attr("height");
	return "M "+p1+" "+p2+" L "+p3+" "+p4+" L "+p5+" "+p6+" L "+p7+"  "+p8+" L "+p1+"  "+p2+"";
};

var ReteNode=function(node,r){
	this.id=node["id"];
	var x=node["x"];
	var y=node["y"];
	var w=node["width"];
	var h=node["height"];
	var corner=node["roundCorner"];
	this.rect=r.rect(x, y, w, h, corner);
	var label=node["label"];
	var title=node["title"];
	var text=r.text(x+w/2,y+h/2,label);
	text.drag(_move, _dragger);
	text.attr({fill: "blue", "title": title,cursor: "move"});
	this.rect.text=text;
	text.rect=this.rect;
	var color = node["color"];
	this.rect.attr({fill: color, stroke: color, "stroke-width": 2, "title": title, cursor: "move"});
	this.rect.drag(_move, _dragger);
	nodes.push(this);
	var children=node["children"];
	if(!children){
		return;
	}
	for(var i=0;i<children.length;i++){
		var childNode=children[i];
		new ReteNode(childNode,r);
	}
};
ReteNode.prototype.getRect=function(){
	return this.rect;
};
function drawReteDiagram(container,files){
	var url = window._server+'/retediagram/loadReteDiagramData?_r='+Math.random();
	$.ajax({
		url,
		type : "POST",
		data : {files},
		error : function(req, error) {
			alert("RETE树加载失败!");
		},
		success : function(data) {
			var width=data["width"];
			var height=data["height"];
			var winWidth=$(window).height();
			var winHeight=$(window).width();
			if(winWidth>width){
				width=winWidth;
			}
			if(winHeight>height){
				height=winHeight;
			}
			r = Raphael(container, width, height);
			new ReteNode(data["rootNode"],r);
			var edges=data["edges"];
			if(!edges){
				return;
			}
			for(var i=0;i<edges.length;i++){
				var edge=edges[i];
				var fromId=edge["from"];
				var toId=edge["to"];
				var fromNode=_findNode(fromId);
				var toNode=_findNode(toId);
				connections.push(r.connection(fromNode.rect,toNode.rect));
			}
		}
	});
};
function _findNode(id){
	for(var i=0;i<nodes.length;i++){
		var node=nodes[i];
		if(node.id==id){
			return node;
		}
	}
	throw "Node ["+id+"] not found.";
};
