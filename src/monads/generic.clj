(ns monads.generic
  ;;(:use div.schemey)
  (:use div.syntaxlib))

(defmacro mdo [& fs]
  `(right-associate ~'>> ~@fs))

(defmacro mlet [v+expr & body]
  (let [v (first v+expr) ;; better pattern matching?
        expr (second v+expr)]
    `(~'>>= ~expr
            (fn [~v]
              ~@body))))
