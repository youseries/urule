/**
 * Created by jacky on 2016/7/18.
 */
import BaseTool from './BaseTool.js';
import StartNode from './StartNode.js';

export default class StartTool extends BaseTool{
    getType(){
        return '开始';
    }
    getIcon(){
        return `<i class="rf rf-start" style="color:#737383"></i>`
    }
    newNode(){
        return new StartNode();
    }
    getConfigs(){
        return {
            in:0,
            out:1,
            single:true
        };
    }
    getPropertiesProducer(){
        const _this=this;
        return function (){
            const g=$(`<div></div>`);
            g.append(_this.getCommonProperties(this));
            return g;
        }
    }
}