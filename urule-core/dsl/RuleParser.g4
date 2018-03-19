grammar RuleParser;

import RuleLexer;

ruleSet : ruleSetHeader
		  ruleSetBody
		;

ruleSetHeader : resource*
			  | functionImport*
			  | resource* functionImport*
			  | functionImport* resource*
			  ;
			  
ruleSetBody : rules* ;

rules : ruleDef | loopRuleDef ;

functionImport : 'import' packageDef';'?;

packageDef : Identifier
		   | Identifier('.'Identifier)+
		   | packageDef'.*';

resource : importVariableLibrary
		 | importActionLibrary
		 | importConstantLibrary
		 | importParameterLibrary
		  ;
		  
importParameterLibrary : 'importParameterLibrary' STRING ';'?;

importVariableLibrary : 'importVariableLibrary' STRING ';'?;

importConstantLibrary : 'importConstantLibrary' STRING ';'?;

importActionLibrary : 'importActionLibrary' STRING ';'?;

functionDef : 'function' Identifier '(' functionParameters? ')' '{' expressionBody '}'';'?;

functionParameters : functionParameter (','functionParameter)* ;

functionParameter : Datatype Identifier ;

ruleDef : ('rule'|'\u89c4\u5219') STRING
		  attribute*
		  left
		  right
		  other?
		  ('end'|'\u7ed3\u675f') ';'?
		  ;
		  
loopRuleDef : ('loopRule' | '\u5faa\u73af\u89c4\u5219') STRING
		  attribute*
		  loopTarget
		  loopStart?
		  left
		  right
		  other?
		  loopEnd?
		  ('end'|'\u7ed3\u675f') ';'?
		  ;
		  
loopTarget : ('loopTarget' | '\u5faa\u73af\u5bf9\u8c61') complexValue ;

loopStart : ('loopStart' | '\u5f00\u59cb\u524d\u52a8\u4f5c') action* ;		

loopEnd : ('loopEnd' | '\u7ed3\u675f\u540e\u52a8\u4f5c') action*;
		  
	
attribute :loopAttribute 
		  | salienceAttribute
		  | effectiveDateAttribute
		  | expiresDateAttribute
		  | enabledAttribute
		  | debugAttribute
		  | activationGroupAttribute
		  | agendaGroupAttribute
		  | autoFocusAttribute
		  | ruleflowGroupAttribute
		  ;

loopAttribute : ('loop' | '\u5141\u8bb8\u5faa\u73af\u89e6\u53d1') '=' Boolean ','?;
salienceAttribute : ('salience' | '\u4f18\u5148\u7ea7') '=' NUMBER ','?;
effectiveDateAttribute : ('effective-date' | '\u751f\u6548\u65f6\u95f4' | '\u751f\u6548\u65e5\u671f') '=' STRING ','?;
expiresDateAttribute : ('expires-date' | '\u5931\u6548\u65f6\u95f4' | '\u5931\u6548\u65e5\u671f') '=' STRING ','?;
enabledAttribute : ('enabled' | '\u6fc0\u6d3b' | '\u542f\u7528') '=' Boolean ','?;
debugAttribute : ('debug' | '\u8c03\u8bd5' | '\u5141\u8bb8\u8c03\u8bd5') '=' Boolean ','?;
activationGroupAttribute : ('activation-group' | '\u6fc0\u6d3b\u7ec4') '=' STRING ','? ;
agendaGroupAttribute : ('agenda-group' | '\u8bae\u7a0b\u7ec4') '=' STRING ','? ;
autoFocusAttribute : ('auto-focus' | '\u81ea\u52a8\u83b7\u53d6\u7126\u70b9') '=' Boolean ','?;
ruleflowGroupAttribute : ('ruleflow-group' | '\u89c4\u5219\u6d41\u7ec4') '=' STRING ','?;

left : 
	   ('if'|'\u5982\u679c')
	   condition?						
	 ;
	 

condition : leftParen condition rightParen	#parenConditions
		  | condition (join condition)+		#multiConditions
		  |	conditionLeft op (complexValue|nullValue)	#singleCondition
		  | namedConditionSet	#singleNamedConditionSet 
		  ;
		  
namedConditionSet : (refName colon)? refObject leftParen namedCondition rightParen;

namedCondition : leftParen namedCondition rightParen	#parenNamedConditions
			   | namedCondition (join namedCondition)+ 	#multiNamedConditions
			   | property op (complexValue|nullValue)	#singleNamedConditions
			   ;


decisionTableCellCondition : op (complexValue|nullValue)										#singleCellCondition
						   | decisionTableCellCondition (join decisionTableCellCondition)+		#multiCellConditions
						   | leftParen decisionTableCellCondition rightParen					#parenCellConditions
						   ;

refName : Identifier ;

refObject : variableCategory | parameterName ;


nullValue : 'null';

conditionLeft : (variable|parameter|functionInvoke|methodInvoke|expEval|expAll|expExists|expCollect|commonFunction) (ARITH value)* ;

expEval : 'eval'leftParen expressionBody rightParen;

expAll : 'all'leftParen 
		(variable|parameter) 
		',' exprCondition
		(',' (NUMBER|percent))?
		rightParen;
		
expExists : 'exist'leftParen 
			(variable|parameter) 
			',' exprCondition
			(',' (NUMBER|percent))?
			rightParen;
		
expCollect : 'collect'leftParen 
			(variable|parameter) 
			(',' exprCondition)?
			rightParen
			'.'
			( COUNT | property'.'(SUM|AVG|MAX|MIN))
			;

commonFunction : Identifier leftParen complexValue(','property)? rightParen ;

		
exprCondition : property op (complexValue|nullValue)
			  | exprCondition (join exprCondition)+
			  ;

expressionBody : .*? ;

percent : NUMBER'%';

leftParen : '(';

rightParen : ')';

colon : ':';


join : 
	   AND
	 | OR
	 ;
			 
right : ('then'|'\u90a3\u4e48')
		action*
		;
other : ('else'|'\u5426\u5219')
		action*
		;
		
actions : action*;

action : assignAction ';'?
	   | outAction ';'?
	   | methodInvoke ';'?
	   | functionInvoke ';'?
	   | commonFunction ';'?
	   ;

assignAction : (variable|namedVariable|parameter) '=' complexValue;

outAction : 'out''(' complexValue ')';

methodInvoke : beanMethod'('actionParameters?')';

functionInvoke : '@'Identifier'(' actionParameters? ')';

actionParameters : complexValue (',' complexValue)* ;

beanMethod :  Identifier'.'Identifier ;

complexValue : value
			 | variable
			 | namedVariable
			 | constant
			 | variableCategory
			 | parameter
			 | methodInvoke
			 | functionInvoke
			 | commonFunction
			 | leftParen complexValue rightParen
			 | complexValue (ARITH complexValue)+
			 ;

parameter : parameterName'.'Identifier;

parameterName : 'parameter'|'\u53c2\u6570' ;

constant : constantCategory'.'property;

variable : variableCategory'.'property;

namedVariable : namedVariableCategory'.'property;

property : Identifier('.'Identifier)*;

variableCategory : Identifier;

namedVariableCategory : '!'Identifier;

constantCategory : '$'Identifier;

value : 
		STRING
	  | NUMBER
	  | Boolean
	  ;
op
:
	  GreaterThen
	| GreaterThenOrEquals
	| LessThen
	| LessThenOrEquals
	| Equals
	| NotEquals
	| EndWith
	| NotEndWith
	| StartWith
	| NotStartWith
	| In
	| NotIn
	| Match
	| NotMatch
	| EqualsIgnoreCase
	| NotEqualsIgnoreCase
	| Contain
	| NotContain
;	  
