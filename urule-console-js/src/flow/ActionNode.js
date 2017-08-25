/**
 * Created by jacky on 2016/7/18.
 */
import actionSVG from './svg/action.svg';
import BaseNode from './BaseNode.js';

export default class ActionNode extends BaseNode{
    getSvgIcon(){
        return actionSVG;
    }
    toXML(){
        const json=this.toJSON();
        json.type="ActionNode";
        const nodeName=this.getNodeName(json.type);
        const nodeProps=this.getXMLNodeBaseProps(json);
        let xml=`<${nodeName} ${nodeProps} action-bean="${this.actionBean}">`;
        xml+=this.getFromConnectionsXML();
        xml+=`</${nodeName}>`;
        return xml;
    }
    initFromJson(json){
        super.initFromJson(json);
        this.actionBean=json.actionBean;
    }
    validate(){
        let errorInfo=super.validate();
        if(errorInfo)return errorInfo;
        if(!this.actionBean){
            errorInfo=`${this.name}节点的动作Bean属性不能为空`;
        }
        return errorInfo;
    }
}