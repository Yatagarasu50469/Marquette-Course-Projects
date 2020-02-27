#|
================================================================
Program: COSC_3410_Assignment_Q_1
Version: 1.0
Author: David Helminiak
Date Created: 24 September 2018
Date Last Modified: 10 October 2018
================================================================

==========================DESCRIPTION===========================
Provides an extention to the F1CWAE language to handle functions
and implements two recursive pograms in the language:
1) Calculation of the fibonacci sequence's nth value
2) Calculation of a factorial: n!
================================================================

==========================SIGNATURES============================
INTERPRETER
parse : sexp -> F1CWAE
subst : F1CWAE symbol F1CWAE --> F1CWAE
interp : F1CWAE --> number

FUNCTIONS
sum-digits: num -> num
fibonacciNum : num -> num
factorial : num -> num
================================================================

============================TEST CASES==========================
ADDITIONAL TESTING -- PASSED

(test (interp (parse '{fibonacciNum 0}) dlist) 1)
(test (interp (parse '{fibonacciNum 1}) dlist) 1) 
(test (interp (parse '{fibonacciNum 2}) dlist) 1)
(test (interp (parse '{fibonacciNum 3}) dlist) 2)
(test (interp (parse '{fibonacciNum 4}) dlist) 3)
(test (interp (parse '{fibonacciNum 5}) dlist) 5)

(test (interp (parse '{factorial 0}) dlist) 1)
(test (interp (parse '{factorial 1}) dlist) 1)
(test (interp (parse '{factorial 2}) dlist) 2)
(test (interp (parse '{factorial 3}) dlist) 6)

EXAMPLE TESTING
(test (interp (parse '{sum-digits 6430761}) dlist) 27)

================================================================

=========================PERSONAL NOTES=========================

================================================================
|#

;============================PROGRAM============================
#lang plai

;Define F1CWAE (with arithmetic expressions) in abstract trees
(define-type F1CWAE
  [id (name symbol?)]
  [num (n number?)]
  [equalVal? (lhs F1CWAE?) (rhs F1CWAE?)]
  [add (lhs F1CWAE?) (rhs F1CWAE?)] 
  [sub (lhs F1CWAE?) (rhs F1CWAE?)]
  [minus (lhs F1CWAE?)]
  [mult (lhs F1CWAE?) (rhs F1CWAE?)] 
  [div (lhs F1CWAE?) (rhs F1CWAE?)]
  [with (name symbol?) (named-expr F1CWAE?) (body F1CWAE?)]
  [if<0 (lhs F1CWAE?) (rhs F1CWAE?) (rhs2 F1CWAE?)]
  [app (fun-name symbol?) (arg F1CWAE?)]  
)

;Define function definition taking a function name, argument name, and a body
(define-type FunDef
  [fundef (fun-name symbol?)
          (arg-name symbol?)
          (body F1CWAE?)
  ]
)

;Define function lookup for a name within a list of function definitions
(define (lookup-fundef name defs)
  (cond ;Set conditonals
    [(null? defs) (error name "Definition not found")] ;If the function list is empty return error
    [(symbol=? name (fundef-fun-name (car defs))) (car defs)] ;Should the symbolic function name sought match the symbolic function name of the first function list item, return that function
    [else (lookup-fundef name (cdr defs))]; Otherwise, recurse over the remaining function definitions listed
  )
)

;Define parse of s-expressions into F1CWAEs
(define (parse sexp)
  (cond  ;Set conditionals
    [(number? sexp) (num sexp)]
    [(symbol? sexp) (id sexp)] ;If the sexp is a symbol, return its id
    [(list? sexp) ;If the sexp is a list
     (cond  ;Set conditionals
       [(null? sexp) (error "Expression was blank; empty lists not currently supported")]
       [(number? (first sexp)) (error "Number submitted as or without operator")]
     [(case (first sexp) ;Consider the following cases for its first value
       [(equalVal?) (cond ;Set conditionals
                      [(> (length sexp) 3) (error "equalVal? list contains too many values; currently have:" (length sexp))] ;Max length check
                      [(< (length sexp) 3) (error "equalVal? list is missing arguments; currently:" (length sexp))] ;Min length check
                      [(eq? (length sexp) 3) (equalVal? (parse (second sexp)) (parse (third sexp)))] ;If sexp length 3, perform standard equalVal?
                      [else (error "A highly improbable and unknown error has occured for equalVal? function")] ;Indicate unspecified error
                    )
       ]
       [(+) (cond ;Set conditionals
              [(> (length sexp) 3) (error "add list contains too many values; currently have:" (length sexp))] ;Max length check
              [(< (length sexp) 3) (error "add list is missing arguments; currently:" (length sexp))] ;Min length check
              [(eq? (length sexp) 3) (add (parse (second sexp)) (parse (third sexp)))] ;If sexp length 3, perform standard addition
              [else (error "A highly improbable and unknown error has occured for add function")] ;Indicate unspecified error
            )
       ]
       [(*) (cond ;Set conditionals
              [(> (length sexp) 3) (error "mult list contains too many values; currently have:" (length sexp))] ;Max length check
              [(< (length sexp) 3) (error "mult list is missing arguments; currently have:" (length sexp))] ;Min length check
              [(eq? (length sexp) 3) (mult (parse (second sexp)) (parse (third sexp)))] ;If sexp length 3, perform standard multiplication
              [else (error "A highly improbable and unknown error has occured for mult function")] ;Indicate unspecified error
            )
       ]
       [(/) (cond ;Set conditionals
              [(> (length sexp) 3) (error "div list contains too many values; currently have:" (length sexp))] ;Max length check
              [(< (length sexp) 3) (error "div list is missing arguments; currently have:" (length sexp))] ;Min length check
              [(eq? (length sexp) 3) (div (parse (second sexp)) (parse (third sexp)))] ;If sexp length 3, perform standard division
              [else (error "An unknown error has occured for div function")] ;Indicate unspecified error
            )
       ]
       [(-) (cond ;Set conditionals
              [(> (length sexp) 3) (error "sub/minus list contains too many values; currently have:" (length sexp))] ;Max length check
              [(< (length sexp) 2) (error "sub/minus list is missing arguments; currently have:" (length sexp))] ;Min length check
              [(eq? (length sexp) 2) (cond ;If the length is 2, set the following conditions for unary subtraction "minus"
                                       [(minus (parse (second sexp)))] ;Perform unary subtraction
                                       [else (error "A highly improbable and unknown error has occured for the minus function")] ;Indicate unspecified error
                                     )
              ]
              [(eq? (length sexp) 3) (cond ;If the length is 3, set the following conditions for subtraction
                                       [(sub (parse (second sexp)) (parse (third sexp)))] ;Perform standard subtraction
                                       [else (error "A highly improbable and unknown error has occured for the sub function")] ;Indicate unspecified error
                                     )
              ]
              [else (error "A highly improbable and unknown error has occured for either the subtraction or minus function")] ;Indicate unspecified error
            )
       ]
       [(with) (cond ;Set conditionals
                 [(> (length sexp) 3) (error "with list contains too many values; currently have:" (length sexp))] ;Max length check
                 [(< (length sexp) 3) (error "with list is missing arguments; currently have:" (length sexp))] ;Min length check
                 [(not (list? (second sexp))) (error "with list first argument was not a list")]
                 [(< (length (second sexp)) 2) (error "with list is missing values; currently have:" (length (second sexp)))] ;Min length check for argument list
                 [(> (length (second sexp)) 2) (error "with list contains too many values; currently have:" (length (second sexp)))] ;Max length check for argument list
                 [(with (first (second sexp)) (parse (second (second sexp)))(parse (third sexp)))]
                 [else (error "A highly improbable and unknown error has occured for with function")] ;Indicate unspecified error
               )
       ]
       [(if<0) (cond ;Set conditionals
                 [(> (length sexp) 4) (error "if<0 list contains too many values; currently have:" (length sexp))] ;Max length check
                 [(< (length sexp) 4) (error "if<0 list is missing arguments; currently have:" (length sexp))] ;Min length check
                 [(eq? (length sexp) 4) (if<0 (parse (second sexp)) (parse (third sexp)) (parse (fourth sexp)))]
                 [else (error "A highly improbable and unknown error has occured for if<0 function")] ;Indicate unspecified error
               )
       ]
       [else (cond
               [(symbol? (first sexp)) (app (first sexp) (parse (second sexp)))]
               [else (error "The parser has failed to interperet the operator:" (first sexp))]
             )
       ]
   )
      ]
     )
  ]
  [else (error "Parser input was unexpected")]
 )
)

;Define substitution
(define(subst expr sub-id val)
  (type-case F1CWAE expr
    [id (a) (if (symbol=? a sub-id) val expr)]
    [num (a) expr]
    [equalVal? (a b) (eq? (subst a sub-id val) (subst b sub-id val))]
    [add (a b) (add (subst a sub-id val) (subst b sub-id val))]
    [minus (a) (minus (subst a sub-id val))]
    [sub (a b) (sub (subst a sub-id val) (subst b sub-id val))]
    [mult (a b) (mult (subst a sub-id val) (subst b sub-id val))]
    [div (a b) (div (subst a sub-id val) (subst b sub-id val))] 
    [with (bound-id named-expr bound-body)
          (if (symbol=? bound-id sub-id)
              (with bound-id (subst named-expr sub-id val) bound-body)
              (with bound-id (subst named-expr sub-id val) (subst bound-body sub-id val)))]
    [if<0 (a b c) (if<0 (subst a sub-id val) (subst b sub-id val) (subst c sub-id val))]
    [app (fun-name arg-expr) (app fun-name (subst arg-expr sub-id val))]
  )
)

;Define interpretation of F1CWAE expressions
(define (interp expr fun-defs)
  (type-case F1CWAE expr
    [id (a) (error interp "free identifier ~a" a)]
    [num (a) a]
    [equalVal? (a b) (eq? (interp a fun-defs) (interp b fun-defs))]
    [add (a b) (+ (interp a fun-defs) (interp b fun-defs))]
    [minus (a) (- (interp a fun-defs))]
    [sub (a b) (- (interp a fun-defs) (interp b fun-defs))]
    [mult (a b) (* (interp a fun-defs) (interp b fun-defs))]
    [div (a b) (quotient (interp a fun-defs) (interp b fun-defs))]
    [with (bound-id named-expr bound-body) (interp (subst bound-body bound-id (num (interp named-expr fun-defs))) fun-defs)]
    [if<0 (a b c) (cond
                    [(< (interp a fun-defs) 0) (interp b fun-defs)]
                    [else (interp c fun-defs)]
                  )
    ]
    [app (fun-name arg-expr)
         (let ([the-fun-def (lookup-fundef fun-name fun-defs)]) ;Let the function be the function as defined in the provided function definition list
           (interp (subst (fundef-body the-fun-def) (fundef-arg-name the-fun-def) (num (interp arg-expr fun-defs))) fun-defs)
         )
    ]
   )
)

(define dlist (list
                (fundef 'sum-digits 'n ;; Add up the digits of n
                        (parse '{
                                 if<0 {- n 10} n {with {q {/ n 10}} {with {r {- n {* q 10}}} {+ r {sum-digits q}}}};; if single digit return value, otherwise, pull off the last digit and recurse
                                 }
                        )
                )
                (fundef 'fibonacciNum 'n ;Get the fibonacci value of n
                        (parse '{
                                 if<0 {- n 2} 1 ;If n is 1 then return 1, otherwise
                                      (if<0 {- n 3} 1 (+ (fibonacciNum (- n 1)) (fibonacciNum (- n 2)))) ;if n is 2 then return 1; if not then recurse and sum as formula dictates
                                }
                         )
                )
                (fundef 'factorial 'n ;Compute the factorial of n
                        (parse '{
                                 if<0 {- n 2} 1 ;If n is 1 then return 1, otherwise
                                      (* n (factorial (- n 1))) ;Multiply n by the recursion on n-1
                                }
                         )
                )
              )
)
