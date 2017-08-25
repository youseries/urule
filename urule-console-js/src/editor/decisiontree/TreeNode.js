/**
 * Created by Jacky.gao on 2016/2/22.
 */
TreeNode=function(parentNode){
    this.parentNode=parentNode;
    this.container=$("<table class='nodeTable' border='0' cellpadding='0' cellspacing='0'>");
    var row=$("<tr>");
    this.col=$("<td align='center'>");
    row.append(this.col);
    this.container.append(row);
    this.nextColCount=0;
    this.lineCols=[];
    this.childrenNodes=[];
};

TreeNode.prototype.delete=function(){
    if(!this.parentNode){
        return;
    }
    _setDirty();
    var pos;
    var parentChildrenNodes=this.parentNode.childrenNodes;
    for(var i= 0;i<parentChildrenNodes.length;i++){
        if(parentChildrenNodes[i]===this){
            pos=i;
            break;
        }
    }
    parentChildrenNodes.splice(pos,1);
    this.parentNode.nextColCount-=2;
    this.parentNode.col.prop("colspan",this.parentNode.nextColCount);
    this.parentNode.lineCol.prop("colspan",this.parentNode.nextColCount);
    if(parentChildrenNodes.length>0){
        this.parentCol.remove();
        var parentLineCols=this.parentNode.lineCols;
        var pos;
        for(var j=0;j<this.newLineCols.length;j++){
            for(var i=0;i<parentLineCols.length;i++){
                if(parentLineCols[i]===this.newLineCols[j]){
                    pos=i;
                    break;
                }
            }
            parentLineCols.splice(pos,1);
        }
        if(parentLineCols.length>1){
            parentLineCols[0].css("border-top-style","none");
            parentLineCols[parentLineCols.length-1].css("border-top-style","none");
        }
        this.newLineCols[0].remove();
        this.newLineCols[1].remove();
    }else{
        this.parentNode.lineRow.remove();
        this.parentNode.nextRow.remove();
        this.parentNode.nextLineRow.remove();
        this.parentNode.nextRow=null;
        this.parentNode.lineCols.splice(0,this.parentNode.lineCols.length);
    }
};

TreeNode.prototype.addChild=function(type){
    _setDirty();
    if(!this.nextRow){
        this.nextRow=$("<tr style='min-height:40px'>");
        this.nextLineRow=$("<tr style='height:20px;'>");
        this.lineRow=$("<tr style='height:20px;'>");
        this.lineCol=$("<td><div class='vertical-line'></div></td>");
        this.lineRow.append(this.lineCol);
        this.container.append(this.lineRow);
        this.container.append(this.nextLineRow);
        this.container.append(this.nextRow);
    }
    var newLineCols=this.newLine();

    var newCol=$("<td class='nodeContainer' align='center' colspan='2'>");
    var childNode;
    if(type==="condition"){
        childNode=new ConditionTreeNode(this);
    }else if(type==="action"){
        childNode=new ActionTreeNode(this);
    }else if(type==="variable"){
        childNode=new VariableTreeNode(this,true);
    }
    childNode.newLineCols=newLineCols;
    childNode.parentCol=newCol;
    this.childrenNodes.push(childNode);
    newCol.append(childNode.container);
    this.nextColCount+=2;
    this.nextRow.append(newCol);
    if(this.nextColCount>1){
        this.col.prop("colspan",this.nextColCount);
        this.lineCol.prop("colspan",this.nextColCount);
    }
    return childNode;
};
TreeNode.prototype.initChildrenNodeData=function(data){
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
};
TreeNode.prototype.newLine=function(){
    var lineCol1=$("<td style='border-right:solid #4cae4c 2px;'></td>");
    this.nextLineRow.append(lineCol1);
    var lineCol2=$("<td style='border-left:solid #4cae4c 2px;'></td>");
    this.nextLineRow.append(lineCol2);
    if(this.lineCols.length>0){
        var lastLineCol=this.lineCols[this.lineCols.length-1];
        lastLineCol.css("border-top","solid #4cae4c 2px");
        lineCol1.css("border-top","solid #4cae4c 2px");
    }
    this.lineCols.push(lineCol1);
    this.lineCols.push(lineCol2);
    return [lineCol1,lineCol2];
};