#|
================================================================
Program: COSC_3410_Assignment_2_ALL_DEFINES
Version: 1.0
Author: David Helminiak
Date Created: 10 September 2018
Date Last Modified: 14 September 2018
================================================================

==========================DESCRIPTION===========================
Contains a listing of all definitions created for assignment 2.
Descriptions, signatures, cases, sample runs and testing are
present in accompanied files:  COSC_3410_Assignment_2_Problem_#.
================================================================

|#

;============================PROGRAMS============================
#lang racket

;============================PROGRAM 1============================
(define updown ;Define the updown function
  (lambda (numList) ;Expect a list of numbers
    (cond ;Set conditional statements
      [(null? numList) '()]; If the list is empty, return an empty list
      [(even? (car numList)) (cons (+ (car numList) 1) (updown (cdr numList)))]; If the value is even, add one to it and recurse over the remainder
      [(odd? (car numList)) (cons (- (car numList) 1) (updown (cdr numList)))]; If the value is odd, subtract one from it and recurse over the remainder
    )
  )
)
;================================================================

;============================PROGRAM 2============================
(define zip ;Define the zip function
  (lambda (firstList secList) ;Expect a pair of lists
    (cond ;Set conditional statements
      [(and (null? firstList) (null? secList)) '()] ;If both lists are empty return an empty list
      [(null? firstList) (error 'zip "first list is too short")] ;If the first list is empty return an error
      [(null? secList) (error 'zip "second list is too short")] ;If the second list is empty return an error
      [else (cons (append (list (car firstList)) (list (car secList))) (zip (cdr firstList) (cdr secList)))] ;Otherwise create a pair based on the first value of each list and recurse over the remainder
    )
  )
)
;================================================================

;============================PROGRAM 3============================
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
;================================================================

;============================PROGRAM 4============================
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
;================================================================