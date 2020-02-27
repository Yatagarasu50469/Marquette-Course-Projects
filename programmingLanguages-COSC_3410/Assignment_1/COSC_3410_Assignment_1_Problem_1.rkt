#|
================================================================
Program: COSC_3410_Assignment_1_Problem_1
Version: 1.0
Author: David Helminiak
Date Created: 31 August 2018
Date Last Modified: 31 August 2018
================================================================

==========================DESCRIPTION===========================
Takes two numbers as limits and a list of numbers, returning
true if each number in the list is between the limits and false
if not.
================================================================

===========================SIGNATURE============================
in-range? atom atom list --> boolean
================================================================

=============================CASES==============================
Input: Two values 'a' and 'b' with an empty list

Output: True, as a null list is always within range

----------------------------------------------------------------
Input: Two values 'a' and 'b' with a list whose first value is 
below 'a'

Output: False, as there is a value not within range

----------------------------------------------------------------
Input: Two values 'a' and 'b' with a list whose first value is 
greater than the 'b'

Output: False, as there is a value not within range

----------------------------------------------------------------
Input:  Two values a and b with a list whose first value is
within range of 'a' and 'b'

Output: Result of the function called for the remaining values
within the list
================================================================

============================SAMPLE RUN==========================
ADDITIONAL TESTING
>(in-range? 0 10 '())
#t
>(in-range? 0 10 '(1 3 5 9 5 7))
#t
>(in-range? 0 10 '(-1 11))
#f
>(in-range? 0 10 '(0 10))
#t

EXAMPLE TESTING
>(in-range? 3 12 '(5 3 9) )
#t
>(in-range? 3 12 '(5 13 9) )
#f
>(in-range? 4 4 '(4 4 4 4 4) )
#t
>(in-range? 3 12 '() )
#t
================================================================
|#

;============================PROGRAM============================
#lang racket

(define in-range? ;Define the in-range? function
  (lambda (inLimit outLimit numList) ;Expect two limits and a list as inputs
    (cond ;Set conditional statements
      [(null? numList) #t] ;If the list is empty then return true boolean
      [(< (car numList) inLimit) #f] ;Return false if first param. less than in_limit
      [(> (car numList) outLimit) #f] ;Return false if first param. greater than out_limit
      [else (in-range? inLimit outLimit (cdr numList))] ;Check in-range for all other numList values
    )
  )
)

;=======================ADDITIONAL TESTING======================
(printf "\nADDITIONAL TESTING\n")

(printf ">(in-range? 0 10 '())\n")
(in-range? 0 10 '())

(printf ">(in-range? 0 10 '(1 3 5 9 5 7))\n")
(in-range? 0 10 '(1 3 5 9 5 7))

(printf ">(in-range? 0 10 '(-1 11))\n")
(in-range? 0 10 '(-1 11))

(printf ">(in-range? 0 10 '(0 10))\n")
(in-range? 0 10 '(0 10))

;========================EXAMPLE TESTING========================
(printf "\nEXAMPLE TESTING\n")

(printf ">(in-range? 3 12 '(5 3 9) )\n")
(in-range? 3 12 '(5 3 9) )

(printf ">(in-range? 3 12 '(5 13 9) )\n")
(in-range? 3 12 '(5 13 9) )

(printf ">(in-range? 4 4 '(4 4 4 4 4) )\n")
(in-range? 4 4 '(4 4 4 4 4) )

(printf ">(in-range? 3 12 '() )\n")
(in-range? 3 12 '() )
