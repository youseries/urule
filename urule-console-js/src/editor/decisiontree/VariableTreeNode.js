/**
 * Created by Jacky.gao on 2016/2/24.
 */
VariableTreeNode=function(parentNode,allowDelete){
    this.allowDelete=allowDelete;
    TreeNode.call(this,parentNode);
    this.initNode();
};
VariableTreeNode.prototype=Object.create(TreeNode.prototype);
VariableTreeNode.prototype.constructor=VariableTreeNode;
VariableTreeNode.prototype.initNode=function(){
    var nodeContainer=$("<div class='node varNode'>");
    this.col.append(nodeContainer);
    var contentContainer=$("<span>");
    nodeContainer.append(contentContainer);
    this.condition=new urule.ConditionLeft(contentContainer);
    var self=this;
    var operations=$("<span class='operations'><i class='icon-ok-circle'></i></span>");
    nodeContainer.append(operations);
    var menuItems=[];
    menuItems.push({
        name:"addCondition",
        label:"添加条件",
        onClick:function(){
            self.addChild("condition");
        }
    });
    if(this.allowDelete){
        menuItems.push({
            name:"delete",
            label:"删除",
            onClick:function(){
                URule.confirm("真的要删除当前节点？",function(){
                    self.delete();
                });
            }
        });
    }
    var menu=new URule.menu.Menu({menuItems:menuItems});
    operations.click(function(e){
        menu.show(e);
    });
};
VariableTreeNode.prototype.initData=function(data){
    if(!data){
        return;
    }
    var left=data["left"];
    this.condition.initData(left);
    TreeNode.prototype.initChildrenNodeData.call(this,data);
};
VariableTreeNode.prototype.toXml=function(){
    if(this.childrenNodes.length==0){
        throw "变量节点下至少要有一个条件节点.";
    }
    var xml="<variable-tree-node>";
    xml+=this.condition.toXml();
    $.each(this.childrenNodes,function(i,childNode){
        xml+=childNode.toXml();
    });
    xml+="</variable-tree-node>";
    return xml;
};