/**
 * Created by Jacky.gao on 2016/3/28.
 */
import BaseNode from './BaseNode.js';
import {MsgBox} from 'flowdesigner';

export default class ActionNode extends BaseNode{
    constructor(context,parentNode){
        super(context,parentNode);
        this.actionTypes=[];
        this.init();
    }
    init(){
        this.nodeContainer=$("<div class='node actionNode'>");
        this.context.container.append(this.nodeContainer);
        this.initBindResizeEvent();
        this.actionsContainer=$("<span style='display: inline-block;'>");
        this.nodeContainer.append(this.actionsContainer);
        this.addAction();
        var self=this;
        var operations=$("<span class='operations'><i class='glyphicon glyphicon-ok-circle'></i></span>");
        this.nodeContainer.append(operations);
        var menuItems=[];
        menuItems.push({
            name:"delete",
            label:"删除",
            onClick:function(){
                MsgBox.confirm("真的要删除当前节点？",function(){
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
        this.nodeHeight=this.nodeContainer.height()+15;
        this.nodeWidth=this.nodeContainer.width();
    }
    addAction(notfirst){
        var actionContainer=$("<span>");
        if(notfirst){
            actionContainer.css("display","block");
        }
        window._setDirty();
        var delIcon=$("<i class='glyphicon glyphicon-minus-sign' style='color: #ac2925;padding-right: 5px'></i>");
        actionContainer.append(delIcon);
        this.actionsContainer.append(actionContainer);
        var newActionType=new urule.ActionType(actionContainer);
        this.actionTypes.push(newActionType);
        actionContainer.actionType=newActionType;
        var self=this;
        delIcon.click(function(){
            if(self.actionTypes.length===1){
                MsgBox.alert("动作至少要有一个.");
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
                window._setDirty();
            }else{
                MsgBox.alert("未找到要删除的动作对象.");
            }
        });
        return newActionType;
    }
    initData(data){
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
    }
    toXml(){
        var xml="<action-tree-node>";
        $.each(this.actionTypes,function(i,actionType){
            xml+=actionType.toXml();
        });
        xml+="</action-tree-node>";
        return xml;
    }
};