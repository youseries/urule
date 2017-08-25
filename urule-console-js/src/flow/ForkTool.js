/**
 * Created by jacky on 2016/7/18.
 */
import BaseTool from './BaseTool.js';
import ForkNode from './ForkNode.js';

export default class ForkTool extends BaseTool{
    getType(){
        return '分支';
    }
    getIcon(){
        return `<i class="rf rf-fork" style="color:#737383"></i>`
    }
    newNode(){
        return new ForkNode();
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