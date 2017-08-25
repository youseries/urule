/**
 * Created by jacky on 2016/7/21.
 */
import CodeMirror from 'codemirror';
import '../../node_modules/codemirror/addon/mode/simple.js';
import {Event} from 'flowdesigner';
import * as Constants from './Constants.js';

CodeMirror.defineSimpleMode("if", {
    // The start state contains the rules that are intially used
    start: [
        // The regex matches the token, the token property contains the type
        {regex: /"(?:[^\\]|\\.)*?"/, token: "string"},
        {regex: /(true|false|null|and|or)\b/, token: "atom"},
        {regex: /\s+(\u6216\u8005|\u6216|\u5E76\u4E14|\u4E14)\s+/, token: "atom"},
        {regex: /\u53C2\u6570/, token: "atom-2"},
        {regex: /(out|eval)\b/, token: "atom-3"},
        {regex: /\.([\w$_\u4e00-\u9fa5][\w$_\u4e00-\u9fa5\d]*)*/, token: "property"},
        {regex: /0x[a-f\d]+|[-+]?(?:\.\d+|\d+\.?\d*)(?:e[-+]?\d+)?/i, token: "number"},
        {regex: /\/\/.*/, token: "comment"},
        {regex: /\/(?:[^\\]|\\.)*?\//, token: "comment"},
        // A next property will cause the mode to move to a different state
        {regex: /\/\*/, token: "comment", next: "comment"},
        {regex: /[-+\/*=<>!]+/, token: "operator"},
        {regex: /(Endwith|NotEndwith|Startwith|NotStartwith|In|NotIn|Match|NotMatch|'EqualsIgnoreCase|NotEqualsIgnoreCase)\b/, token: "operator"},

        // indent and dedent properties guide autoindentation
        {regex: /[\{\[\(]/, indent: true},
        {regex: /[\}\]\)]/, dedent: true},
        {regex: /[\w$_\u4e00-\u9fa5][\w$_\u4e00-\u9fa5\d]*/, token: "variable"},
        // You can embed other modes with the mode property. This rule
        // causes all code between << and >> to be highlighted with the XML
        // mode.
        {regex: /<</, token: "meta", mode: {spec: "xml", end: />>/}}
    ],
    // The multi-line comment state.
    comment: [
        {regex: /.*?\*\//, token: "comment", next: "start"},
        {regex: /.*/, token: "comment"}
    ],
    // The meta property contains global information about the mode. It
    // can contain properties like lineComment, which are supported by
    // all modes, and also directives like dontIndentStates, which are
    // specific to simple modes.
    meta: {
        dontIndentStates: ["comment"],
        lineComment: "//"
    }
});

CodeMirror.defineSimpleMode("then", {
    // The start state contains the rules that are intially used
    start: [
        // The regex matches the token, the token property contains the type
        {regex: /"(?:[^\\]|\\.)*?"/, token: "string"},
        {regex: /(true|false|null)\b/, token: "atom"},
        {regex: /\u53C2\u6570/, token: "atom-2"},
        {regex: /(out)\b/, token: "atom-3"},
        {regex: /\.([\w$_\u4e00-\u9fa5][\w$_\u4e00-\u9fa5\d]*)*/, token: "property"},
        {regex: /0x[a-f\d]+|[-+]?(?:\.\d+|\d+\.?\d*)(?:e[-+]?\d+)?/i, token: "number"},
        {regex: /\/\/.*/, token: "comment"},
        {regex: /\/(?:[^\\]|\\.)*?\//, token: "comment"},
        // A next property will cause the mode to move to a different state
        {regex: /\/\*/, token: "comment", next: "comment"},
        {regex: /[-+\/*]+/, token: "operator"},
        // indent and dedent properties guide autoindentation
        {regex: /[\{\[\(]/, indent: true},
        {regex: /[\}\]\)]/, dedent: true},
        {regex: /[\w$_\u4e00-\u9fa5][\w$_\u4e00-\u9fa5\d]*/, token: "variable"},
        // You can embed other modes with the mode property. This rule
        // causes all code between << and >> to be highlighted with the XML
        // mode.
        {regex: /<</, token: "meta", mode: {spec: "xml", end: />>/}}
    ],
    // The multi-line comment state.
    comment: [
        {regex: /.*?\*\//, token: "comment", next: "start"},
        {regex: /.*/, token: "comment"}
    ],
    // The meta property contains global information about the mode. It
    // can contain properties like lineComment, which are supported by
    // all modes, and also directives like dontIndentStates, which are
    // specific to simple modes.
    meta: {
        dontIndentStates: ["comment"],
        lineComment: "//"
    }
});

//hint start...
Event.eventEmitter.on(Constants.LIB_CHANGE,function(lib){
    Constants.LIBRARY=lib;
});

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
    }
    return scriptHint(editor, keywords, function (e, cur) {return e.getTokenAt(cur);}, options);
};

CodeMirror.registerHelper("hint", "if", uruleHint);

var ifKeywords = ( "参数 > >= < <= == != Endwith NotEndwith Startwith NotStartwith In NotIn Match NotMatch EqualsIgnoreCase NotEqualsIgnoreCase eval()").split(" ");
var thenKeywords = ( "参数 out").split(" ");

function getCompletions(token, context, keywords, options) {
    var found = [], start = token.string, global = options && options.globalScope || window;
    function maybeAdd(str) {
        if (str.toUpperCase().lastIndexOf(start.replace(/\s*/,"").toUpperCase(), 0) == 0 && !arrayContains(found, str)) found.push(str);
    }
    function gatherCompletions(base) {
        const {actionLibraries,variableCategories,constantCategories}=Constants.LIBRARY;
        for(let actionLibrary of actionLibraries){
            const springBeans=actionLibrary.springBeans || [];
            for(let springBean of springBeans){
                const methods=springBean.methods || [];
                if(springBean.name.toUpperCase().lastIndexOf(base.toUpperCase(), 0) != 0)continue;
                for(let method of methods){
                    let parameters=method.parameters || [], name=method.name+"(", ps=[];
                    for(var z=0;z<parameters.length;z++){
                        let parameter=parameters[z];
                        ps.push(parameter.name);
                    }
                    name+=ps.join(",")+")";
                    maybeAdd(name);
                }
            }
        }

        for(let vc of variableCategories){
            const variables=vc.variables || [];
            if(vc.name.toUpperCase().lastIndexOf(base.toUpperCase(), 0) != 0)continue;
            for(let v of variables){
                const name=v.label;
                maybeAdd(name);
            }
        }

        for(let cc of constantCategories){
            const constants=cc.constants || [], name="$"+cc.label;
            if(name.toUpperCase().lastIndexOf(base.toUpperCase(), 0) != 0)continue;
            for(let constant of constants){
                const name=constant.label;
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
        const {actionLibraries,variableCategories,constantCategories}=Constants.LIBRARY;
        for(let actionLibrary of actionLibraries){
            const springBeans=actionLibrary.springBeans;
            for(let springBean of springBeans){
                const name=springBean.name;
                maybeAdd(name);
            }
        }

        for(let vc of variableCategories){
            const name=vc.name;
            if(name !== "参数") {
                maybeAdd(name);
            }
        }

        for(let cc of constantCategories){
            const name="$"+cc.label;
            maybeAdd(name);
        }
        forEach(keywords, maybeAdd);
    }
    return found;
}

export default CodeMirror;