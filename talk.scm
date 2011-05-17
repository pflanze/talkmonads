;; Introduction to Monads
;; @Bonjure 17 May 2011

;; * Start from scratch, in Scheme
;; * going to see an unsolved issue
;; * move over to Clojure


;; (NOTE: this is Scheme plus tools from a number of libraries (like
;; define-macro*, match*, iota) that I'm not including here)


(define (hello1)
  (println "Please enter your name:")
  (let ((v (read-line)))
    (print "Hi ")
    (println v)
    (println "I'm hoping you are well."))
  (println "I'm going to stop soon."))

(define (run thunk)
  (read-line) ;; (workaround to drop empty first input line)
  (thunk))

(define (hello2)
  (begin
    (println "Please enter your name:")
    (let ((v (read-line)))
      (begin
       (print "Hi ")
       (println v)
       (println "I'm hoping you are well.")))
    (println "I'm going to stop soon.")))

;; a * b * c
;; (* a b c)

;; a ; b ; c
;; (begin a b c)

(define (begin2 a b) ;; first-then
  (a)
  (b))

(define simplehello1
  (lambda ()
    (begin2
     (lambda () (print "Hello "))
     (lambda ()
       (begin2
	(lambda () (print "World"))
	(lambda () (println ", today.")))))))

;; better:

(define (io:>> a b)
  (lambda ()
    (a)
    (b)))

(define (io:print str)
  (lambda ()
    (print str)))

(define (io:println str)
  (lambda ()
    (println str)))

(define (io:read-line)
  (lambda ()
    (read-line)))

(define simplehello2
  (io:>>
   (io:print "Hello ")
   (io:>>
    (io:print "World")
    (io:println ", today."))))


(define-macro* (io:begin . exprs)
  (match* exprs
	  ((e1 e2 . exprs*)
	   `(io:>> ,e1 (io:begin ,e2 ,@exprs*)))
	  ((e1)
	   e1)))

(define simplehello3
  (io:begin
   (io:print "Hello ")
   (io:print "World")
   (io:println ", today.")))

;; or, using generic syntax:
(define-macro* (m:begin . exprs)
  (match* exprs
	  ((e1 e2 . exprs*)
	   `(>> ,e1 (m:begin ,e2 ,@exprs*)))
	  ((e1)
	   e1)))

;; and aliasing the special combinators to generic names:
(define >> io:>>)
(define >>= io:>>=)
;; (or, use a macro (use-monad io), so was the idea)

;; also, variant of io:>> that passes a value:

(define (io:>>= a b)
  (lambda ()
    (let ((val (a)))
      ((b val)))))

;; how do we make an a that gives a value?:

(define (io:return val)
  (lambda ()
    val))

(define-macro* (m:let binds expr)
  (match* binds
	  ((bind . binds*)
	   `(letm ,bind
		  (m:let ,binds*
			 ,expr)))
	  (()
	   expr)))

(define hello3
  (m:begin
   (io:println "Please enter your name:")
   (m:let ((v (io:read-line)))
	  (m:begin
	   (io:print "Hi ")
	   (io:println v)
	   (io:println "I'm hoping you are well.")))
   (io:println "I'm going to stop soon.")))

(define hello4
  (m:begin
   (io:println "Please enter your name:")
   (m:let ((v io:read-line*))
	  (m:begin
	   (io:print "Hi ")
	   (io:println v)
	   (io:println "I'm hoping you are well.")))
   (io:println "I'm going to stop soon.")))

;; for-each

(define numbers (iota 10))

(define (foreach1)
  (for-each (lambda (x)
	      (print x)
	      (print " "))
	    numbers)
  (println "."))

(define io:noop
  (lambda ()
    (void)))

;; and the alias
(define m:noop io:noop)

(define (m:for-each mproc lis)
  (if (null? lis)
      m:noop
      (let-pair ((a r) lis)
		(>> (mproc a)
		    (m:for-each mproc r)))))

(define foreach2
  (m:begin
   (m:for-each (lambda (x)
		 (m:begin
		  (io:print x)
		  (io:print " ")))
	       numbers)
   (io:println ".")))


;; * but can do other stuff with the same structure

(define (state:>> a b)
  (lambda (state)
    (b (a state))))

;; use two return values:
(define (state:>>= a b)
  (lambda (state)
    (let-values (((state* val) (a state)))
      ((b val) state*))))

(define (state:return val)
  (lambda (state)
    (values state
	    val)))

(define (set-state val)
  (lambda (state)
    val))

(define (get-state)
  (lambda (state)
    (values state
	    state)))

(define state1
  (in-monad
   state
   (m:begin
    (m:let ((v (get-state)))
	   (set-state (begin
			(println v)
			(+ v 1))))
    (m:let ((v (get-state)))
	   (set-state (begin
			(println v)
			(+ v 1)))))))

