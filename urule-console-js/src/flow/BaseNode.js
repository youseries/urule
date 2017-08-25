/**
 * Created by jacky on 2016/7/18.
 */
import {Node,MsgBox} from 'flowdesigner';

export default class BaseNode extends Node{
    getNodeName(type){
        switch (type){
            case "ActionNode":
                return 'action';
            case "DecisionNode":
                return 'decision';
            case "ForkNode":
                return 'fork';
            case "JoinNode":
                return 'join';
            case "PackageNode":
                return 'rule-package';
            case "RuleNode":
                return 'rule';
            case "ScriptNode":
                return 'script';
            case "StartNode":
                return 'start';
            default:
                MsgBox.alert(`不支持的节点类型[${type}]`);
        }
    }

    initFromJson(json){
        const {width,height}=json;
        if(parseInt(width)<30){
            json.width=30;
        }
        if(parseInt(height)<65){
            json.height=65;
        }
        super.initFromJson(json);
        this.eventBean=json.eventBean;
    }

    validate(){
        let errorInfo=null;
        if(!this.name || this.name.length<1){
            errorInfo=`节点名不能为空!`;
            return errorInfo;
        }
        if(this.in===-1 || this.in>0){
            if(this.out===-1 || this.out>0){
                if(this.fromConnections.length===0 && this.toConnections.length===0){
                    errorInfo=`${this.name}节点未和其它节点连接!`;
                }
            }else{
                if(this.toConnections.length===0){
                    errorInfo=`${this.name}节点未和其它节点连接!`;
                }
            }
        }else{
            if(this.out===-1 || this.out>0){
                if(this.fromConnections.length===0){
                    errorInfo=`${this.name}节点未和其它节点连接!`;
                }
            }
        }
        return errorInfo;
    }

    getFromConnectionsXML(){
        let xml='';
        for(let conn of this.fromConnections){
            xml+=this.buildConnectionXML(conn);
        }
        return xml;
    }

    buildConnectionXML(connection){
        const json=connection.toJSON();
        const path=json.path;
        let pathInfo='',i=0;
        for(let p of path){
            if(i>0 && i!==path.length-1){
                if(pathInfo!==''){
                    pathInfo+=',';
                }
                pathInfo+=parseInt(p[1])+','+parseInt(p[2]);
            }
            i++;
        }
        let xml=`<connection g="${pathInfo}" type="${json.type}" to="${json.to}"`;
        if(json.name){
            xml+=` name="${json.name}"`;
        }
        xml+='>';
        xml+='</connection>';
        return xml;
    }

    getXMLNodeBaseProps(json){
        let xml=`name="${json.name}" `;
        xml+=`x="${json.x}" `;
        xml+=`y="${json.y}" `;
        xml+=`width="${json.w}" `;
        xml+=`height="${json.h}" `;
        if(this.eventBean){
            xml+=`event-bean="${this.eventBean}"`;
        }
        return xml;
    }
}