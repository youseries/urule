lexer grammar RuleLexer;

COUNT : 'count';

AVG : 'avg';

SUM : 'sum';

MAX : 'max';

MIN : 'min';

AND : 'and'|'&&'|','|'\u5e76\u4e14'|'\u4e14';
OR : 'or'|'||'|'\u6216\u8005'|'\u6216';

Datatype : 'String'
		 | 'int'
		 | 'Integer'
		 | 'double'
		 | 'Double'
		 | 'long'
		 | 'Long'
		 | 'float'
		 | 'Float'
		 | 'BigDecimal'
		 | 'boolean'
		 | 'Boolean'
		 | 'Date'
		 | 'List'
		 | 'Set'
		 | 'Map'
		 | 'Enum'
		 | 'Object'
		 ;

GreaterThen : '>'|'\u5927\u4e8e';

GreaterThenOrEquals : '>='|'\u5927\u4e8e\u7b49\u4e8e';

LessThen : '<'|'\u5c0f\u4e8e';

LessThenOrEquals : '<='|'\u5c0f\u4e8e\u7b49\u4e8e';

Equals : '=='|'\u7b49\u4e8e';

NotEquals : '!='|'\u4e0d\u7b49\u4e8e';

EndWith : 'EndWith'|'\u7ed3\u675f\u4e8e';

NotEndWith : 'NotEndWith'|'\u4e0d\u7ed3\u675f\u4e8e' ;

StartWith : 'StartWith'|'\u5f00\u59cb\u4e8e';

NotStartWith : 'NotStartWith'|'\u4e0d\u5f00\u59cb\u4e8e';

In : 'In'|'\u5728\u96c6\u5408\u4e2d';

NotIn : 'NotIn'|'\u4e0d\u5728\u96c6\u5408\u4e2d';

Match : 'Match'|'\u5339\u914d';

NotMatch : 'NotMatch'|'\u4e0d\u5339\u914d';

Contain : 'Contain'|'\u5305\u542b';

NotContain : 'NotContain'|'\u4e0d\u5305\u542b';

EqualsIgnoreCase : 'EqualsIgnoreCase'|'\u5ffd\u7565\u5927\u5c0f\u5199\u7b49\u4e8e';

NotEqualsIgnoreCase : 'NotEqualsIgnoreCase'|'\u5ffd\u7565\u5927\u5c0f\u5199\u4e0d\u7b49\u4e8e';

ARITH
:
	'+'
	| '-'
	| '*'
	| '/'
	| '%'
;

NUMBER
:
	'-'? INT '.' INT EXP? // ('-'? INT '.' INT EXP?)1.35, 1.35E-9, 0.3, -4.5
	| '-'? INT EXP // 1e10 -3e4
	| '-'? INT // -3, 45
;

Boolean : 'true'
		| 'false'
		;

Identifier
:
	StartChar Char* 
;

STRING :  
	    '"' STRING_CONTENT '"'
;


fragment
STRING_CONTENT : 
	( EscapeSequence | ~('"'))*
;


fragment
INT
:
    DIGIT+
;

fragment
EXP
:
	[Ee] [+\-]? INT
; // \- since - means "range" inside [...]

fragment
EscapeSequence
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UnicodeEscape
    |   OctalEscape
    ;

fragment
OctalEscape
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UnicodeEscape
:
	'\\' 'u' HEX HEX HEX HEX
;

fragment
Char    :   StartChar
            |   '-' | '_' | DIGIT 
            |   '\u00B7'
            |   '\u0300'..'\u036F'
            |   '\u203F'..'\u2040'
            ;

fragment
StartChar
            :   [a-zA-Z]
            |   '\u2070'..'\u218F' 
            |   '\u2C00'..'\u2FEF' 
            |   '\u3001'..'\uD7FF' 
            |   '\uF900'..'\uFDCF' 
            |   '\uFDF0'..'\uFFFD'
            ;

fragment
DIGIT
:
    [0-9]
;

fragment
HEX
:
	[0-9a-fA-F]
;

WS
:
	[ \t\r\n]+ -> channel(HIDDEN)
;
 
NL
:
    '\r'? '\n' ->channel(HIDDEN)
;
 

COMMENT
    :   '/*' .*? '*/'    ->channel(HIDDEN);

LINE_COMMENT
    : '//' ~[\r\n]* '\r'? '\n' ->channel(HIDDEN)
    ;    


