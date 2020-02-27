#|
================================================================
Program: COSC_3410_Assignment_2_Problem_3
Version: 1.0
Author: David Helminiak
Date Created: 10 September 2018
Date Last Modified: 13 September 2018
================================================================

==========================DESCRIPTION===========================
Takes a list and computes the product of all the numbers.
Null values returns 1 by the multiplicative identity. 
================================================================

===========================SIGNATURE============================
deep-mult list -> atom
================================================================

=============================CASES==============================
Input: list containing no values

Output: 1

----------------------------------------------------------------
Input: list contains a list

Output: deep-mult applied to it, times deep-mult applied to the
remainder

----------------------------------------------------------------
Input: list containing a number

Output: product of the number by 1

----------------------------------------------------------------
Input: list containing multiple numbers

Output: product of all numbers

================================================================

============================SAMPLE RUN==========================
ADDITIONAL TESTING
>(deep-mult '())
1
>(deep-mult '(5))
5
>(deep-mult '(5 2))
10
>(deep-mult '((5) (2)))
10
>(deep-mult '((Trying (5)) to (2) break (((code) 10))))
100

EXAMPLE TESTING
>(deep-mult '(5 a b 8 2))
80
>(deep-mult '((4 (6 1)) 2 3 (4)))
576
>(deep-mult '(these (aren't 77) (all 32 (numbers 93 here))))
229152
>(deep-mult '())
1
>(deep-mult '(no numbers here))
1
================================================================
|#

;============================PROGRAM============================
#lang racket

(define atom? ;Define atom
  (lambda (x)
    (and (not (pair? x)) (not (null? x)))
  )
)

(define deep-mult ;Define the deep-mult function
  (lambda (valList) ;Expect a single list
    (cond ;Set conditional statements
      [(null? valList) 1] ;If the list is empty return 1
      [(list? (car valList)) (* (deep-mult (car valList)) (deep-mult (cdr valList)))] ;If the first value in the list is a list, then multiply the result of deep-mult applied to it by the result of deep-mult apdplied to the remainder
      [(number? (car valList)) (* (car valList) (deep-mult (cdr valList)))]; If the first value is a number than multiply it by deep-mult function as applied to the remainder 
      [else (deep-mult (cdr valList))] ;Otherwise continue recursing until nothing is left
    )
  )
)

;=======================ADDITIONAL TESTING======================
(printf "\nADDITIONAL TESTING\n")

(printf ">(deep-mult '())\n")
(deep-mult '())

(printf ">(deep-mult '(5))\n")
(deep-mult '(5))

(printf ">(deep-mult '(5 2))\n")
(deep-mult '(5 2))
           
(printf ">(deep-mult '((5) (2)))\n")
(deep-mult '((5) (2)))

(printf ">(deep-mult '((Trying (5)) to (2) break (((code) 10))))\n")
(deep-mult '((Trying (5)) to (2) break (((code) 10))))

;========================EXAMPLE TESTING========================
(printf "\nEXAMPLE TESTING\n")

(printf ">(deep-mult '(5 a b 8 2))\n")
(deep-mult '(5 a b 8 2))

(printf ">(deep-mult '((4 (6 1)) 2 3 (4)))\n")
(deep-mult '((4 (6 1)) 2 3 (4)))

(printf ">(deep-mult '(these (aren't 77) (all 32 (numbers 93 here))))\n")
(deep-mult '(these (aren't 77) (all 32 (numbers 93 here))))

(printf ">(deep-mult '())\n")
(deep-mult '())

(printf ">(deep-mult '(no numbers here))\n")
(deep-mult '(no numbers here))
