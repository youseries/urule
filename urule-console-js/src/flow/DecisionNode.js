/**
 * Created by jacky on 2016/7/18.
 */
import decisionSVG from './svg/decision.svg';
import BaseNode from './BaseNode.js';

export default class DecisionNode extends BaseNode{
    getSvgIcon(){
        return decisionSVG;
    }
    toXML(){
        const json=this.toJSON();
        json.type="DecisionNode";
        const nodeName=this.getNodeName(json.type);
        const nodeProps=this.getXMLNodeBaseProps(json);
        let xml=`<${nodeName} ${nodeProps} decision-type="${this.decisionType}">`;
        let itemXML='';
        for(let item of this.decisionItems){
            let s=null;
            if(this.decisionType==='Percent'){
                s=`<item connection="${item.to}" percent="${item.percent}"></item>`;
            }else{
                s=`<item connection="${item.to}"><![CDATA[${item.script}]]></item>`;
            }
            itemXML+=s;
        }
        xml+=itemXML;
        xml+=this.getFromConnectionsXML();
        xml+=`</${nodeName}>`;
        return xml;
    }

    initFromJson(json){
        super.initFromJson(json);
        this.decisionType=json.decisionType;
        this.decisionItems=json.items;
    }

    validate(){
        let errorInfo=super.validate();
        if(errorInfo)return errorInfo;
        if(!this.decisionType || this.decisionType===''){
            errorInfo=`节点${this.name}的决策类型属性不能为空`;
            return errorInfo;
        }
        if(this.decisionItems.length<1){
            errorInfo=`节点${this.name}的具体的决策项不能少于一个`;
        }
        for(let item of this.decisionItems){
            if(this.decisionType==='Percent'){
                if(!item.to || item.to==='' || !item.percent || item.percent===''){
                    errorInfo=`节点${this.name}的决策项未正确配置`;
                    break;
                }
            }else{
                if(!item.to || item.to==='' || !item.script || item.script===''){
                    errorInfo=`节点${this.name}的决策项未正确配置`;
                    break;
                }
            }
        }
        return errorInfo;
    }
}