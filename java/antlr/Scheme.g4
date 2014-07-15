grammar Scheme;
prog:   (sexpr)* ;
sexprlist:  LPAREN sexpr+ RPAREN ;
sexpr:  sexprBoolean | sexprIdent | sexprNumber | sexprlist;
sexprIdent:     IDENT   ;
sexprNumber:    NUMBER  ;
sexprBoolean:   BOOLEAN ;

IDENT:     (CHAR | SYMBOL) (DIGIT | CHAR | SYMBOL)* ;
NUMBER:     ('-'|'+')? DIGIT+ ;
BOOLEAN:   TRUE | FALSE;

fragment SYMBOL:     '='|'+'|'-'|'*'|'/'|'>'|'<'|'?'|'!' ;
fragment DIGIT:     [0-9] ;
TRUE:       '#t' ;
FALSE:      '#f' ;
fragment CHAR:       [a-zA-Z] ;

LPAREN: '(' ;
RPAREN: ')' ;
WS:         [ \t\r\n]+ -> skip ;
