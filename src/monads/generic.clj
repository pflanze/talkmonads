(ns monads.generic
  ;;(:use div.schemey)
  (:use div.syntaxlib))

(defmacro mdo [& fs]
  `(right-associate ~'>> ~@fs))

(defmacro mlet [v+vexpr bexpr]
  (let [v (first v+vexpr) ;; better pattern matching?
        vexpr (second v+vexpr)]
    `(~'>>= ~vexpr
            (fn [~v]
              ~bexpr))))
