 /**
 * written by renny && ivan
 */
 grammar ANA;

 @header {
 package com.rappi.fraud.analang;
 }

 parse
 : ( workflow | error) EOF
 ;

 error: UNEXPECTED_CHAR
 {
  throw new RuntimeException("UNEXPECTED_CHAR= $UNEXPECTED_CHAR.text");
 }
 ;

 workflow
 : 'workflow' STRING_LITERAL 'ruleset' STRING_LITERAL stmt_list 'end'
 ;

 stmt_list
 : stmt* default_stmt
 ;

 stmt
 : cond 'return' result_value
 ;

 default_stmt
 : 'default' 'return' result_value
 ;



 cond
 : deep_id ( LT | LT_EQ | GT | GT_EQ ) NUMERIC_LITERAL
 | deep_id ( EQ | NOT_EQ1 | NOT_EQ2 | K_IS | K_IS K_NOT | K_IN ) STRING_LITERAL
 | cond K_AND cond
 | cond K_OR cond
 | list_op
 | count
 | average
 ;


 deep_id: ID | ID (DOT deep_id)+;

 list_op: deep_id DOT (K_ANY | K_ALL ) '{' cond '}';

 count: deep_id DOT K_COUNT '{' cond '}' ( LT | LT_EQ | GT | GT_EQ ) NUMERIC_LITERAL;

 average: deep_id DOT K_AVERAGE '{' cond '}' ( LT | LT_EQ | GT | GT_EQ ) NUMERIC_LITERAL;

 result_value
 : STRING_LITERAL
 ;

 any_name
 : ID
 | STRING_LITERAL
 | '(' any_name ')'
 ;

 literal_value
  : NUMERIC_LITERAL
  | STRING_LITERAL
 ;

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
 R_BRACE : '{';
 L_BRACE : '}';

 K_WORKFLOW: W O R K F L O W;
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