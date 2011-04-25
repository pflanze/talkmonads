(ns monads.cj
  ;;(:use div.schemey)
   (:use div.syntaxlib))

(defn compose-2 [f1 f2]
  (fn [& x]
    (f1 (apply f2 x))))

(defmacro compose [& fs]
  `(right-associate compose-2 ~@fs))

(ist ((compose #(/ 1 %) #(+ 1 %)) 10)
     1/11)
(ist ((compose #(/ 1 %) #(+ 1 %) #(* 2 %)) 10)
     1/21)
(ist (macroexpand-all '(compose a b c))
     (monads.cj/compose-2 a (monads.cj/compose-2 b c)))


(ns monads.state)

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
  (fn [state cont]
    (a state
       (fn [state value]
         ((b value)
          state
          cont)))))

(defn return [val]
  (fn [state cont]
    (cont state
          val)))

(defn state-ref []
  (fn [state cont]
    (cont state)))

(defn state-set [x]
  ;; with or without "!"?
  (fn [state cont]
    (cont x)))

