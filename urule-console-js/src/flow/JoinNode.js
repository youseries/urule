/**
 * Created by jacky on 2016/7/18.
 */
import joinSVG from './svg/join.svg';
import BaseNode from './BaseNode.js';

export default class JoinNode extends BaseNode{
    getSvgIcon(){
        return joinSVG;
    }
    toXML(){
        const json=this.toJSON();
        json.type="JoinNode";
        const nodeName=this.getNodeName(json.type);
        const nodeProps=this.getXMLNodeBaseProps(json);
        let xml=`<${nodeName} ${nodeProps}>`;
        xml+=this.getFromConnectionsXML();
        xml+=`</${nodeName}>`;
        return xml;
    }
}