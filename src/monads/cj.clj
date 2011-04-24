(ns monads.cj
  (:use div.ist))

(defn compose-2 [f1 f2]
  (fn [& x]
    (f1 (apply f2 x))))

;; (defmacro compose [& fs]
;;   )

(defn error [str]
  (throw (Exception. str)))

(defn null? [x]
  (and (list? x) ;; (coll? x)  ?
       (not (seq x))))

(defn _right-associate [op l]
  (let [r (reverse l)
        rst (rest r)]
    (if (null? rst)
      (error "need at least 2 arguments")
      (reduce (fn [r v]
                (list op v r))
              (first r)
              rst))))

(ist (_right-associate 'foo '(10 20 30 40))
     (foo 10 (foo 20 (foo 30 40))))
(ist (_right-associate 'foo '(10 40))
     (foo 10 40))
;; (ist (_right-associate 'foo '(40))
;;      40) ;; hum

(defmacro right-associate [op & l]
  (_right-associate op l))



;; (define (transaction:>> a b)
;;   (lambda (commit cont)
;;     (a commit
;;        (lambda (commit)
;; 	 (b commit
;; 	    cont)))))

(defn transaction:>> [a b]
  (fn [commit cont]
    (a commit
       (fn [commit]
         (b commit
            cont)))))

;; (define (transaction:>>= a b)
;;   (lambda (commit cont)
;;     (a commit
;;        (lambda (commit value)
;; 	 ((b value)
;; 	  commit
;; 	  cont)))))

(defn transaction:>>= [a b]
  (fn [commit cont]
    (a commit
       (fn [commit value]
         ((b value)
          commit
          cont)))))

;; (define (transaction:return val)
;;   (lambda (commit cont)
;;     (cont commit
;; 	  val)))

(defn transaction:return [val]
  (fn [commit cont]
    (cont commit
          val)))

