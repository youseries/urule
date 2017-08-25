/**
 * Created by jacky on 2016/7/18.
 */
import CodeMirror from './CodeMirror.js';
import '../../node_modules/codemirror/addon/hint/show-hint.js';
import BaseTool from './BaseTool.js';
import ScriptNode from './ScriptNode.js';

export default class ScriptTool extends BaseTool{
    getType(){
        return '脚本';
    }
    getIcon(){
        return `<i class="rf rf-script" style="color:#737383"></i>`
    }
    newNode(){
        return new ScriptNode();
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
            const scriptGroup=$(`<div class="form-group"><label>动作脚本</label></div>`);
            const scriptArea=$(`<textarea></textarea>`);
            scriptGroup.append(scriptArea);
            const self=this;
            scriptArea.change(function(){
                self.script=$(this).val();
            });
            scriptArea.val(this.script);

            setTimeout(function () {
                const codeMirror = CodeMirror.fromTextArea(scriptArea[0], {
                    lineNumbers: true,
                    mode: "then",
                    extraKeys: {"Alt-/": "autocomplete"},
                    gutters: ["CodeMirror-linenumbers", "CodeMirror-lint-markers"],
                    lint: {
                        getAnnotations:_this.buildScriptLintFunction('ScriptNode'),
                        async:true
                    }
                });
                CodeMirror.commands.autocomplete = function(cm) {
                    cm.showHint({hint: CodeMirror.hint["if"]});
                };

                codeMirror.on('change',function(cm,e){
                    if(e.text=='.'){
                        CodeMirror.commands.autocomplete(cm);
                    }
                    const scriptContent=codeMirror.getValue();
                    scriptArea.val(scriptContent);
                    self.script=scriptContent;
                });
            },100);

            g.append(scriptGroup);
            g.append(_this.getCommonProperties(this));
            return g;
        }
    }
}