/**
 * Created by jacky on 2016/7/18.
 */
import scriptSVG from './svg/script.svg';
import BaseNode from './BaseNode.js';

export default class ScriptNode extends BaseNode{
    getSvgIcon(){
        return scriptSVG;
    }
    toXML(){
        const json=this.toJSON();
        json.type="ScriptNode";
        const nodeName=this.getNodeName(json.type);
        const nodeProps=this.getXMLNodeBaseProps(json);
        let xml=`<${nodeName} ${nodeProps}>`;
        xml+=this.getFromConnectionsXML();
        xml+=`<content>`;
        xml+=`<![CDATA[${this.script}]]>`;
        xml+=`</content>`;
        xml+=`</${nodeName}>`;
        return xml;
    }

    initFromJson(json){
        super.initFromJson(json);
        this.script=json.script;
    }

    validate(){
        let errorInfo=super.validate();
        if(errorInfo)return errorInfo;
        if(!this.script){
            errorInfo=`节点${this.name}的脚本不能为空`;
        }
        return errorInfo;
    }
}