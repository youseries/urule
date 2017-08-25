/**
 * Created by jacky on 2016/7/18.
 */
import ruleSVG from './svg/rule.svg';
import BaseNode from './BaseNode.js';

export default class RuleNode extends BaseNode{
    getSvgIcon(){
        return ruleSVG;
    }
    toXML(){
        const json=this.toJSON();
        json.type="RuleNode";
        const nodeName=this.getNodeName(json.type);
        const nodeProps=this.getXMLNodeBaseProps(json);
        let xml=`<${nodeName} ${nodeProps} file="${this.file}" version="${this.version}">`;
        xml+=this.getFromConnectionsXML();
        xml+=`</${nodeName}>`;
        return xml;
    }

    initFromJson(json){
        super.initFromJson(json);
        this.file=json.file;
        this.version=json.version;
    }

    validate(){
        let errorInfo=super.validate();
        if(errorInfo)return errorInfo;
        if(!this.file){
            errorInfo=`节点${this.name}的文件属性不能为空`;
            return errorInfo;
        }
        if(!this.version){
            errorInfo=`节点${this.version}的版本属性不能为空`;
        }
        return errorInfo;
    }
}