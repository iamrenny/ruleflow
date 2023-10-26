grammar ANA;

@header {
    package com.rappi.fraud.analang;
}

parse: (workflow | error) EOF;

error: UNEXPECTED_CHAR {
    throw new RuntimeException("UNEXPECTED_CHAR= " + $UNEXPECTED_CHAR.text);
};

workflow: K_WORKFLOW workflow_name rulesets* default_clause K_END;

workflow_name: STRING_NOT_SPECIAL_CHARS;

string_literal: (STRING_NOT_SPECIAL_CHARS | SQUOTA_STRING);

STRING_NOT_SPECIAL_CHARS: '\'' [a-zA-Z0-9_-]+ '\'';

rulesets: K_RULESET name rules*;

rules: name rule_body;

rule_body: expr ((K_THEN (K_WITH| K_AND)?  then_result = actions) | (K_RETURN result = return_result actions? ));

name: string_literal;

default_clause: K_DEFAULT (K_THEN | K_RETURN)? default_result = return_result actions?;

return_result: (state | validProperty| validValue | K_EXPR L_PAREN expr R_PAREN);

state: ID;

actions: (K_WITH|K_AND)? action (K_AND action)*?;

action: K_ACTION '(' param_value=string_literal (COMMA action_params)? ')'
      | action_id=ID ('(' action_params ')')?;

action_params: '{' param_pairs '}';

param_pairs: param_pair (COMMA param_pair)*;

param_pair: field_name = string_literal ':' field_value = validValue;

expr: L_PAREN expr R_PAREN                                                      #parenthesis
    | left=expr op=(MULTIPLY | DIVIDE | MODULO) right=expr                      #mathMul
    | left=expr op=(ADD | MINUS) right=expr                                     #mathAdd
    | left=expr op=(LT | LT_EQ | GT | GT_EQ | EQ | EQ_IC | NOT_EQ) right=expr   #comparator
    | value=expr not=K_NOT? op=(K_CONTAINS | K_IN | K_STARTS_WITH) values=listElems #list
    | value=expr DOT op=(K_COUNT | K_AVERAGE | K_ANY | K_ALL | K_DISTINCT | K_NONE)
      (L_BRACE predicate=expr R_BRACE | L_PAREN R_PAREN)                       #aggregation
    | DATE_DIFF L_PAREN (HOUR | DAY | MINUTE) COMMA left=expr COMMA right=expr R_PAREN #dateDiff
    | op=DAY_OF_WEEK L_PAREN left=expr R_PAREN                                   #dayOfWeek
    | op = REGEX_STRIP L_PAREN value = validProperty COMMA regex = SQUOTA_STRING R_PAREN                       #regexlike
    | op=ABS L_PAREN left=expr R_PAREN                                          #unary
    | left=expr op=K_AND right=expr                                             #binaryAnd
    | left=expr op=K_OR right=expr                                              #binaryOr
    | validValue                                                                 #value
    | validProperty                                                              #property;


listElems: storedList=K_LIST L_PAREN string_literal R_PAREN
         | literalList=string_literal (COMMA string_literal)*
         | validProperty;

validValue: string = string_literal
          | number=NUMERIC_LITERAL
          | booleanLiteral=BOOLEAN_LITERAL
          | nullValue=K_NULL
          | currentDate=CURRENT_DATE;

validProperty: root=DOT? property=ID
             | root=DOT? nestedProperty=ID (DOT ID)+;

DOT: '.';
COMMA: ',';
ADD: '+';
MINUS: '-';
MULTIPLY: '*';
DIVIDE: '/';
LT: '<';
LT_EQ: '<=';
GT: '>';
GT_EQ: '>=';
EQ_IC: '=';
EQ: '==';
NOT_EQ: '<>';
MINUTE: 'minute';
HOUR: 'hour';
DAY: 'day';
CURRENT_DATE: 'currentDate' L_PAREN R_PAREN
             | 'currentdate' L_PAREN R_PAREN;
DATE_DIFF: 'dateDiff' | 'datediff';
ABS: 'abs';
REGEX_STRIP: 'regex_strip' | 'regexStrip' | 'regexstrip';
MODULO: '%' | 'mod';
K_STARTS_WITH: 'starts_with' | 'startswith' | 'startsWith';
K_LIST: 'list';
L_BRACE: '{';
R_BRACE: '}';
L_PAREN: '(';
R_PAREN: ')';
K_COLON: ':';
K_ACTION: 'action';
K_WORKFLOW: W O R K F L O W;
K_RULESET: R U L E S E T;
K_RETURN: R E T U R N;
K_THEN: T H E N;
K_DEFAULT: D E F A U L T;
K_WITH: W I T H;
K_END: E N D;
K_ELSE: E L S E;
K_AND: A N D;
K_OR: O R;
K_CONTAINS: C O N T A I N S;
K_IS: I S;
K_NOT: N O T;
K_IS_NOT: K_IS K_NOT;
K_IN: I N;
K_ANY: A N Y;
K_NONE: N O N E;
K_ALL: A L L;
K_COUNT: C O U N T;
K_AVERAGE: A V E R A G E;
K_DISTINCT: D I S T I N C T;
K_NULL: N U L L;
DAY_OF_WEEK: D A Y O F W E E K;
K_EXPR : E X P R;



ID: [a-zA-Z_] [a-zA-Z_0-9]*;
NUMERIC_LITERAL
  : MINUS? DIGIT+ ( '.' DIGIT* )? ( E [-+]? DIGIT+ )?
  | '.' DIGIT+ ( E [-+]? DIGIT+ )?
  ;
BOOLEAN_LITERAL: T R U E | F A L S E;
DQUOTA_STRING: '"' ('\\'. | '\\"' | ~['"\\'])* '"';
SQUOTA_STRING: '\'' ('\\'. | '\'\'' | ~('\'' | '\\'))* '\'';

SINGLE_LINE_COMMENT: '--' ~[\r\n]* -> channel(HIDDEN);
MULTILINE_COMMENT: '/*' .*? '*/' -> channel(HIDDEN);
SPACES: [ \u000B\t\r\n] -> channel(HIDDEN);

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