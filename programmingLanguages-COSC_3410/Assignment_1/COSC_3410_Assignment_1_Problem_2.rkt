#|
================================================================
Program: COSC_3410_Assignment_1_Problem_2
Version: 1.0
Author: David Helminiak
Date Created: 31 August 2018
Date Last Modified: 31 August 2018
================================================================

==========================DESCRIPTION===========================
Takes an atom and a list, returning the number of occurences
found for that atom within that list. 
================================================================

===========================SIGNATURE============================
atom-count atom list --> atom
================================================================

=============================CASES==============================
Input: atom and list containing no occurences of that atom

Output: 0

----------------------------------------------------------------
Input: atom and list whose first parameter matches the atom

Output: 1 + the number of occurences found in the remainder of
the list

----------------------------------------------------------------
Input: atom and list whose first parameter does not match the
atom

Output: The number of occurences found in the remainder of
the list
================================================================

============================SAMPLE RUN==========================
ADDITIONAL TESTING
>(atom-count 'a '())
0
>(atom-count 'a '(a))
1
>(atom-count 'a '(a b)) 
1
>(atom-count 'a '(a b a))
2

EXAMPLE TESTING
>(atom-count 'b '(a b g a b c b) )
3
>(atom-count 'g '(a b g a b c b) )
1
>(atom-count 'w '(a b g a b c b) )
0
>(atom-count 'b '() )
0
>(atom-count 'x '(x xx x xxx x xxxx x) )
4
================================================================
|#

;============================PROGRAM============================
#lang racket
(define atom-count ;Define the atom-count function
  (lambda (searchParam paramList) ;Expect a parameter to search for and a list of parameters
    (cond ;Set conditional statements
      [(null? paramList) 0] ;If the list is empty than return 0 occurences
      [(eq? (car paramList) searchParam) (+ 1 (atom-count searchParam (cdr paramList)))] ;If the list's first param. matches the searchParam, add 1 to the number of occurances found in the remaining list param.s
      [else (atom-count searchParam (cdr paramList))]
    )
  )
)

;=======================ADDITIONAL TESTING======================
(printf "\nADDITIONAL TESTING\n")

(printf ">(atom-count 'a '())\n")
(atom-count 'a '())

(printf ">(atom-count 'a '(a))\n")
(atom-count 'a '(a))

(printf ">(atom-count 'a '(a b)) \n")
(atom-count 'a '(a b)) 

(printf ">(atom-count 'a '(a b a))\n")
(atom-count 'a '(a b a))

;========================EXAMPLE TESTING========================
(printf "\nEXAMPLE TESTING\n")

(printf ">(atom-count 'b '(a b g a b c b) )\n")
(atom-count 'b '(a b g a b c b) )

(printf ">(atom-count 'g '(a b g a b c b) )\n")
(atom-count 'g '(a b g a b c b) )

(printf ">(atom-count 'w '(a b g a b c b) )\n")
(atom-count 'w '(a b g a b c b) )

(printf ">(atom-count 'b '() )\n")
(atom-count 'b '() )

(printf ">(atom-count 'x '(x xx x xxx x xxxx x) )\n")
(atom-count 'x '(x xx x xxx x xxxx x) )