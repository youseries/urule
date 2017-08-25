/**
 * Created by jacky on 2016/7/18.
 */
import BaseTool from './BaseTool.js';
import ActionNode from './ActionNode.js';

export default class ActionTool extends BaseTool{
    getType(){
        return '动作';
    }
    getIcon(){
        return `<i class="rf rf-action" style="color:#737383"></i>`
    }
    newNode(){
        return new ActionNode();
    }
    getConfigs(){
        return {
            out:1
        };
    }
    getPropertiesProducer(){
        const _this=this;
        return function (){
            const g=$(`<div></div>`);
            const actionBeanGroup=$(`<div class="form-group"><label>动作Bean</label></div>`);
            const actionBeanText=$(`<input type="text" class="form-control" title="一个实现了com.bstek.urule.model.flow.FlowAction接口并配置到Spring中的Bean的ID">`);
            actionBeanGroup.append(actionBeanText);
            const self=this;
            actionBeanText.change(function(){
                self.actionBean=$(this).val();
            });
            actionBeanText.val(this.actionBean);
            g.append(actionBeanGroup);
            g.append(_this.getCommonProperties(this));
            return g;
        }
    }
}