/**
 * @author GJ
 */
import CodeMirror from 'codemirror';

CodeMirror.defineSimpleMode("urule", {
  // The start state contains the rules that are intially used
  start: [
    // The regex matches the token, the token property contains the type
    {regex: /"(?:[^\\]|\\.)*?"/, token: "string"},
    // You can match multiple tokens at once. Note that the captured
    // groups must span the whole string in this case
    {regex: /(function)(\s+)([a-z$][\w$]*)/,token: ["keyword", null, "variable-2"]},
    // Rules are matched in the order in which they appear, so there is
    // no ambiguity between this one and the one above
    {regex: /(rule|loopRule|loopTarget|loopStart|loopEnd|if|then|else|end)\b/, token: "keyword"},
    {regex: /(\u89C4\u5219|\u5faa\u73af\u89c4\u5219|\u5faa\u73af\u5bf9\u8c61|\u5f00\u59cb\u524d\u52a8\u4f5c|\u7ed3\u675f\u540e\u52a8\u4f5c|\u5982\u679C|\u90A3\u4E48|\u5426\u5219|\u7ED3\u675F)/, token: "keyword"},
    {regex: /(true|false|null|and|or|importParameterLibrary|importVariableLibrary|importConstantLibrary|importActionLibrary|salience|loop|effective-date|expires-date|enabled|debug|activation-group|agenda-group|auto-focus)\b/, token: "atom"},
    {regex: /\s+(\u6216\u8005|\u6216|\u5E76\u4E14|\u4E14)\s+/, token: "atom"},
    {regex: /\u53C2\u6570/, token: "atom-2"},
    {regex: /(out|eval|all|exist|collect|count)\b/, token: "atom-3"},
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