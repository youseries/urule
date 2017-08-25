/**
 * @author GJ
 */
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