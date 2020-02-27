#|
================================================================
Program: COSC_3410_Assignment_3
Version: 1.0
Author: David Helminiak
Date Created: 24 September 2018
Date Last Modified: 26 September 2018
================================================================

==========================DESCRIPTION===========================
Provides an extention to the WAE language including multiply and
divide operators, a conditional expression "if<0" and some basic
WAE grammer/calculation error checks. 
================================================================

==========================SIGNATURES============================
parse : sexp -> WAE
subst : WAE symbol WAE --> WAE
calc : WAE --> number
================================================================

============================TEST CASES==========================
ADDITIONAL TESTING -- PASSED

     ERROR CHECKING
;(calc (parse '()))
;(calc (parse '{+ 1}))
;(calc (parse '{+ 1 2 3}))
;(calc (parse '{-}))
;(calc (parse '{- 1 2 3}))
;(calc (parse '{* 1}))
;(calc (parse '{* 1 2 3}))
;(calc (parse '{/ 1}))
;(calc (parse '{/ 1 2 3}))
;(calc (parse '{if<0 1 2 3 4}))
;(calc (parse '{if<0 1 2}))
;(calc (parse '{if<0 1}))
;(calc (parse '{with {x 1} {y 1} {+ x y}}))
;(calc (parse '{with {x 1}}))

     PROGAM TESTING -- PASSED
(test (calc (parse '{with {x 1} {+ x 1}})) 2)
(test (calc (parse '{with {x 1} {* x 5}})) 5)
(test (calc (parse '{with {x 1} {/ x 2}})) 0)
(test (calc (parse '{with {x 3} {/ x 2}})) 1)
(test (calc (parse '{with {x 1} {- x 1}})) 0)
(test (calc (parse '{with {x 1} {- x}})) -1)
(test (calc (parse '{with {x 3} {if<0 x 0 1}})) 1)
(test (calc (parse '{with {x 0} {if<0 x 0 1}})) 1)
(test (calc (parse '{with {x -1} {if<0 x 0 1}})) 0)

EXAMPLE TESTING

     ERROR CHECKING -- PASSED
;(calc (parse '{+ 4 5 6}))
;(calc (parse '{+ 2}))
;(calc (parse '{with 6}))
;(calc (parse '{with {x 6}}))
;(calc (parse '{3 4 5}))
;(calc (parse '{- 6 5 2}))
;(calc (parse '{+}))
;(calc (parse '{with {x 32} {+ x 4} {+ x 10}}))


     PROGAM TESTING -- PASSED
(test (calc (parse '{with {a 5} {if<0 a 77 99}})) 99)
(test (calc (parse '{with {x -12} {- x}})) 12)
(test (calc (parse '{* 7 3})) 21)
(test (calc (parse '{/ 12 5})) 2)
(test (calc (parse '{- 33})) -33)
(test (calc (parse '{- 5 {- 2}})) 7)
(test (calc (parse '{if<0 7 111 222})) 222)

(test (calc (parse '
{with {x 9}    ; you can change these vals, but the larger one should
  {with {y 3}  ; always wind up at the left of the final number
               ; and the smaller one at the right
     {with  {min  {if<0 {- x y} x y}}
        {with  {max  {if<0 {- x y} y x}}
           {+ {* max 1000} min} ; output will be best if smaller is
 }}}})) 9003)

(test (calc (parse '
{with {num1 -33}
  {with {num2 876}
    {with {num3 305}
	  {if<0 {- num1 num2} {if<0 {- num2 num3} num3 num2}
	                 {if<0 {- num1 num3} num3 num1} }
}}})) 876)

(test (calc (parse '
{with {a 1}
  {with {b 3}
    {with {c 2}
	  {/ {+ {- b} {- {* b b} {* {* 4 a} c}}} {* 2 a} }
}}})) -1)

================================================================

=========================PERSONAL NOTES=========================
The following error checking line:

;[(not (num? (parse (second sexp)))) (error "First add argument is not a number")] ;Check input type is correct

is non-functional when used in the parser definition as binding
of variables within the 'with' function occurs after the
evaluation of functions using symbols. Tests such as: 

(calc (parse '{+ '{1 2 3} 4}))
(calc (parse '{+ '{1 2 3} 1}))
(calc (parse '{+ 1 '{1 2 3}}))
(calc (parse '{- '{1 2 3} 1}))
(calc (parse '{- 1 '{1 2 3}}))
(calc (parse '{- '{1 2 3}}))
(calc (parse '{* '{1 2 3} 1}))
(calc (parse '{* 1 '{1 2 3}}))
(calc (parse '{/ '{1 2 3} 1}))
(calc (parse '{/ 1 '{1 2 3}}))

will fail given that the parser cannot distinguish between lists
and symbolic expressions (since they do not evaluate).

================================================================
|#

;============================PROGRAM============================
#lang plai

;Define WAE (with arithmetic expressions) in abstract trees
(define-type WAE ;Define 'with' arithmetic expression
  [id (name symbol?)]
  [num (n number?)] 
  [add (lhs WAE?) (rhs WAE?)] 
  [sub (lhs WAE?) (rhs WAE?)]
  [minus (lhs WAE?)]
  [mult (lhs WAE?) (rhs WAE?)] 
  [div (lhs WAE?) (rhs WAE?)]
  [with (name symbol?) (named-expr WAE?) (body WAE?)]
  [if<0 (lhs WAE?) (rhs WAE?) (rhs2 WAE?)]
  
)

;Define parse of s-expressions into WAEs
(define (parse sexp)
  (cond  ;Set conditionals
    [(number? sexp) (num sexp)]
    [(symbol? sexp) (id sexp)] ;If the sexp is a symbol, return its id
    [(list? sexp) ;If the sexp is a list
     (cond  ;Set conditionals
       [(null? sexp) (error "Expression was blank; empty lists not currently supported")]
       [(number? (first sexp)) (error "Number submitted as or without operator")]
     [(case (first sexp) ;Consider the following cases for its first value
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
   )
      ])
  ]
 )
)

;Define substitution
(define(subst expr sub-id val)
  (type-case WAE expr
    [id (a) (if (symbol=? a sub-id) val expr)]
    [num (a) expr]
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
  )
)

;Define calculation of WAE expressions
(define (calc expr)
  (type-case WAE expr
    [id (a) (error 'calc "free identifier ~a" a)]
    [num (a) a]
    [add (a b) (+ (calc a) (calc b))]
    [minus (a) (- (calc a))]
    [sub (a b) (- (calc a) (calc b))]
    [mult (a b) (* (calc a) (calc b))]
    [div (a b) (quotient (calc a) (calc b))]
    [with (bound-id named-expr bound-body) (calc (subst bound-body bound-id (num (calc named-expr))))]
    [if<0 (a b c) (cond
                    [(< (calc a) 0) (calc b)]
                    [else (calc c)]
                  )
    ]
   )
)