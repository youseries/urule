/**
 * Created by Jacky.gao on 2016/3/28.
 */
import Connection from './Connection.js';
export default class BaseNode{
    constructor(context,parentNode){
        this.context=context;
        this.parentNode=parentNode;
        this.children=[];
    }
    initBindResizeEvent(){
        var self=this;
        this.nodeContainer.bind("DOMSubtreeModified",function(){
            self.resizeNode();
        });
    }
    initChildrenNodeData(data){
        var childrenNodes=[];
        var conditionTreeNodes=data["conditionTreeNodes"];
        if(conditionTreeNodes){
            childrenNodes=conditionTreeNodes;
        }
        var variableTreeNodes=data["variableTreeNodes"];
        if(variableTreeNodes){
            childrenNodes=childrenNodes.concat(variableTreeNodes);
        }
        var actionTreeNodes=data["actionTreeNodes"];
        if(actionTreeNodes){
            childrenNodes=childrenNodes.concat(actionTreeNodes);
        }
        if(!childrenNodes || childrenNodes.length===0){
            return;
        }
        for(var i=0;i<childrenNodes.length;i++){
            var childNodeData=childrenNodes[i];
            var newNode=this.addChild(childNodeData["nodeType"]);
            newNode.initData(childNodeData);
        }
    }
    delete(){
        while(this.children.length>0){
            var connection=this.children[this.children.length-1];
            connection.node.delete();
        }
        var nodeHeight=0;
        if(this.children.length>1){
            for(var i=0;i<this.children.length;i++){
                var node=this.children[i].node.nodeContainer;
                nodeHeight+=node.height()+15;
            }
        }else{
            nodeHeight=this.nodeContainer.height()+15;
        }
        this.nodeContainer.remove();
        this.connection.path.remove();
        var parentChildren=this.parentNode.children;
        var pos=parentChildren.indexOf(this.connection);
        parentChildren.splice(pos,1);
        this.parentNode.resetItemPosition(pos,-nodeHeight);
        this.parentNode.resetParentNodePosition();
        this.resetCanvasSize();
        window._setDirty();
    }
    addChild(nodeType){
        var parentLeft=parseInt(this.nodeContainer.css("left"))+3;
        var parentTop=parseInt(this.nodeContainer.css("top"))+3;
        var conn=new Connection(this.context,nodeType,this);
        var childrenCount=this.children.length;
        var w=this.nodeContainer.width(),h=this.nodeContainer.height();
        var startX=parentLeft+w-20;
        if(this.children.length>0){
            var firstConn=this.children[0];
            startX=firstConn.startX;
        }
        var startY=parentTop+h/2;
        var lastNodeYPosition=0;
        var lastNode=this.findLastChildrenNode();
        if(lastNode){
            var lastNodeContainer=lastNode.nodeContainer;
            lastNodeYPosition=parseInt(lastNodeContainer.css("top"))+lastNodeContainer.height()+15;
            startY=lastNode.connection.startX;
        }
        var endX=startX+40;
        if(this.children.length>0){
            var firstConn=this.children[0];
            endX=firstConn.endX;
        }
        var endY=startY;
        if(lastNodeYPosition>0){
            endY=lastNodeYPosition+h/2+3;
        }
        conn.drawPath(startX,startY,endX,endY);
        this.children.push(conn);
        if(childrenCount>0){
            if(this.parentNode){
                var newNodeHeight=conn.node.nodeContainer.height()+15;
                var parentChildren=this.parentNode.children;
                var pos=parentChildren.indexOf(this.connection);
                this.parentNode.resetItemPosition(pos+1,newNodeHeight);
            }
            this.resetParentNodePosition();
        }
        if(this.context.lastLeftNode){
            var left=parseInt(conn.node.nodeContainer.css("left"));
            var oldLeft=parseInt(this.context.lastLeftNode.nodeContainer.css("left"));
            if(left>oldLeft){
                this.context.lastLeftNode=conn.node;
            }
        }else{
            this.context.lastLeftNode=conn.node;
        }
        if(this.context.lastBottomNode){
            var top=parseInt(conn.node.nodeContainer.css("top"));
            var oldTop=parseInt(this.context.lastBottomNode.nodeContainer.css("top"));
            if(top>oldTop){
                this.context.lastBottomNode=conn.node;
            }
        }else{
            this.context.lastBottomNode=conn.node;
        }

        this.resetCanvasSize();
        window._setDirty();
        return conn.node;
    }
    resizeNode(){
        if(!this.nodeHeight){
            return;
        }
        var nodeHeight=this.nodeContainer.height()+15;
        if(this.parentNode && nodeHeight!==this.nodeHeight){
            var difHeight=nodeHeight-this.nodeHeight;
            var pos=this.parentNode.children.indexOf(this.connection);
            this.parentNode.resetItemPosition(pos+1,difHeight);
            this.resetParentNodePosition();
            this.nodeHeight=nodeHeight;
        }
        var nodeWidth=this.nodeContainer.width();
        if(nodeWidth!==this.nodeWidth){
            var difWidth=nodeWidth-this.nodeWidth;
            this.resetItemWidth(difWidth);
            this.nodeWidth=nodeWidth;
        }
        this.resetCanvasSize();
    }
    resetCanvasSize(){
        if(this.context.lastLeftNode){
            var left=parseInt(this.context.lastLeftNode.nodeContainer.css("left"));
            this.context.container.css("width",left+this.context.lastLeftNode.nodeContainer.width()+30);
        }
        if(this.context.lastBottomNode){
            var top=parseInt(this.context.lastBottomNode.nodeContainer.css("top"));
            this.context.container.css("height",top+this.context.lastBottomNode.nodeContainer.height()+30);
        }
    }
    resetItemWidth(difWidth){
        for(var i=0;i<this.children.length;i++){
            var connection=this.children[i];
            connection.startX=connection.startX+difWidth;
            connection.endX=connection.endX+difWidth;
            connection.updatePath();
            var node=connection.node;
            var left=parseInt(node.nodeContainer.css("left"))+difWidth;
            node.nodeContainer.css("left",left);
            node.resetItemWidth(difWidth);
        }
    }
    resetItemPosition(index,nodeHeight){
        if(index==-1){
            return;
        }
        for(var i=index;i<this.children.length;i++){
            var connection=this.children[i];
            connection.endY=connection.endY+nodeHeight;
            if(index==0){
                connection.startY=connection.startY+nodeHeight;
            }
            connection.update(nodeHeight);
        }
        if(index>0 && this.parentNode){
            var parentChildren=this.parentNode.children;
            var pos=parentChildren.indexOf(this.connection);
            this.parentNode.resetItemPosition(pos+1,nodeHeight);
        }
    }
    resetParentNodePosition(){
        var newTop;
        if(this.children.length>1){
            var firstNodeContainer=this.children[0].node.nodeContainer;
            var lastNodeContainer=this.children[this.children.length-1].node.nodeContainer;
            var firstNodeTop=parseInt(firstNodeContainer.css("top"));
            var lastNodeTop=parseInt(lastNodeContainer.css("top"))+lastNodeContainer.height();
            var dif=lastNodeTop-firstNodeTop;
            newTop=parseInt(firstNodeContainer.css("top"))+dif/2-this.nodeContainer.height()/2;
        }else if(this.children.length===1){
            var firstNodeContainer=this.children[0].node.nodeContainer;
            newTop=parseInt(firstNodeContainer.css("top"));
        }
        if(newTop){
            this.nodeContainer.css("top",newTop-2);
            if(this.connection){
                this.connection.endY=newTop+this.nodeContainer.height()/2;
                this.connection.updatePath();
            }
            if(this.children.length===1){
                newTop=this.children[0].endY;
            }else{
                newTop+=this.nodeContainer.height()/2;
            }
            for(var i=0;i<this.children.length;i++){
                var connection=this.children[i];
                connection.startY=newTop;
                connection.updatePath();
            }
        }
        if(this.parentNode){
            this.parentNode.resetParentNodePosition();
        }
    }
    findLastChildrenNode(){
        if(this.children.length===0){
            return null;
        }
        var lastNode=this.children[this.children.length-1].node;
        var nextLastNode=lastNode.findLastChildrenNode();
        if(nextLastNode){
            return nextLastNode;
        }
        return lastNode;
    }
};