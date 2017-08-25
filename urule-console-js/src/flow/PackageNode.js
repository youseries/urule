/**
 * Created by jacky on 2016/7/18.
 */
import packageSVG from './svg/package.svg';
import BaseNode from './BaseNode.js';

export default class PackageNode extends BaseNode{
    getSvgIcon(){
        return packageSVG;
    }
    toXML(){
        const json=this.toJSON();
        json.type="PackageNode";
        const nodeName=this.getNodeName(json.type);
        const nodeProps=this.getXMLNodeBaseProps(json);
        let xml=`<${nodeName} ${nodeProps} project="${window._project}" package-id="${this.packageId}">`;
        xml+=this.getFromConnectionsXML();
        xml+=`</${nodeName}>`;
        return xml;
    }

    initFromJson(json){
        super.initFromJson(json);
        this.packageId=json.packageId;
    }

    validate(){
        let errorInfo=super.validate();
        if(errorInfo)return errorInfo;
        if(!this.packageId){
            errorInfo=`${this.name}节点的知识包属性不能为空`;
        }
        return errorInfo;
    }
}