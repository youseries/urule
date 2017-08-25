/**
 * Created by Jacky.Gao on 2016/3/23.
 */
import Raphael from 'raphael';
import VariableNode from './VariableNode.js';
import ActionNode from './ActionNode.js';
import ConditionNode from './ConditionNode.js';

export default class Context{
    constructor(container,rootNode){
        this.container=container;
        this.paper = new Raphael(container.get(0), "100%", "100%");
    }
    newVariableNode(parentNode){
        const newNode=new VariableNode(this,parentNode);
        return newNode;
    }
    newActionNode(parentNode){
        const newNode=new ActionNode(this,parentNode);
        return newNode;
    }
    newConditionNode(parentNode){
        const newNode=new ConditionNode(this,parentNode);
        return newNode;
    }
};