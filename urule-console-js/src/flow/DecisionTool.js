/**
 * Created by jacky on 2016/7/18.
 */
import CodeMirror from './CodeMirror.js';
import '../../node_modules/codemirror/addon/hint/show-hint.js';
import BaseTool from './BaseTool.js';
import DecisionNode from './DecisionNode.js';
import {MsgBox} from 'flowdesigner';

export default class DecisionTool extends BaseTool{
    getType(){
        return '决策';
    }
    getIcon(){
        return `<i class="rf rf-decision" style="color:#737383"></i>`
    }
    newNode(){
        return new DecisionNode();
    }
    getPropertiesProducer(){
        const _this=this;
        return function (){
            const g=$(`<div></div>`);
            const decisionTypeGroup=$(`<div class="form-group"><label>决策类型</label></div>`)
            const decisionTypeSelect=$(`<select class="form-control"></select>`);
            decisionTypeSelect.append('<option value="Criteria">条件</option>');
            decisionTypeSelect.append('<option value="Percent">百分比</option>');
            const self=this;
            decisionTypeSelect.val(this.decisionType);
            decisionTypeGroup.append(decisionTypeSelect);
            const tableContainer=$('<div>');

            if(!this.decisionItems || this.decisionItems.length!==self.fromConnections.length){
                this.decisionItems=[];
                for(let conn of self.fromConnections){
                    this.decisionItems.push({connection:'',content:''});
                }
            }

            const percentTable=$(`<table class="table table-bordered"></table>`);
            tableContainer.append(percentTable);
            percentTable.hide();
            percentTable.append(`<thead><tr><td>百分比(%)</td><td style="width: 160px">流向</td></tr></thead>`);
            const percentTbody=$(`<tbody></tbody>`);
            percentTable.append(percentTbody);
            let index=0;
            for(let conn of this.fromConnections){
                const item=this.decisionItems[index];
                index++;
                const tr=$('<tr></tr>');
                percentTbody.append(tr);
                const td=$('<td></td>');
                tr.append(td);
                const percentText=$('<input type="text" class="form-control">');
                td.append(percentText);
                percentText.change(function(){
                    item.percent=$(this).val();
                });
                percentText.val(item.percent);
                const td1=$("<td></td>");
                tr.append(td1);
                const pathSelect=$(`<select class="form-control"></select>`);
                for(let c of this.fromConnections){
                    pathSelect.append(`<option>${c.name ? c.name : ''}</option>`);
                }
                pathSelect.change(function(){
                    item.to=$(this).val();
                });
                pathSelect.val(item.to);
                td1.append(pathSelect);
            }

            const conditionTable=$(`<table class="table table-bordered"></table>`);
            tableContainer.append(conditionTable);
            conditionTable.hide();
            conditionTable.append(`<thead><tr><td>条件脚本</td><td style="width: 160px">流向</td></tr></thead>`);
            const tbody=$(`<tbody></tbody>`);
            conditionTable.append(tbody);
            index=0;
            for(let conn of this.fromConnections){
                let decisionItem=this.decisionItems[index];
                index++;
                const tr=$('<tr></tr>');
                tbody.append(tr);
                const td=$('<td></td>');
                tr.append(td);
                const textGroup=$(`<div class="input-group"></div>`);
                const scriptText=$(`<input type="text" class="form-control" style="font-size: 12px" value="${decisionItem.script || ''}">`);
                textGroup.append(scriptText);
                const openEditorButton=$(`<span class="input-group-btn"><button type="button" class="btn btn-default"><i class="glyphicon glyphicon-edit"></i></button></span>`);
                textGroup.append(openEditorButton);
                openEditorButton.click(function(){
                    const codeEditorContainer=$('<div></div>');
                    const codeEditor=$(`<textarea>${decisionItem.script || ''}</textarea>`);
                    codeEditorContainer.append(codeEditor);
                    let codeMirror=null;
                    MsgBox.dialog('编辑脚本',codeEditorContainer,function(){
                        if(codeMirror){
                            const scriptContent=codeMirror.getValue();
                            codeEditor.val(scriptContent);
                            scriptText.val(scriptContent);
                            decisionItem.script=scriptContent;
                        }
                    });
                    setTimeout(function(){
                        codeMirror = CodeMirror.fromTextArea(codeEditor[0], {
                            lineNumbers: true,
                            mode: "if",
                            extraKeys: {"Alt-/": "autocomplete"},
                            gutters: ["CodeMirror-linenumbers", "CodeMirror-lint-markers"],
                            lint: {
                                getAnnotations:_this.buildScriptLintFunction('DecisionNode'),
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
                        });
                    },500);
                });
                td.append(textGroup);

                const td1=$("<td></td>");
                tr.append(td1);
                const pathSelect=$(`<select class="form-control" style="font-size: 12px"></select>`);
                for(let c of this.fromConnections){
                    pathSelect.append(`<option>${c.name ? c.name : ''}</option>`);
                }
                pathSelect.change(function(){
                    decisionItem.to=$(this).val();
                });
                pathSelect.val(decisionItem.to || '');
                td1.append(pathSelect);
            }

            decisionTypeSelect.change(function(){
                self.decisionType=$(this).val();
                if(self.decisionType==='Criteria'){
                    percentTable.hide();
                    conditionTable.show();
                }else{
                    conditionTable.hide();
                    percentTable.show();
                }
            });
            if(self.decisionType==='Criteria'){
                percentTable.hide();
                conditionTable.show();
            }else if(self.decisionType==='Percent'){
                conditionTable.hide();
                percentTable.show();
            }
            g.append(decisionTypeGroup);
            g.append(tableContainer);
            g.append(_this.getCommonProperties(this));
            return g;
        }
    }
}