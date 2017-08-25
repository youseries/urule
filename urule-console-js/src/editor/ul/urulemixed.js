
/**
 * @author GJ
 */
import CodeMirror from 'codemirror';

CodeMirror.defineMode("rulemixed", function(config, parserConfig) {
  var uruleMode = CodeMirror.getMode(config, "urule");
  var javaMode = CodeMirror.getMode(config, "text/x-java");
  uruleMode.block="import";
  function urule(stream, state) {
    var style = uruleMode.token(stream, uruleMode.startState()),
    	cur=stream.current();
    if ("function"==cur){
    	 state.token = java;
         state.localMode = javaMode;
         state.localState = javaMode.startState()
    }else if("rule"==cur||"规则"==cur){
    	state.block="rule";
    }else if("loopRule"==cur||"循环规则"==cur){
      state.block="rule";
    }else if("loopStart"==cur||"循环开始前动作"==cur){
      state.block="then";
    }else if("loopTarget"==cur||"循环对象"==cur){
      state.block="if";
    }else if("if"==cur||"如果"==cur){
      state.block="if";
    }else if("then"==cur||"那么"==cur){
    	state.block="then";
    }else if("loopEnd"==cur||"循环结束后动作"==cur){
      state.block="then";
    }else if("end"==cur||"结束"==cur){
    	state.block="end";
    }else if(/^(importVariableLibrary|importConstantLibrary|importActionLibrary)$/.test(cur)){
    	state.block="import";
    }
    return style;
  };
  
  function java(stream, state) {
	var style=state.localMode.token(stream, state.localState),
		cur=stream.current();
    if (/rule|\u89C4\u5219/.test(cur)) {
      state.token = urule;
      state.localState = state.localMode = null;
      stream.backUp(cur.length);
      uruleMode.bloack="rule";
      return null;
    } if (/function/.test(cur)){
   	  state.token = urule;
   	  state.localState = state.localMode = null;
      stream.backUp(cur.length);
      return null;
    }
    return style;
  };
 
  return {
    startState: function() {
      var state = uruleMode.startState();
      return {token: urule, localMode: null, localState: null, uruleState: state};
    },

    copyState: function(state) {
      if (state.localState)
        var local = CodeMirror.copyState(state.localMode, state.localState);
      return {token: state.token, localMode: state.localMode, localState: local,
              uruleState: CodeMirror.copyState(uruleMode, state.uruleState),block:state.block};
    },

    token: function(stream, state) {
      return state.token(stream, state);
    },

    indent: function(state, textAfter) {
      if (!state.localMode || /^\s*<\//.test(textAfter))
        return uruleMode.indent(state.uruleState, textAfter);
      else if (state.localMode.indent)
        return state.localMode.indent(state.localState, textAfter);
      else
        return CodeMirror.Pass;
    },

    innerMode: function(state) {
      return {state: state.localState || state.uruleState, mode: state.localMode || uruleMode};
    }
  };
}, "urule", "text/x-java");
