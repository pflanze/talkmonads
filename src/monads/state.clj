(ns monads.state
  ;;(:use monads.generic) useless since the import has to be done at
  ;;the end user.
  )

;; reduce is fold-left, but I need a fold-right, and misusing conj
;; with vectors to append at the end as a workaround just seems weird
;; to me. Following Scheme conventions here with the order of
;; arguments to f (first the value, then the rest).
(defn fold-right [f tail s]
  ((fn rec [s]
     (if (seq s)
       (f (first s) (rec (rest s)))
       tail))
   s))

;; debugging print
(defmacro depr [name & vars]
  `(println
    (str
     '~name ": "
     ~@(fold-right (fn [v r]
                     (cons (str v "=")
                           (cons v
                                 (if (seq r)
                                   (cons ", " r)
                                   r))))
                   '()
                   vars))))

;; the monadic type (?) is: (state cont) -> no return (CPS), cont is
;; receiving the new state [[[and new cont.  NO  hm ? ]]]

;; cont in the case of >> receives just state

(defn >> [a b]
  (fn [state cont]
    (a state
       (fn [state]
         (b state
            cont)))))

;; cont in the case of >>= receives [state value]
;; ç

(defn >>= [a b]
  (depr >>= a b)
  (fn [state cont]
    (depr >>=:f state cont)
    (a state
       (fn [state value]
         (depr >>=:f:f state value)
         ((b value)
          state
          cont)))))

(defn return [val]
  (depr return val)
  (fn [state cont]
    (depr return:f state cont)
    (cont state
          val)))

(defn state-ref []
  (fn [state cont]
    (depr state-ref:f state cont)
    (cont state)))

(defn state-set [x]
  ;; with or without "!"?
  (fn [state cont]
    (cont x)))

