/**
 * Created by Jacky.gao on 2016/2/24.
 */
ActionTreeNode=function(parentNode){
    TreeNode.call(this,parentNode);
    this.actionTypes=[];
    this.initNode();
};
ActionTreeNode.prototype=Object.create(TreeNode.prototype);
ActionTreeNode.prototype.constructor=ActionTreeNode;
ActionTreeNode.prototype.initNode=function(){
    this.nodeContainer=$("<div class='node actionNode'>");
    this.col.append(this.nodeContainer);
    this.actionsContainer=$("<span style='display: inline-block;'>");
    this.nodeContainer.append(this.actionsContainer);
    this.addAction();
    var self=this;
    var operations=$("<span class='operations'><i class='icon-ok-circle'></i></span>");
    this.nodeContainer.append(operations);
    var menuItems=[];
    menuItems.push({
        name:"delete",
        label:"删除",
        onClick:function(){
            URule.confirm("真的要删除当前节点？",function(){
                self.delete();
            });
        }
    });
    menuItems.push({
        name:"addAction",
        label:"添加动作",
        onClick:function(){
            self.addAction(true);
        }
    });
    var menu=new URule.menu.Menu({menuItems:menuItems});
    operations.click(function(e){
        menu.show(e);
    });
};
ActionTreeNode.prototype.addAction=function(notfirst){
    var actionContainer=$("<span>");
    if(notfirst){
        actionContainer.css("display","block");
    }
    var delIcon=$("<i class='icon-minus-sign icon-large' style='color: #ac2925;padding-right: 5px'></i>");
    actionContainer.append(delIcon);
    this.actionsContainer.append(actionContainer);
    var newActionType=new urule.ActionType(actionContainer);
    this.actionTypes.push(newActionType);
    actionContainer.actionType=newActionType;
    var self=this;
    delIcon.click(function(){
        if(self.actionTypes.length===1){
            URule.alert("动作至少要有一个.");
            return;
        }
        var pos=-1;
        $.each(self.actionTypes,function(i,at){
            if(at===actionContainer.actionType){
                pos=i;
                return false;
            }
        });
        if(pos!==-1){
            self.actionTypes.splice(pos,1);
            actionContainer.remove();
        }else{
            URule.alert("未找到要删除的动作对象.");
        }
    });
    return newActionType;
};
ActionTreeNode.prototype.initData=function(data){
    if(!data){
        return;
    }
    var actions=data["actions"];
    if(!actions || actions.length===0){
        return;
    }
    this.actionTypes[0].parentContainer.remove();
    this.actionTypes.splice(0,1);
    for(var i=0;i<actions.length;i++){
        var action=actions[i];
        var newActionType=this.addAction(i!==0);
        newActionType.initData(action);
    }
};

ActionTreeNode.prototype.toXml=function(){
    var xml="<action-tree-node>";
    $.each(this.actionTypes,function(i,actionType){
        xml+=actionType.toXml();
    });
    xml+="</action-tree-node>";
    return xml;
};