#|
================================================================
Program: COSC_3410_Assignment_2_Problem_1
Version: 1.0
Author: David Helminiak
Date Created: 10 September 2018
Date Last Modified: 10 September 2018
================================================================

==========================DESCRIPTION===========================
Takes a list of numbers and returns the list with each even
number added to by one, and each odd number subtracted to by one. 
================================================================

===========================SIGNATURE============================
updown list -> list
================================================================

=============================CASES==============================
Input: list without any values

Output: list without any values

----------------------------------------------------------------
Input: list with an odd number

Output: list with the number with 1 subtracted from it

----------------------------------------------------------------
Input: list with an even number

Output: list with the number with 1 added to it

----------------------------------------------------------------
Input: list with more than one value

Output: list with even/odd rules applied to each values
================================================================

============================SAMPLE RUN==========================
ADDITIONAL TESTING
>(updown '())
'()
>(updown '(1))
'(0)
>(updown '(2))
'(3)
>(updown '(1 2))
'(0 3)
>(updown '(2 1 3 4 1))
'(3 0 2 5 0)

EXAMPLE TESTING
>(updown '(2 4 10 3 6))
'(3 5 11 2 7)
>(updown '(1 3))
'(0 2)
>(updown '())
'()
>(updown '(1 2 -3 -4 7 12))
'(0 3 -4 -3 6 13)
================================================================
|#

;============================PROGRAM============================
#lang racket
(define updown ;Define the updown function
  (lambda (numList) ;Expect a list of numbers
    (cond ;Set conditional statements
      [(null? numList) '()]; If the list is empty, return an empty list
      [(even? (car numList)) (cons (+ (car numList) 1) (updown (cdr numList)))]; If the value is even, add one to it and recurse over the remainder
      [(odd? (car numList)) (cons (- (car numList) 1) (updown (cdr numList)))]; If the value is odd, subtract one from it and recurse over the remainder
    )
  )
)

;=======================ADDITIONAL TESTING======================
(printf "\nADDITIONAL TESTING\n")

(printf ">(updown '())\n")
(updown '())

(printf ">(updown '(1))\n")
(updown '(1))

(printf ">(updown '(2))\n")
(updown '(2))

(printf ">(updown '(1 2))\n")
(updown '(1 2))

(printf ">(updown '(2 1 3 4 1))\n")
(updown '(2 1 3 4 1))

;========================EXAMPLE TESTING========================
(printf "\nEXAMPLE TESTING\n")

(printf ">(updown '(2 4 10 3 6))\n")
(updown '(2 4 10 3 6))

(printf ">(updown '(1 3))\n")
(updown '(1 3))

(printf ">(updown '())\n")
(updown '())

(printf ">(updown '(1 2 -3 -4 7 12))\n")
(updown '(1 2 -3 -4 7 12))
