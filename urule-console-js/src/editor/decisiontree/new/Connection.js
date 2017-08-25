/**
 * Created by Jacky.Gao on 2016/3/23.
 */
import Raphael from 'raphael';

export default class Connection{
    constructor(context,nodeType,parentNode){
        this.context=context;
        this.nodeType=nodeType;
        this.parentNode=parentNode;
    }
    drawPath(startX,startY,endX,endY){
        this.startX=startX;
        this.endX=endX;
        if(this.isJoin){
            this.startY=startY-3;
            this.endY=endY+2;
        }else{
            this.startY=startY-3;
            this.endY=endY-3;
        }
        var color = Raphael.rgb(120, 120, 120);
        this.path=this.context.paper.path(this.buildPathInfo()).attr("stroke", color);
        if(this.nodeType==="condition"){
            this.node=this.context.newConditionNode(this.parentNode);
        }else if(this.nodeType==="action"){
            this.node=this.context.newActionNode(this.parentNode);
        }else if(this.nodeType==="variable"){
            this.node=this.context.newVariableNode(this.parentNode);
        }
        this.node.connection=this;
        this.initNodePosition();
    }
    updatePath(){
        var pathInfo=this.buildPathInfo();
        this.path.attr("path",pathInfo);
    }
    update(nodeHeight){
        this.updatePath();
        var top=parseInt(this.node.nodeContainer.css("top"))+nodeHeight;
        this.node.nodeContainer.css({
            "top":top
        });
        this.node.resetItemPosition(0,nodeHeight);
    }
    initNodePosition(){
        var h=this.node.nodeContainer.height(),w=this.node.nodeContainer.width();
        var left=this.endX,top=this.endY-h/2;
        this.node.nodeContainer.css({
            position:"absolute",
            left:left,
            top:top
        });
    }
    buildPathInfo(){
        return "M" + this.startX + "," + this.startY + " C" + this.startX + "," + this.endY + "," + this.startX + "," + this.endY + "," + this.endX + "," + this.endY;
    }
};

