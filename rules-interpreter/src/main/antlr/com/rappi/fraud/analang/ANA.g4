 /**
 * written by renny && ivan
 */
 grammar ANA;

 @header {
 package com.rappi.fraud.analang;
 }

 parse
 : (workflow | error) EOF
 ;

 error: UNEXPECTED_CHAR
 {
  throw new RuntimeException("UNEXPECTED_CHAR= $UNEXPECTED_CHAR.text");
 }
 ;

 workflow
 : K_WORKFLOW workflowName = STRING_LITERAL rulesets* defaultResult = default_result K_END
 ;

 rulesets
 : K_RULESET ruleSetName = STRING_LITERAL rules*
 ;

 rules
 : name cond K_RETURN result = state actions?
 ;

 default_result
 : K_DEFAULT result = state
 ;

 state
 : ('allow'|'block'|'prevent')
 ;

 actions
 : K_WITH action (K_AND action)*?
 ;

 action
 : ('manual_review')
 ;

 name : ID;

 cond
 : field op = (EQ | NOT_EQ1 | NOT_EQ2) value = STRING_LITERAL                                       #string
 | field op = (LT | LT_EQ | GT | GT_EQ) value = NUMERIC_LITERAL                                     #number
 | left = cond op = (K_AND | K_OR) right = cond                                                     #logical
 | field DOT op = (K_ANY | K_ALL) L_BRACE cond R_BRACE                                              #list
 | field DOT K_COUNT L_BRACE cond R_BRACE op = (LT | LT_EQ | GT | GT_EQ) value = NUMERIC_LITERAL    #count
 | field DOT K_AVERAGE L_BRACE cond R_BRACE op = (LT | LT_EQ | GT | GT_EQ) value = NUMERIC_LITERAL  #average
 | L_PAREN cond R_PAREN                                                                             #parens
 ;

 field: ID | ID (DOT field)+;

 DOT : '.';
 COMMA : ',';
 STAR : '*';
 PLUS : '+';
 MINUS : '-';
 TILDE : '~';
 LT : '<';
 LT_EQ : '<=';
 GT : '>';
 GT_EQ : '>=';
 EQ : '=';
 NOT_EQ1 : '!=';
 NOT_EQ2 : '<>';
 L_BRACE : '{';
 R_BRACE : '}';
 L_PAREN: '(';
 R_PAREN: ')';

 K_WORKFLOW: W O R K F L O W;
 K_RULESET: R U L E S E T;
 K_RETURN: R E T U R N;
 K_DEFAULT: D E F A U L T;
 K_WITH: W I T H;
 K_END: E N D;
 K_ELSE: E L S E;
 K_AND: A N D;
 K_OR: O R;
 K_CONTAINS: C O N T A I N S;
 K_NOT_CONTAINS: K_NOT K_CONTAINS;
 K_IS: I S;
 K_NOT: N O T;
 K_IS_NOT: K_IS K_NOT;
 K_IN: I N;
 K_ANY: A N Y;
 K_ALL: A L L;
 K_COUNT: C O U N T;
 K_AVERAGE: A V E R A G E;

 ID
 : [a-zA-Z_] [a-zA-Z_0-9]*
 | [a-zA-Z_] [a-zA-Z_0-9]*
 ;

 NUMERIC_LITERAL
  : DIGIT+ ( '.' DIGIT* )? ( E [-+]? DIGIT+ )?
  | '.' DIGIT+ ( E [-+]? DIGIT+ )?
  ;

 STRING_LITERAL
 : '\'' ( ~'\'' | '\'\'' )* '\''
 ;

 SINGLE_LINE_COMMENT
 : '--' ~[\r\n]* -> channel(HIDDEN)
 ;

 MULTILINE_COMMENT
 : '/*' .*? ( '*/' | EOF ) -> channel(HIDDEN)
 ;

 SPACES
 : [ \u000B\t\r\n] -> channel(HIDDEN)
 ;


 fragment DIGIT : [0-9];

 fragment A : [aA];
 fragment B : [bB];
 fragment C : [cC];
 fragment D : [dD];
 fragment E : [eE];
 fragment F : [fF];
 fragment G : [gG];
 fragment H : [hH];
 fragment I : [iI];
 fragment J : [jJ];
 fragment K : [kK];
 fragment L : [lL];
 fragment M : [mM];
 fragment N : [nN];
 fragment O : [oO];
 fragment P : [pP];
 fragment Q : [qQ];
 fragment R : [rR];
 fragment S : [sS];
 fragment T : [tT];
 fragment U : [uU];
 fragment V : [vV];
 fragment W : [wW];
 fragment X : [xX];
 fragment Y : [yY];
 fragment Z : [zZ];

 UNEXPECTED_CHAR
  : .
  ;