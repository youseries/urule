/**
 * Created by jacky on 2016/7/18.
 */
import startSVG from './svg/start.svg';
import BaseNode from './BaseNode.js';

export default class StartNode extends BaseNode{
    getSvgIcon(){
        return startSVG;
    }
    toXML(){
        const json=this.toJSON();
        json.type="StartNode";
        const nodeName=this.getNodeName(json.type);
        const nodeProps=this.getXMLNodeBaseProps(json);
        let xml=`<${nodeName} ${nodeProps}>`;
        xml+=this.getFromConnectionsXML();
        xml+=`</${nodeName}>`;
        return xml;
    }
}