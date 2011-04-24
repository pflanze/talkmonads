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

(defn >> [a b]
  (fn [state cont]
    (a state
       (fn [state]
         (b state
            cont)))))

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

