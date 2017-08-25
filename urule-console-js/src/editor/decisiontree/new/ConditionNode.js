/**
 * Created by Jacky.gao on 2016/3/28.
 */
import {MsgBox} from 'flowdesigner';
import BaseNode from './BaseNode.js';

export default class ConditionNode extends BaseNode{
    constructor(context,parentNode){
        super(context,parentNode);
        this.init();
    }
    init(){
        this.nodeContainer=$("<div class='node conditionNode'>");
        this.context.container.append(this.nodeContainer);
        this.initBindResizeEvent();
        var self=this;
        this.contentContainer=$("<span>");
        this.nodeContainer.append(this.contentContainer);
        this.operator=new urule.ComparisonOperator(function(){
            self.inputType=self.operator.getInputType();
            if(self.inputType){
                self.contentContainer.append(self.inputType.getContainer());
            }
        });
        this.contentContainer.append(this.operator.getContainer());

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
                MsgBox.confirm("真的要删除当前节点？",function(){
                    self.delete();
                });
            }
        });
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
        var op=data["op"];
        this.operator.setOperator(op);
        var value=data["value"];
        this.operator.initRightValue(value);
        this.inputType=this.operator.getInputType();
        if(this.inputType){
            this.contentContainer.append(this.inputType.getContainer());
        }
        super.initChildrenNodeData(data);
    }
    toXml(){
        if(this.children.length==0){
            throw "条件节点下至少要有一个动作节点.";
        }
        var xml="<condition-tree-node op=\""+this.operator.getOperator()+"\">";
        if(this.inputType){
            xml+=this.inputType.toXml();
        }
        $.each(this.children,function(i,connection){
            xml+=connection.node.toXml();
        });
        xml+="</condition-tree-node>";
        return xml;
    }
};