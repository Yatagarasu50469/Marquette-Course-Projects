#|
================================================================
Program: COSC_3410_Assignment_8_Problem_1
Version: 1.0
Author: David Helminiak
Date Created: 27 November 2018
Date Last Modified: 28 November 2018
================================================================

============================SAMPLE RUN==========================
PROVIDED TEST CASES
================================================================
SHOULD WORK PROGRAMS:

(get-type (parse '{with {x : number 3} {= x 3}}) (mtTypeSub))

(typeTest? '{with {x : number 2}
  {with {y : number 12}
    {+ {* x x} {* y y}}
}})

(typeTest? '{- 1 {- 2 {- 3 {- 4 {- 5 6}}}}})

(typeTest? '{with {x : number 3} ;; this should evaluate to a boolean value
  {= x 2}})


(typeTest? '{with {not : (boolean -> boolean)
         {fun {v : boolean}
           {if v {= 0 1} {= 0 0}}
      }  }
   {with {limit : number 44}
      {with {flag : boolean {< 70 limit}}
        {not flag}
   }  }
})


(typeTest? '{if {= {* 5 21} {* 7 15}}
    9999
    5555
})

(typeTest? '{with {number : (number -> number) {fun {number : number}
        {* number number}}}
   {with {num : number 8}
     {number num} }})


(typeTest? '{with {x : number 9}    ; you can change these vals, but the larger one should
  {with {y : number 3}  ; always wind up at the left of the final number
               ; and the smaller one at the right
     {with  {min : number  {if {< x y}
                     x 
                     y}}
        {with  {max : number  {if {< x y}
                        y 
                        x}}
           {+ {* max 1000} min} ; output will be best if smaller is
 }}}})                           ; no more than two digits


(typeTest? '{with {double : (number -> number) {fun {n : number} {* 2 n}}}
  {double 12}})

(typeTest? '{with {abs : (number -> number) {fun {x : number} {if {< x 0} {- x} x}}} ;; absolute value
   {abs -101}
})

(typeTest? '{with {mod-base : (number -> (number -> number)) {fun {b : number}
                  {fun {n : number}
                    {- n {* b {/ n b}}}
                  }
                }
      }
  {{mod-base 7} 11} ;; Compute 11 mod 7
})


(typeTest? '{with {mod : (number -> (number -> number)) {fun {n : number}
                  {fun {b : number}
                    {- n {* b {/ n b}}}
                  }
                }
      }
  {{mod 100} 7} ;; Compute 100 mod 7
})

RESULT:
PASSED

================================================================
ERRORS FROM PARSER PROGRAMS:

(typeTest? '{+ 3 4 5})

(typeTest? '{with {x:number 3}
   x})
   
(typeTest? '{with {x 3}
   x})

(typeTest? '{6 - 7})

(typeTest? '{fun {n : number 12}
   {+ n 3}})

(typeTest? '{fun {n 12} : number
   {+ n 3}})

(typeTest? '{with {y : number} {+ x y}})

(typeTest? '{with {number {fun {number : number}
        {* number number}}}
   {with {num : number 8}
     {number num} }})


RESULT:
PASSED

================================================================
ERROR FROM TYPE CHECKER PROGRAMS:

(typeTest? '{* 3 {< 7 8}})


(typeTest? '{with {double : (number -> number) {fun {n : number} {* 2 n}}}
  {with {x : number 7}
    {if {= x 10} x double}
 }})

(typeTest? '{if 5 3 4})


(typeTest? '{with {double : (number -> number) {fun {n : number} {* 2 n}}}
  {with {val : boolean {< 8 7}}
    {double val}}})
    
    
(typeTest? '{with {f : number 10}
  {f 3}
})

(typeTest? '{with {b : boolean {= 0 0}}
    {/ b 45} })

(typeTest? '{with {b : boolean 5}
    b })

(typeTest? '{with {not : (boolean -> boolean)
         {fun {v : boolean}
           {if v {= 0 1} {= 0 0}}
      }  }
   {not 11}
})

RESULT:
PASSED
|#

;============================PROGRAM============================
#lang plai

;;DEFINITIONS


(define-type TJILL
  [num (n number?)]
  [add (lhs TJILL?) (rhs TJILL?)]
  [sub (lhs TJILL?) (rhs TJILL?)]
  [mult (lhs TJILL?) (rhs TJILL?)]
  [div (lhs TJILL?) (rhs TJILL?)]
  [neg (lhs TJILL?)]
  [equalVal? (lhs TJILL?) (rhs TJILL?)]
  [lessThan? (lhs TJILL?) (rhs TJILL?)]
  [iff? (test-expr TJILL?) (lhs TJILL?) (rhs TJILL?)]
  [id (name symbol?)]
  [fun (param symbol?) (ptype Type?) (body TJILL?)]
  [app (fun-expr TJILL?) (arg TJILL?)]
  [boolean (bool TJILL?)])

(define-type TJILL-Value
  [numV (n number?)]
  [closureV (param symbol?)
            (body TJILL?)
            (type Type?)
            (env Env?)]
  [booleanV (b boolean?)])


(define-type Value*Store
  [v*s (value TJILL-Value?) (store Store?)]
)


(define-type Env [mtSub]
  [aSub (name symbol?)
        (location number?)
        (env Env?)])

(define-type TypeEnv [mtTypeSub]
  [aTypeSub (id symbol?)
            (type Type?)
            (typeEnv TypeEnv?)])

(define-type Store [mtSto]
  [aSto (location number?)
        (value TJILL-Value?)
        (store Store?)])

(define-type Type
  [numType]
  [boolType]
  [funType (domain Type?)
           (codomain Type?)])

;; env-lookup : symbol Env ! location
(define (env-lookup name env)
  (type-case Env env
    [mtSub () (error 'env-lookup "no binding found for id ~a" name)]
    [aSub (bound-name bound-location rest-env)
          (if (symbol=? name bound-name)
              bound-location
              (env-lookup name rest-env))]
 ))

;; typeEnv-lookup : symbol TypeEnv ! location
(define (type-lookup name typeEnv)
  (type-case TypeEnv typeEnv
    [mtTypeSub () (error 'typeEnv-loopup "no binding found for specified type ~a" name)]
    [aTypeSub (bound-name bound-type rest-typeEnv)
          (if (symbol=? name bound-name)
              bound-type
              (type-lookup name rest-typeEnv))]
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

(define (get-type expr typexp)
  (cond [(TJILL? expr)
         (type-case TJILL expr
           [num (n) (numType)]
           [boolean (b) (boolType)]
           [add (l r) (cond
                        [(equal? (numType? (get-type l typexp)) #t) (cond
                                                          [(equal? (numType? (get-type r typexp)) #t) (numType)]
                                                          [else (error "Wrong type in get-type function for add")]
                                                    )
                        ]
                      )
           ]
           [sub (l r) (cond
                        [(equal? (numType? (get-type l typexp)) #t) (cond
                                                          [(equal? (numType? (get-type r typexp)) #t) (numType)]
                                                          [else (error "Wrong type in get-type function for sub")]
                                                    )
                        ]
                      )
           ]
           [mult (l r) (cond
                        [(equal? (numType? (get-type l typexp)) #t) (cond
                                                          [(equal? (numType? (get-type r typexp)) #t) (numType)]
                                                          [else (error "Wrong type in get-type function for mult")]
                                                    )
                        ]
                      )
           ]
           [div (l r) (cond
                        [(equal? (numType? (get-type l typexp)) #t) (cond
                                                          [(equal? (numType? (get-type r typexp)) #t) (numType)]
                                                          [else (error "Wrong type in get-type function for div")]
                                                    )
                        ]
                      )
           ]
           [neg (n) (cond
                        [(equal? (numType? (get-type n typexp)) #t) (numType)]
                        [else (error "Wrong type in get-type function for neg")]
                    )
           ]
           [equalVal? (l r) (cond
                              [(equal? (get-type l typexp) (get-type r typexp)) (boolType) ]
                              [else (error "Wrong type in get-type function for equalVal?")]
                            )
           ]
           [lessThan? (l r) (cond
                        [(equal? (numType? (get-type l typexp)) #t) (cond
                                                          [(equal? (numType? (get-type r typexp)) #t) (boolType)]
                                                          [else (error "Wrong type in get-type function for lessThan?")]
                                                    )
                        ]
                      )
           ]
           [iff? (test l r) (cond
                              [(equal? (get-type test typexp) (boolType)) (cond
                                                                               [(equal? (get-type l typexp) (get-type r typexp)) (get-type l typexp)]
                                                                               [else (error "Value types in get-type function for iff? do not match")]
                                                                             )
                              ]
                              [else (error "Wrong type provided for test in get-type function for iff?")]
                            )
           ]
           [id (name) (type-lookup name typexp)]
           [fun (bound-id bound-type bound-body) (funType bound-type (get-type bound-body (aTypeSub bound-id bound-type typexp)))]
           [app (fun-expr arg-expr) (cond
                                      [null? fun-expr (error "Null value entered for the fun-expr within app")]
                                      [null? arg-expr (error "Null value entered for the arg-expr within app")]
                                      [(funType? (get-type fun-expr typexp)) (cond
                                                                               [(equal? (get-type arg-expr typexp) (funType-domain (get-type fun-expr typexp))) (funType-codomain (get-type fun-expr typexp))]
                                                                               [else (error "funtype specified for app had invalid parameters")]
                                                                             )
                                      ]
                                      [else (error "Unknown type submitted to app")]
                                    )
           ]
         )
        ]
  )
)

(define (type-parse typexp)
  (cond
    [(null? typexp) (error "type-parse input was null")]
    [(symbol? typexp)
     (cond
       [(equal? typexp 'number) (numType)]
       [(equal? typexp 'boolean) (boolType)]
       [else (funType (type-parse (first typexp)) (type-parse (third typexp)))]
     )
    ]
    [(list? typexp)
      (cond 
            [(equal? (length typexp) 3) (funType (type-parse (first typexp)) (type-parse (third typexp)))]
            [else (error "list input to type-parse was of an incorrect length")]
      )
     ]
    [else (error "Incorrect typexp submitted to type-parse")]
  )
)

(define (typeTest? typexp)
  (get-type (parse typexp) (mtTypeSub))
)

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

;; interp : TJILL Env Store ! Value*Store
(define (interp expr env store)
  (type-case TJILL expr
    [num (n) (v*s (numV n) store)]
    [boolean (b) (v*s (booleanV b) store)]
    [add (l r)
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
    [fun (bound-id bound-type bound-body)
         (v*s (closureV bound-id bound-type bound-body env) store)]
    [app (fun-expr arg-expr)
         (type-case Value*Store (interp fun-expr env store)
           [v*s (fun-value fun-store)
                (type-case TJILL-Value fun-value
                  [closureV (cl-param cl-body cl-type cl-env)
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
                  [numV (_) (error 'interp "trying to apply a number")]
                  [booleanV (_) (error 'interp "trying to apply a boolean")])])]
    ))

;;PARSER


;Define parse of s-expressions into TJILLs
(define (parse sexp)
  (cond  ;Set conditionals
    [(number? sexp) (num sexp)]
    [(symbol? sexp) (id sexp)] ;If the sexp is a symbol, return its id
    [(boolean? sexp) (boolean sexp)]
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
          [(with) (cond; Set conditionals
                    [(> (length sexp) 3) (error "with list contains too many values; currently have:" (length sexp))] ;Max length check
                    [(< (length sexp) 3) (error "with list is missing arguments; currently have:" (length sexp))] ;Min length check
                    [(eq? (length sexp) 3) (cond
                                              [(> (length (second sexp)) 4) (error "second element in with list contains too many values; currently have:" (length (second sexp)))] ;Max length check
                                              [(< (length (second sexp)) 4) (error "second element in with list is missing arguments; currently have:" (length (second sexp)))] ;Min length check
                                              [(symbol? (first (second sexp))) (app (fun (caadr sexp) (type-parse (third (second sexp))) (parse (third sexp))) (parse (fourth (second sexp))))]
                                              [else (error "second element in with list is missing symbol as first expression")]
                                           )
                    ]
                    [else (error "An unknown error has occured for with function")] ;Indicate unspecified error
                  )
          ]
          [(fun) (cond
                   [(> (length sexp) 3) (error "fun list contains too many values; currently have:" (length sexp))] ;Max length check
                   [(< (length sexp) 3) (error "fun list is missing arguments; currently have:" (length sexp))] ;Min length check
                   [(eq? (length sexp) 3) (cond
                                            [(> (length (second sexp)) 3) (error "second element in fun list contains too many values; currently have:" (length (second sexp)))] ;Max length check
                                            [(< (length (second sexp)) 3) (error "second element in fun list is missing arguments; currently have:" (length (second sexp)))] ;Min length check
                                            [(eq? (length (second sexp)) 3) (fun (caadr sexp) (type-parse (third (second sexp))) (parse (third sexp)))]
                                            [else (error "A highly improbable and unknown error has occured for the fun function")]
                                          )
                   ]
                   [else (error "An unknown error has occured for fun function")] ;Indicate unspecified error
                 )
          ]
          [else (app (parse(first sexp)) (parse (second sexp)))]
        )
       ]
      )
    ]
    [else (error "Unknown function, or one with incorrect types being used, was passed to the parser")]
  )
)

