(ns monads.cj
  ;;(:use div.schemey)
   (:use div.syntaxlib))

(defn compose-2 [f1 f2]
  (fn [& x]
    (f1 (apply f2 x))))

;; (defmacro compose [& fs]
;;   )




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

