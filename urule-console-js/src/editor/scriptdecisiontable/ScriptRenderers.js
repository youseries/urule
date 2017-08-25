import CodeMirror from 'codemirror';

(function (Handsontable) {
	'use strict';
	var URuleRenderer = function (instance, TD, row, col, prop, value, cellProperties) {
		Handsontable.renderers.cellDecorator.apply(this, arguments);
	    if(!value && cellProperties.placeholder) {
	    	value = cellProperties.placeholder;
	    }
	    var self = this, cellData=ht.getCellData(row,col), content, codeMirror, colData=ht.getColData(col), editor, type=colData.type, mode,scriptType;
	    applySpanProperties(TD,cellData);
	    if(!cellData || cellData.codeMirror){
	    	if(cellData && cellData.codeMirror) {
	    		cellData.codeMirror.setValue(cellData.script || "");
	    	}
	    	return;
	    }
	    cellData.container=TD;
	    $(TD).empty();
	    var container = $("<div/>");
	    editor = $("<textarea/>")[0];
	    container.append(editor);
		CodeMirror.commands.autocomplete = function(cm) {
		    cm.showHint({hint: CodeMirror.hint["if"]});
		};
		if(type == "Criteria") {
			mode = "if";
			scriptType='DecisionNode';
		} else if(type == "ConsolePrint") {
			mode = "print";
			scriptType='ScriptNode';
		} else {
			mode = "then";
			scriptType='ScriptNode';
		}
		codeMirror = CodeMirror.fromTextArea(editor, {
		    mode: mode,
		    extraKeys: {"Alt-/": "autocomplete"},
		    autofocus : false
		});
		codeMirror.on("change",function(cm,e){
			if(e.text=="."){
				CodeMirror.commands.autocomplete(codeMirror);
			}
			cellData.script = cm.getValue();
			ht.setDirty();
			setTimeout(function() {
				ht.invoke("render");
			}, 200);
		});
		codeMirror.on("keyup",function(cm,e){
			if(e.keyCode==13){
				ht.invoke("render");
			}
		});
		codeMirror.setSize("100%","100%");
		$(TD).append(container).click(function(event){
			codeMirror.focus();
		});
		codeMirror.setValue(cellData.script || "");
		cellData.codeMirror = codeMirror;
	};
	
	var applySpanProperties = function (TD, cellData) {
		  if (cellData) {
			  var rowspan=cellData.rowspan;
			  TD.style.display="table-cell";
			  if (rowspan>1) {
			      TD.setAttribute('rowspan', rowspan);
			  }else{
				  TD.removeAttribute('rowspan');
			  }
		  }else {
			  TD.style.display="none";
			  TD.removeAttribute('rowspan');
			  TD.removeAttribute('colspan');
		  }
	};
	
	Handsontable.renderers.URuleRenderer = URuleRenderer;
	Handsontable.renderers.registerRenderer('urule', URuleRenderer);
})(Handsontable);

(function(){
	Handsontable.URuleCell = {
		editor:Handsontable.editors.TextEditor,
		renderer: Handsontable.renderers.URuleRenderer
	};
	Handsontable.cellTypes.urule=Handsontable.URuleCell;
})();
