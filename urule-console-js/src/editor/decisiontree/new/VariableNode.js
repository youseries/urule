/**
 * Created by Jacky.Gao on 2016/3/23.
 */
import {MsgBox} from 'flowdesigner';
import BaseNode from './BaseNode.js';
export default class VariableNode extends BaseNode{
    constructor(context,parentNode,disabledDel){
        super(context,parentNode);
        this.disabledDel=disabledDel;
        this.init();
    }
    init(){
        this.nodeContainer=$("<div class='node varNode'>");
        this.context.container.append(this.nodeContainer);
        this.initBindResizeEvent();
        var contentContainer=$("<span>");
        this.nodeContainer.append(contentContainer);
        this.condition=new urule.ConditionLeft(contentContainer);
        var self=this;
        var operations=$("<span class='operations'><i class='glyphicon glyphicon-ok-circle'></i></span>");
        this.nodeContainer.append(operations);
        var menuItems=[];
        menuItems.push({
            name:"addCondition",
            label:"添加条件",
            onClick:function(){
                self.addChild("condition");
            }
        });
        if(!this.disabledDel){
            menuItems.push({
                name:"delete",
                label:"删除",
                onClick:function(){
                    MsgBox.confirm("真的要删除当前节点？",function(){
                        self.delete();
                    });
                }
            });
        }
        var menu=new URule.menu.Menu({menuItems:menuItems});
        operations.click(function(e){
            menu.show(e);
        });
        this.nodeHeight=this.nodeContainer.height()+15;
        this.nodeWidth=this.nodeContainer.width();
    }
    initData(data){
        if(!data){
            return;
        }
        var left=data["left"];
        this.condition.initData(left);
        super.initChildrenNodeData(data);
    }
    toXml(){
        if(this.children.length==0){
            throw "变量节点下至少要有一个条件节点.";
        }
        var xml="<variable-tree-node>";
        xml+=this.condition.toXml();
        $.each(this.children,function(i,connection){
            xml+=connection.node.toXml();
        });
        xml+="</variable-tree-node>";
        return xml;
    }
};