(function (Handsontable) {
	'use strict';
	var URuleRenderer = function (instance, TD, row, col, prop, value, cellProperties) {
		Handsontable.renderers.cellDecorator.apply(this, arguments);
	    if(!value && cellProperties.placeholder) {
	    	value = cellProperties.placeholder;
	    }
	    var cellData=ht.getCellData(row,col),
	    	content;
	    applySpanProperties(TD,cellData);
	    if(!cellData){
	    	return;
	    }
	    cellData.container=TD;
	    content=ht.getCellContent(cellData)
	    if(content instanceof urule.CellCondition){
		    $(TD).empty();

	    	//$(TD).css("background-color","rgb(253, 252, 233)");
	    	var disContainer=content.getDisplayContainer();
	    	$(TD).append($("<div/>").append(disContainer));
	    }else if(content instanceof urule.CellContent){
	    	//$(TD).append(content.inputType.getContainer());
	    }else{
	    	//$(TD).append(content.container);
	    }
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
  