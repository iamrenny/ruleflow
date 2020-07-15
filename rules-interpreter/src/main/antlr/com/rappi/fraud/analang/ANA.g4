 grammar ANA;

 @header {
    package com.rappi.fraud.analang;
 }

 parse
 : (workflow | error) EOF
 ;

 error: UNEXPECTED_CHAR {
    throw new RuntimeException("UNEXPECTED_CHAR= $UNEXPECTED_CHAR.text");
 }
 ;

 workflow
 : K_WORKFLOW name rulesets* defaultResult = default_result K_END
 ;

 rulesets
 : K_RULESET name rules*
 ;

 rules
 : name expr K_RETURN result = state actions?
 ;

 name
 : STRING_LITERAL
 ;

 default_result
 : K_DEFAULT (K_RETURN)? result = state
 ;

 state: ('allow' | 'block' | 'prevent')
 ;

 actions
 : K_WITH action (K_AND action)*?
 ;

 action
 : ('manual_review')
 ;

 expr
 : L_PAREN expr R_PAREN                                                                                     #parenthesis
 | left = expr op = (ADD | SUBTRACT | MULTIPLY | DIVIDE) right = expr                                       #math
 | left = expr op = (LT | LT_EQ | GT | GT_EQ | EQ | EQ_IC | NOT_EQ) right = expr                            #comparator
 | value = expr op = (K_CONTAINS | K_IN) values = listElems                                                 #list
 | value = expr DOT op = (K_COUNT | K_AVERAGE | K_ANY | K_ALL | K_DISTINCT)
        (L_BRACE predicate = expr R_BRACE | L_PAREN R_PAREN)                                                #aggregation
 | DATE_DIFF L_PAREN (HOUR | DAY) COMMA left = expr COMMA right = expr R_PAREN                              #dateDiff
 | left = expr op = (K_AND | K_OR) right = expr                                                             #binary
 | validProperty                                                                                            #property
 | validValue                                                                                               #value
 ;

 listElems
 : storedList = K_LIST L_PAREN STRING_LITERAL R_PAREN
 | literalList = STRING_LITERAL (COMMA STRING_LITERAL)*
 | validProperty
 ;

 validValue
 : string = STRING_LITERAL
 | number = NUMERIC_LITERAL
 | nullValue = K_NULL
 | currentDate = CURRENT_DATE
 ;

 validProperty
 : property = ID
 | nestedProperty = ID (DOT ID)+
 ;


 DOT : '.';
 COMMA : ',';
 ADD : '+';
 SUBTRACT : '-';
 MULTIPLY: '*';
 DIVIDE: '/';
 TILDE : '~';
 LT : '<';
 LT_EQ : '<=';
 GT : '>';
 GT_EQ : '>=';
 EQ_IC : '=';
 EQ : '==';
 NOT_EQ : '<>';
 HOUR : 'hour';
 DAY : 'day';
 CURRENT_DATE : 'currentDate' L_PAREN R_PAREN;
 DATE_DIFF: 'dateDiff';
 K_LIST: 'list';
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
 K_DISTINCT: D I S T I N C T;
 K_NULL: N U L L;
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