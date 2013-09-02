grammar Mapping;

@parser::header {
  package org.wso2.datamapper.parsers;
  import java.util.*;
}

@lexer::header {
  package org.wso2.datamapper.parsers;
}

mapping : stat ';'
     ;
     
stat: defdatatype | defelement | defvar | deffunc
    ;    
     
deftype : ID '-''>' ID 
        ;

defelement : outputelement '=" value
           ;
           
outputelement: ID (DOT ID)* | var
             ;
  
value: (function )+ | (arg)
            ;

var : VAR varid
    ;   

function : funcid '(' arg (',' arg)* ')'
		 ;
		
funcid : ID
       ;

varid : ID
        ;

arg: ID (DOT ID)* | DELEMETER
   ;


DOT : [.];
VAR : 'var' ;
ID  :   [a-zA-Z]+;      // match identifiers
DELEMETER : '"' . '"' ;  // match delemeters
INT :   [0-9]+ ;         // match integers
NEWLINE:'\r'? '\n' ;     // return newlines to parser (is end-statement signal)
WS  :   [ '\t' | ' ']+ -> skip ; // toss out whitespace