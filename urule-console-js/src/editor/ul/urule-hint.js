/**
 * @author GJ
 */
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
	var block=null,line=codeMirror.getCursor().line;
	while(!block&&line>=0){
		block=codeMirror.getStateAfter(line).block;
		line--;
	}
	if(line<0){
		block="import";
	}
	return block;
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
	return {list: getCompletions(token, context, keywords, options),
		from: Pos(cur.line, token.start),
		to: Pos(cur.line, token.end)};
	}

	function uruleHint(editor, options) {
		var keywords=javaKeywords,
			mode=getMode(editor);
		if(mode.name=="urule"){
			var block=getBlock();
			if(block=="import"){
				keywords=importKeywords;
			}else if(block=="rule"){
				keywords=ruleKeywords;
			}else if(block=="if"){
				keywords=ifKeywords;
			}else if(block=="then"){
				keywords=thenKeywords;
			}else if(block=="end"){
				keywords=endKeywords;
			}
		}
		return scriptHint(editor, keywords,
				  function (e, cur) {return e.getTokenAt(cur);},
				  options);
	};
	CodeMirror.registerHelper("hint", "urule", uruleHint);

	var importKeywords=( "importVariableLibrary importConstrantLibrary importActionLibrary import function").split(" ");
	var ruleKeywords = ( "salience effective-date expires-date enabled debug activation-group agenda-group auto-focus ruleflow-group").split(" ");
	var ifKeywords = ( "参数 > >= < <= == != Endwith NotEndwith Startwith NotStartwith In NotIn Match NotMatch EqualsIgnoreCase NotEqualsIgnoreCase Contain NotContain eval()").split(" ");
	var thenKeywords = ( "参数 out").split(" ");
	var endKeywords = ( "function").split(" ");
	var javaKeywords=("abstract assert boolean break byte case catch char class const continue default " +
				"do double else enum extends final finally float for goto if implements import " +
				"instanceof int interface long native new package private protected public " +
				"return short static strictfp super switch synchronized this throw throws transient " +
				"try void volatile while").split(" ");

	function getCompletions(token, context, keywords, options) {
		var found = [], start = token.string, global = options && options.globalScope || window;
		function maybeAdd(str) {
			if (str.toUpperCase().lastIndexOf(start.replace(/\s*/,"").toUpperCase(), 0) == 0 && !arrayContains(found, str)) found.push(str);
		}
		function gatherCompletions(base) {
			var block=getBlock();
			if(!codeMirror._library||(block!="if"&&block!="then"))return;
			var library=codeMirror._library,
				actionLibraries=library.actionLibraries||[],
				variableCategories=library.variableCategories||[],
				constantCategories=library.constantCategories||[];
			for(var i=0;i<actionLibraries.length;i++){
				var actionLibrary=actionLibraries[i],
					springBeans=actionLibrary.springBeans||[];
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
			}

			for(var i=0;i<variableCategories.length;i++){
				var variableCategorie=variableCategories[i],
					variables=variableCategorie.variables||[];
				if(variableCategorie.name.toUpperCase().lastIndexOf(base.toUpperCase(), 0) != 0)continue;
				for(var j=0;j<variables.length;j++){
					var variable=variables[j],
						name=variable.label;
					maybeAdd(name);
				}
			}

			for(var i=0;i<constantCategories.length;i++){
				var constantCategory=constantCategories[i],
					constants=constantCategory.constants||[],
					name="$"+constantCategory.label;
				if(name.toUpperCase().lastIndexOf(base.toUpperCase(), 0) != 0)continue;
				for(var j=0;j<constants.length;j++){
					var constant=constants[j],
						name=constant.label;
					maybeAdd(name);
				}
			}

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
			var block=getBlock();
			if(codeMirror._library&&(block == "if" || block == "then")){
				var library=codeMirror._library,
					actionLibraries=library.actionLibraries,
					variableCategories=library.variableCategories,
					constantCategories=library.constantCategories;
				for(var i=0;i<actionLibraries.length;i++){
					var actionLibrary=actionLibraries[i],
						springBeans=actionLibrary.springBeans;
					for(var j=0;j<springBeans.length;j++){
						var springBean=springBeans[j],
							name=springBean.name;
						maybeAdd(name);
					}
				}

				for(var i=0;i<variableCategories.length;i++){
					var variableCategorie=variableCategories[i],
						name=variableCategorie.name;
					if(name != "参数") {
						maybeAdd(name);
					}
				}

				for(var i=0;i<constantCategories.length;i++){
					var constantCategorie=constantCategories[i],
						name="$"+constantCategorie.label;
					maybeAdd(name);
				}
			}
			forEach(keywords, maybeAdd);
		}
		return found;
	}