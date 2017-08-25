import CodeMirror from 'codemirror';

var Pos = CodeMirror.Pos;
function forEach(arr, f) {
for (var i = 0, e = arr.length; i < e; ++i){
	f(arr[i])
};
}

function arrayContains(arr, item) {
if (!Array.prototype.indexOf) {
	var i = arr.length;
	while (i--) {
		if (arr[i] === item) {
			return true;
		}
	}
	return false;
}
return arr.indexOf(item) != -1;
}

function getMode(editor){
var cur=editor.getCursor();
var mode=editor.getModeAt({line:cur.line,ch:cur.ch});
return mode;
}

function getBlock(){
return "if";
}

function scriptHint(editor, keywords, getToken, options) {
var cur = editor.getCursor(),
	token = getToken(editor, cur),
	comment=/\b(?:string|comment)\b/,
	property=/^[\w$_\u4e00-\u9fa5]*$/;
if (comment.test(token.type)) return;
token.state = CodeMirror.innerMode(editor.getMode(), token.state).state;
if (token.type=="property") {
	token = {
		start: cur.ch-token.string.length+1,
		end: cur.ch+token.string.length-1,
		string: token.string.replace(".",""),
		state: token.state
	};
}else if(!property.test(token.string)){
	token = {
			start: cur.ch,
			end: cur.ch,
			string: "",
			state: token.state
		};
}else if (token.end > cur.ch) {
	token.end = cur.ch;
	token.string = token.string.slice(0, cur.ch - token.start);
}
var tprop = token;
while (true) {
	tprop = getToken(editor, Pos(cur.line, tprop.start));
	if (tprop.type != "property") break;
	tprop = getToken(editor, Pos(cur.line, tprop.start));
	if (!context){
		var context = [];
	}
	context.push(tprop);
}
return {list: getCompletions( token, context, keywords, options),
	from: Pos(cur.line, token.start),
	to: Pos(cur.line, token.end)};
}

function uruleHint(editor, options) {
	var mode=getMode(editor);
	var block=mode.name, keywords;
	if(block=="if"){
		keywords=ifKeywords;
	}else if(block=="then"){
		keywords=thenKeywords;
	}else if(block=="print"){
		keywords=printKeywords;
	}
	return scriptHint(editor, keywords,
			  function (e, cur) {return e.getTokenAt(cur);},
			  options);
};
CodeMirror.registerHelper("hint", "if", uruleHint);

var ifKeywords = ( "参数 大于 大于等于 小于 小于等于 等于 不等于 结束于 不结束于 开始于 不开始于 在集合中 不在集合中 匹配 不匹配 忽略大小写等于 忽略大小写不等").split(" ");
var thenKeywords = ( "参数").split(" ");
var printKeywords = ( "参数").split(" ");


function getCompletions(token, context, keywords, options) {
	var found = [], start = token.string, global = options && options.globalScope || window;
	function maybeAdd(str) {
		str = str || "";
		if (str.toUpperCase().lastIndexOf(start.replace(/\s*/,"").toUpperCase(), 0) == 0 && !arrayContains(found, str)) found.push(str);
	}
	function gatherCompletions(base) {
		var	actionLibraries=window._uruleEditorActionLibraries||[],
			parameter = window._uruleEditorParameterLibraries || [],
			variableCategories=window._uruleEditorVariableLibraries||[],
			constantCategories=window._uruleEditorConstantLibraries||[];
		$.each(actionLibraries, function(index, library){
			var springBeans=library.springBeans||[];
			for(var j=0;j<springBeans.length;j++){
				var springBean=springBeans[j],
					methods=springBean.methods||[];
				if(springBean.name.toUpperCase().lastIndexOf(base.toUpperCase(), 0) != 0)continue;
				for(var k=0;k<methods.length;k++){
					var method=methods[k],
						parameters=method.parameters||[],
						name=method.name+"(",
						ps=[];
					for(var z=0;z<parameters.length;z++){
						var parameter=parameters[z];
						ps.push(parameter.name);
					}

					name+=ps.join(",")+")";
					maybeAdd(name);
				}
			}
		});
		$.each(variableCategories, function(index, categories){
			for(var i=0;i<categories.length;i++){
				var variableCategory=categories[i],
					variables=variableCategory.variables||[];
				if(variableCategory.name.toUpperCase().lastIndexOf(base.toUpperCase(), 0) != 0)continue;
				for(var j=0;j<variables.length;j++){
					var variable=variables[j],
						name=variable.label;
					maybeAdd(name);
				}
			}
		});
		$.each(constantCategories, function(index, categories){
			categories = categories.categories;
			for(var i=0;i<categories.length;i++){
				var constantCategory=categories[i],
					constants=constantCategory.constants||[],
					name="$"+constantCategory.label;
				if(name.toUpperCase().lastIndexOf(base.toUpperCase(), 0) != 0)continue;
				for(var j=0;j<constants.length;j++){
					var constant=constants[j],
						name=constant.label;
					maybeAdd(name);
				}
			}
		});

		$.each(parameter, function(index, ps){
			if("参数" == base) {
				for(var i=0;i<ps.length;i++){
					var p=ps[i],
						name=p.label;
					maybeAdd(name);
				}
			}
		});
	}

	if (context && context.length) {
		var obj = context.pop(), base;
		if (obj.type && (obj.type.indexOf("variable") === 0||obj.type.indexOf("atom-2")===0)) {
			base = obj.string;
			if(base){
				if(base=="参数"){
					gatherCompletions("参数");
				}else{
					gatherCompletions(base);
				}
			}
		}
	} else {
		var	actionLibraries=window._uruleEditorActionLibraries||[],
			variableCategories=window._uruleEditorVariableLibraries||[],
			constantCategories=window._uruleEditorConstantLibraries||[];
		$.each(actionLibraries, function(index, library){
			var springBeans=library.springBeans;
			for(var j=0;j<springBeans.length;j++){
				var springBean=springBeans[j],
					name=springBean.name;
				maybeAdd(name);
			}
		});


		$.each(variableCategories, function(index, categories){
			for(var i=0;i<categories.length;i++){
				var variableCategory=categories[i],
					name=variableCategory.name;
				maybeAdd(name);
			}
		});


		$.each(constantCategories, function(index, categories){
			categories = categories.categories;
			for(var i=0;i<categories.length;i++){
				var constantCategory=categories[i],
					name="$"+constantCategory.label;
				maybeAdd(name);
			}
		});

		forEach(keywords, maybeAdd);


	}
	return found;
}
