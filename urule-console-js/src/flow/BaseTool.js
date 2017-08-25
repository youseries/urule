/**
 * Created by jacky on 2016/7/18.
 */
import {Tool} from 'flowdesigner';
import '../../node_modules/codemirror/addon/lint/lint.js';

export default class BaseTool extends Tool{
    getCommonProperties(target){
        const eventGroup=$(`<div class="form-group" title="一个实现了com.bstek.urule.model.flow.NodeEvent接口配置在Spring中bean的id，一旦配置在流程进入及离开该节点时会触发这个实现类"><label>事件Bean</label></div>`);
        const eventText=$(`<input type="text" class="form-control">`);
        eventText.change(function(){
            target.eventBean=$(this).val();
        });
        eventText.val(target.eventBean);
        eventGroup.append(eventText);
        return eventGroup;
    }
    buildScriptLintFunction(type){
        return function (text, updateLinting, options, editor){
            if(text===''){
                updateLinting(editor,[]);
                return;
            }
            const url=window._server+'/common/scriptValidation';
            $.ajax({
                url,
                data:{type,content:text},
                type:'POST',
                success:function(result){
                    if(result){
                        for(let item of result){
                            item.from={line:item.line-1};
                            item.to={line:item.line-1};
                        }
                        updateLinting(editor,result);
                    }else{
                        updateLinting(editor,[]);
                    }
                },
                error:function(){
                    alert('语法检查操作失败！');
                }
            });
        };
    }
}