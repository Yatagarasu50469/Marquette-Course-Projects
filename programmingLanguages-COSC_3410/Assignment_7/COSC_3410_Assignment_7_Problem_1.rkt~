#|
================================================================
Program: COSC_3410_Assignment_7_Problem_1
Version: 1.0
Author: David Helminiak
Date Created: 18 November 2018
Date Last Modified: 19 November 2018
================================================================

============================SAMPLE RUN==========================
PROVIDED TEST CASES
================================================================
PROGRAM:
(run '{with {abs {fun {x} {if {< x 0} {- x} x}}} ;; absolute value

   {abs -101}

})

(run '{with {swap {refun {a}
               {refun {b}
                  {with {t a}
                    {seqn {make a b}
                          {make b t}
                    } }}} }
  {with {x 12}
    {with {y -3}
      {with {z 40}
        {with {w 5}
         {seqn
           {if {< x y} {{swap x} y} 0} ;; 0 here just serves as a no-op
           {if {< y z} {{swap y} z} 0}
           {if {< z w} {{swap z} w} 0}
           {if {< x y} {{swap x} y} 0}
           {if {< y z} {{swap y} z} 0}
           {if {< x y} {{swap x} y} 0}
           {show x}
           {show y}
           {show z}
           {show w}
		   99999 ; just return 99999 as the "end of program" value
         }
  }}}}
})

(run '{with {mk-rec {fun {proc}
                   {{fun {g} {g g}}
                    {fun {f}
                         {proc {fun {x} {{f f} x}}}
                        }}} }
      {with {fac {mk-rec {fun {f}
                            {fun {n}
                               {if {= 0 n} 1
                                   {* n {f {- n 1}}}}}}}}
            {fac 7}
      }})

RESULT:
PASSED

================================================================
INDEPENDENT TESTS
================================================================
1. Arbitrary number of sub-expressions

PROGRAM:
(run '{with {x 10} {seqn {show x} {show {* x 2}} {show {* x 3}}}})

OUTPUT:
(numV 10)
(numV 20)
(numV 30)
(v*s (numV 30) (aSto 3 (numV 10) (mtSto)))

RESULT:
PASSED

================================================================
2-1. Addition from right to left

PROGRAM:
(run '{with {x 0} {r+ x {seqn {make x 1}}}})

OUTPUT:
(v*s (numV 1) (aSto 10 (numV 1) (aSto 10 (numV 0) (mtSto))))

RESULT:
PASSED

2-2. Addition from left to right

PROGRAM:
(run '{with {x 0} {+ x {seqn {make x 1}}}})

OUTPUT:
(v*s (numV 2) (aSto 11 (numV 1) (aSto 11 (numV 0) (mtSto))))

RESULT:
PASSED

================================================================
3-1. Function through call-by-value

PROGRAM:
(run '{with {swap {fun {x}
              {fun {y}
{with {z x}
  {seqn {make x y}
        {make y z}}}}}}
  {with {a 5}
  {with {b 4}
      {seqn {{swap a} b}
            b}}}})
OUTPUT:
(v*s
 (numV 4)
 (aSto
  34
  (numV 5)
  (aSto
   33
   (numV 4)
   (aSto
    35
    (numV 5)
    (aSto
     34
     (numV 4)
     (aSto
      33
      (numV 5)
      (aSto
       32
       (numV 4)
       (aSto
        31
        (numV 5)
        (aSto
         30
         (closureV 'x (fun 'y (app (fun 'z (seqn '((make x y) (make y z)))) (id 'x))) (mtSub))
         (mtSto))))))))))

RESULT:
PASSED

3-2. Function through call-by-reference

PROGRAM:
(run '{with {swap {refun {x}
              {fun {y}
{with {z x}
  {seqn {make x y}
        {make y z}}}}}}
  {with {a 5}
  {with {b 4}
      {seqn {{swap a} b}
            b}}}})

OUTPUT:
(v*s
 (numV 4)
 (aSto
  39
  (numV 5)
  (aSto
   37
   (numV 4)
   (aSto
    40
    (numV 5)
    (aSto
     39
     (numV 4)
     (aSto
      38
      (numV 4)
      (aSto
       37
       (numV 5)
       (aSto
        36
        (refclosV 'x (fun 'y (app (fun 'z (seqn '((make x y) (make y z)))) (id 'x))) (mtSub))
        (mtSto)))))))))

RESULT:
PASSED

================================================================
|#

;============================PROGRAM============================
#lang plai

;;DEFINITIONS


(define-type JILL
  [num (n number?)]
  [leftadd (lhs JILL?) (rhs JILL?)]
  [rightadd (lhs JILL?) (rhs JILL?)]
  [sub (lhs JILL?) (rhs JILL?)]
  [mult (lhs JILL?) (rhs JILL?)]
  [div (lhs JILL?) (rhs JILL?)]
  [neg (lhs JILL?)]
  [equalVal? (lhs JILL?) (rhs JILL?)]
  [lessThan? (lhs JILL?) (rhs JILL?)]
  [iff? (test-expr JILL?) (lhs JILL?) (rhs JILL?)]
  [id (name symbol?)]
  [fun (param symbol?) (body JILL?)]
  [app (fun-expr JILL?) (arg JILL?)]
  [refun (param symbol?) (body JILL?)]
  [make (name symbol?)(value-expr JILL?)]
  [show (expr JILL?)]
  [seqn (elList list?)])

(define-type JILL-Value
  [numV (n number?)]
  [closureV (param symbol?)
            (body JILL?)
            (env Env?)]
  [refclosV (param symbol?)
            (body JILL?)
            (env Env?)]
  [booleanV (b boolean?)])

(define-type Value*Store
  [v*s (value JILL-Value?) (store Store?)]
)

(define-type Env [mtSub]
  [aSub (name symbol?)
        (location number?)
        (env Env?)])

(define-type Store [mtSto]
  [aSto (location number?)
        (value JILL-Value?)
        (store Store?)])

;; env-lookup : symbol Env ! location
(define (env-lookup name env)
  (type-case Env env
    [mtSub () (error 'env-lookup "no binding found for id ~a" name)]
    [aSub (bound-name bound-location rest-env)
          (if (symbol=? name bound-name)
              bound-location
              (env-lookup name rest-env))]
 ))

;; store-lookup : location Store ! BCFAE-Value
(define (store-lookup loc-index sto)
  (type-case Store sto
    [mtSto () (error 'store-lookup "no value at location")]
    [aSto (location value rest-store)
          (if (= location loc-index)
              value
              (store-lookup loc-index rest-store))]))

;; next-location : -> number
(define next-location
  (local [(define last-loc (box -1))]
    (lambda (store)
      (begin (set-box! last-loc (+ (unbox last-loc) 1))
             (unbox last-loc)))))

(define (run sexp)
  (interp (parse sexp) (mtSub) (mtSto))
  )

;;SCHEME EVALUATIONS


(define (num_add n1 n2)
  (numV (+ (numV-n n1)
           (numV-n n2))))

(define (num_sub n1 n2)
  (numV (- (numV-n n1)
           (numV-n n2))))

(define (num_mult n1 n2)
  (numV (* (numV-n n1)
           (numV-n n2))))

(define (num_div n1 n2)
  (numV (quotient (numV-n n1)
           (numV-n n2))))

(define (num_neg n1)
  (numV (- (numV-n n1))))

(define (num_eq n1 n2)
  (booleanV (eq? (numV-n n1)
           (numV-n n2))))

(define (num_lt n1 n2)
  (booleanV (< (numV-n n1)
           (numV-n n2))))

(define (num-zero? n)
  (zero? (numV-n n)))


;;INTERPRETER

;; interp : JILL Env Store ! Value*Store
(define (interp expr env store)
  (type-case JILL expr
    [num (n) (v*s (numV n) store)]
    [leftadd (l r)
         (type-case Value*Store (interp l env store)
           [v*s (l-value l-store)
                (type-case Value*Store (interp r env l-store)
                  [v*s (r-value r-store)
                       (v*s (num_add l-value r-value)
                            r-store)])])]
    [rightadd (r l)
         (type-case Value*Store (interp l env store)
           [v*s (l-value l-store)
                (type-case Value*Store (interp r env l-store)
                  [v*s (r-value r-store)
                       (v*s (num_add l-value r-value)
                            r-store)])])]
    [sub (l r)
         (type-case Value*Store (interp l env store)
           [v*s (l-value l-store)
                (type-case Value*Store (interp r env l-store)
                  [v*s (r-value r-store)
                       (v*s (num_sub l-value r-value)
                            r-store)])])]
    [mult (l r)
         (type-case Value*Store (interp l env store)
           [v*s (l-value l-store)
                (type-case Value*Store (interp r env l-store)
                  [v*s (r-value r-store)
                       (v*s (num_mult l-value r-value)
                            r-store)])])]
    [div (l r)
         (type-case Value*Store (interp l env store)
           [v*s (l-value l-store)
                (type-case Value*Store (interp r env l-store)
                  [v*s (r-value r-store)
                       (v*s (num_div l-value r-value)
                            r-store)])])]
    [neg (n)
         (type-case Value*Store (interp n env store)
           [v*s (l-value l-store)
                (v*s (num_neg l-value)
                     l-store)
           ]
         )
    ]
    [equalVal? (l r)
         (type-case Value*Store (interp l env store)
           [v*s (l-value l-store)
                (type-case Value*Store (interp r env l-store)
                  [v*s (r-value r-store)
                       (v*s (num_eq l-value r-value)
                            r-store)])])]
    [lessThan? (l r)
         (type-case Value*Store (interp l env store)
           [v*s (l-value l-store)
                (type-case Value*Store (interp r env l-store)
                  [v*s (r-value r-store)
                       (v*s (num_lt l-value r-value)
                            r-store)])])]
    [iff? (test l r)
          (type-case Value*Store (interp test env store)
            [v*s (test-value test-store)
                 (cond
                   [(booleanV-b test-value) (interp l env test-store)]
                   [else (interp r env test-store)]
                 )
            ]
          )
    ]
    [id (name) (v*s (store-lookup (env-lookup name env) store) store)]
    [fun (bound-id bound-body)
         (v*s (closureV bound-id bound-body env) store)]
    [refun (bound-id bound-body)
           (v*s (refclosV bound-id bound-body env) store)]
    [app (fun-expr arg-expr)
         (type-case Value*Store (interp fun-expr env store)
           [v*s (fun-value fun-store)
                (type-case JILL-Value fun-value
                  [closureV (cl-param cl-body cl-env)
                            (type-case Value*Store (interp arg-expr env fun-store)
                              [v*s (arg-value arg-store)
                                   (local ([define new-loc (next-location arg-store)])
                                     (interp cl-body
                                             (aSub cl-param
                                                   new-loc
                                                   cl-env)
                                             (aSto new-loc
                                                   arg-value
                                                   arg-store)))])]
                  [refclosV (cl-param cl-body cl-env)
                            (local ([define arg-loc (env-lookup (id-name arg-expr) env)])
                              (interp cl-body
                                      (aSub cl-param
                                            arg-loc
                                            cl-env)
                                      fun-store))]
                  [numV (_) (error 'interp "trying to apply a number")]
                  [booleanV (_) (error 'interp "trying to apply a boolean")])])]
    [make (name value-expr)
          (type-case Value*Store (interp value-expr env store)
            [v*s (value-value value-store)
                 (local ([define the-loc (env-lookup name env)])
                   (v*s value-value
                        (aSto the-loc value-value value-store)))])]
    [show (expr)
          (type-case Value*Store (interp expr env store)
            [v*s (expr-value expr-store)
                 (begin (print expr-value)
                        (newline)
                        (v*s expr-value
                             expr-store))])]
    [seqn (elList)
          (cond
            [(null? elList) (error "seqn list was empty")]
            [(eq? (length elList) 1)
             (type-case Value*Store (interp (parse (car elList)) env store)
             [v*s (eFirst-value eFirst-store)
                  (v*s eFirst-value eFirst-store)
             ])
            ]
            [else (type-case Value*Store (interp (parse (car elList)) env store)
                  [v*s (eFirst-value eFirst-store)
                       (interp (seqn (cdr elList)) env eFirst-store)
                  ])
            ]
          )
    ]
    ))

;;PARSER


;Define parse of s-expressions into JILLs
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
                  [(eq? (length sexp) 3) (leftadd (parse (second sexp)) (parse (third sexp)))] ;If sexp length 3, perform standard addition
                  [else (error "A highly improbable and unknown error has occured for add function")] ;Indicate unspecified error
                )
          ]
          [(r+) (cond ;Set conditionals
                  [(> (length sexp) 3) (error "add list contains too many values; currently have:" (length sexp))] ;Max length check
                  [(< (length sexp) 3) (error "add list is missing arguments; currently:" (length sexp))] ;Min length check
                  [(eq? (length sexp) 3) (rightadd (parse (second sexp)) (parse (third sexp)))] ;If sexp length 3, perform standard addition
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
                                          [(neg (parse (second sexp)))] ;Perform unary subtraction
                                          [else (error "A highly improbable and unknown error has occured for the uminus function")] ;Indicate unspecified error
                                        )
                 ]
                 [(eq? (length sexp) 3) (cond ;If the length is 3, set the following conditions for subtraction
                                          [(sub (parse (second sexp)) (parse (third sexp)))] ;Perform standard subtraction
                                          [else (error "A highly improbable and unknown error has occured for the sub function")] ;Indicate unspecified error
                                        )
                 ]
                 [else (error "A highly improbable and unknown error has occured for either the subtraction or uminus function")] ;Indicate unspecified error
               )
          ]
          [(=) (cond ;Set conditionals
                 [(> (length sexp) 3) (error "equalVal? list contains too many values; currently have:" (length sexp))] ;Max length check
                 [(< (length sexp) 3) (error "equalVal? list is missing arguments; currently have:" (length sexp))] ;Min length check
                 [(eq? (length sexp) 3) (equalVal? (parse (second sexp)) (parse (third sexp)))] ;If sexp length 3, perform standard operation
                 [else (error "An unknown error has occured for equalVal? function")] ;Indicate unspecified error
               )
          ]
          [(<) (cond ;Set conditionals
                 [(> (length sexp) 3) (error "lessThan? list contains too many values; currently have:" (length sexp))] ;Max length check
                 [(< (length sexp) 3) (error "lessThan? list is missing arguments; currently have:" (length sexp))] ;Min length check
                 [(eq? (length sexp) 3) (lessThan? (parse (second sexp)) (parse (third sexp)))] ;If sexp length 3, perform standard operation
                 [else (error "An unknown error has occured for lessThan? function")] ;Indicate unspecified error
               )
          ]
          [(if) (iff? (parse (second sexp)) (parse (third sexp)) (parse (fourth sexp)))]
          [(with) (app (fun (caadr sexp) (parse (third sexp))) (parse (cadadr sexp)))]
          [(app) (app (parse (first sexp)) (parse (second sexp)))]
          [(fun) (fun (caadr sexp) (parse (third sexp)))]
          [(refun) (refun (caadr sexp) (parse (third sexp)))]
          [(make) (make (second sexp) (parse (third sexp)))]
          [(show) (show (parse (second sexp)))]
          [(seqn) (seqn (cdr sexp))]
          [else (app (parse(first sexp)) (parse (second sexp)))]
        )
       ]
      )
    ]
  )
)

  