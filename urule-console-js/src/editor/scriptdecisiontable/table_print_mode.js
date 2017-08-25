/**
 * @author GJ
 */
import CodeMirror from 'codemirror';
CodeMirror.defineSimpleMode("print", {
  // The start state contains the rules that are intially used
  start: [
    // The regex matches the token, the token property contains the type
    {regex: /"(?:[^\\]|\\.)*?"/, token: "string"},
    {regex: /(true|false|null|and|or)\b/, token: "atom"},
    {regex: /\s+(\u6216\u8005|\u6216|\u5E76\u4E14)\s+/, token: "atom"},
    {regex: /\u53C2\u6570/, token: "atom-2"},
    {regex: /(out|eval)\b/, token: "atom-3"},
    {regex: /\.([\w$_\u4e00-\u9fa5][\w$_\u4e00-\u9fa5\d]*)*/, token: "property"},
    {regex: /0x[a-f\d]+|[-+]?(?:\.\d+|\d+\.?\d*)(?:e[-+]?\d+)?/i, token: "number"},
    {regex: /\/\/.*/, token: "comment"},
    {regex: /\/(?:[^\\]|\\.)*?\//, token: "comment"},
    // A next property will cause the mode to move to a different state
    {regex: /\/\*/, token: "comment", next: "comment"},
    {regex: /[-+\/*=<>!]+/, token: "operator"},
    {regex: /(\u5927\u4E8E|\u5927\u4E8E\u7B49\u4E8E|\u5C0F\u4E8E|\u5C0F\u4E8E\u7B49\u4E8E|\u7B49\u4E8E|\u4E0D\u7B49\u4E8E|\u7ED3\u675F\u4E8E|\u4E0D\u7ED3\u675F\u4E8E|\u5F00\u59CB\u4E8E|\u4E0D\u5F00\u59CB\u4E8E|\u5728\u96C6\u5408\u4E2D|\u4E0D\u5728\u96C6\u5408\u4E2D|\u5339\u914D|\u4E0D\u5339\u914D|\u5FFD\u7565\u5927\u5C0F\u5199\u7B49\u4E8E|\u5FFD\u7565\u5927\u5C0F\u5199\u4E0D\u7B49)\s+/, token: "operator"},

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