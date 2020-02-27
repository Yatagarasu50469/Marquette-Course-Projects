#|
================================================================
Program: COSC_3410_Assignment_2_Problem_4
Version: 1.0
Author: David Helminiak
Date Created: 10 September 2018
Date Last Modified: 14 September 2018
================================================================

==========================DESCRIPTION===========================
Remove all of the included parentheses in a given list. 
================================================================

===========================SIGNATURE============================
drop-parens list -> list
================================================================

=============================CASES==============================
Input: empty list

Output: empty list

----------------------------------------------------------------
Input: list with values

Output: list with values

----------------------------------------------------------------
Input: list with lists of values

Output: list with values

================================================================

============================SAMPLE RUN==========================
ADDITIONAL TESTING
>(drop-parens '())
'()
>(drop-parens '(a list of values))
'(a list of values)
>
'(a list of values)
>(drop-parens '(((((((((((romeo)))))))))) & ((((((((((juliet))))))))))))
'(romeo & juliet)

EXAMPLE TESTING
>(drop-parens '((a 34)(b 77)(g 6)) )
'(a 34 b 77 g 6)
>(drop-parens '(a b c) )
'(a b c)
>(drop-parens '() )
'()
>(drop-parens '(()((() x)())) )
'(x)
================================================================
|#

;============================PROGRAM============================
#lang racket

(define atom? ;Define atom
  (lambda (x)
    (and (not (pair? x)) (not (null? x)))
  )
)

(define drop-parens ;Define the drop-parens function
  (lambda (nestedList) ;Expect a list as input
    (cond ;Set conditionals
      [(null? nestedList) '()] ;If the list is empty, return an empty list
      [(atom? (car nestedList)) (append (list (car nestedList)) (drop-parens (cdr nestedList)))] ;If the first value is an atom, then add its list form to the result of a recursive call on the list remainder
      [else (append (drop-parens (car nestedList)) (drop-parens (cdr nestedList)))] ;Otherwise, it is a list, so add the result of a recursive call on the list to the result of a recursive call on the remainder 
    )
  )
)

;=======================ADDITIONAL TESTING======================
(printf "\nADDITIONAL TESTING\n")

(printf ">(drop-parens '())\n")
(drop-parens '())

(printf ">(drop-parens '(a list of values))\n")
(drop-parens '(a list of values))

(printf ">\n")
(drop-parens '((a) ((list of)) (((values)))))

(printf ">(drop-parens '(((((((((((romeo)))))))))) & ((((((((((juliet))))))))))))\n")
(drop-parens '(((((((((((romeo)))))))))) & ((((((((((juliet))))))))))))

;========================EXAMPLE TESTING========================
(printf "\nEXAMPLE TESTING\n")

(printf ">(drop-parens '((a 34)(b 77)(g 6)) )\n")
(drop-parens '((a 34)(b 77)(g 6)) )

(printf ">(drop-parens '(a b c) )\n")
(drop-parens '(a b c) )

(printf ">(drop-parens '() )\n")
(drop-parens '() )

(printf ">(drop-parens '(()((() x)())) )\n")
(drop-parens '(()((() x)())) )