/**
 * Created by Jacky.gao on 2016/2/24.
 */
ConditionTreeNode=function(parentNode){
    TreeNode.call(this,parentNode);
    this.initNode();
};
ConditionTreeNode.prototype=Object.create(TreeNode.prototype);
ConditionTreeNode.prototype.constructor=ConditionTreeNode;
ConditionTreeNode.prototype.initNode=function(){
    var nodeContainer=$("<div class='node conditionNode'>");
    this.col.append(nodeContainer);
    var self=this;
    var contentContainer=$("<span>");
    this.contentContainer=contentContainer;
    nodeContainer.append(contentContainer);
    this.operator=new urule.ComparisonOperator(function(){
        self.inputType=self.operator.getInputType();
        if(self.inputType){
            contentContainer.append(self.inputType.getContainer());
        }
    });
    contentContainer.append(this.operator.getContainer());

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
    menuItems.push({
        name:"addVariable",
        label:"添加变量",
        onClick:function(){
            self.addChild("variable");
        }
    });
    menuItems.push({
        name:"addAction",
        label:"添加动作",
        onClick:function(){
            self.addChild("action");
        }
    });
    menuItems.push({
        name:"delete",
        label:"删除",
        onClick:function(){
            URule.confirm("真的要删除当前节点？",function(){
                self.delete();
            });
        }
    });
    var menu=new URule.menu.Menu({menuItems:menuItems});
    operations.click(function(e){
        menu.show(e);
    });
};

ConditionTreeNode.prototype.initData=function(data){
    if(!data){
        return;
    }
    var op=data["op"];
    this.operator.setOperator(op);
    var value=data["value"];
    this.operator.initRightValue(value);
    this.inputType=this.operator.getInputType();
    if(this.inputType){
        this.contentContainer.append(this.inputType.getContainer());
    }
    TreeNode.prototype.initChildrenNodeData.call(this,data);
};

ConditionTreeNode.prototype.toXml=function(){
    if(this.childrenNodes.length==0){
        throw "条件节点下至少要有一个动作节点.";
    }
    var xml="<condition-tree-node op=\""+this.operator.getOperator()+"\">";
    if(this.inputType){
        xml+=this.inputType.toXml();
    }
    $.each(this.childrenNodes,function(i,childNode){
        xml+=childNode.toXml();
    });
    xml+="</condition-tree-node>";
    return xml;
};